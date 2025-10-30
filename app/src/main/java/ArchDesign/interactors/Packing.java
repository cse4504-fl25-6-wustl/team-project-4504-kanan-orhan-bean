package ArchDesign.interactors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.LinkedHashMap;

import ArchDesign.entities.Art;
import ArchDesign.entities.Box;
import ArchDesign.entities.Container;
import ArchDesign.entities.Client;
import ArchDesign.requests.Request;
import ArchDesign.responses.Response;

public class Packing {

    /** DTO for the JSON oversized_pieces array */
    public static class oversizeObjects {
        public final int side1; // longer side
        public final int side2; // shorter side
        public final int quantity;

        public oversizeObjects(int side1, int side2, int quantity) {
            this.side1 = side1;
            this.side2 = side2;
            this.quantity = quantity;
        }
    }

    private Client currentClient;

    public Response packEverything(Request request) {
        Objects.requireNonNull(request, "request must not be null");
        this.currentClient = Objects.requireNonNull(request.getClient(), "client must not be null");
        final List<Art> arts = Objects.requireNonNull(request.getArts(), "arts must not be null");

        // 1) Box up the art
        List<Box> boxes = constructBoxesForArtsHypo(arts);

        // 2) Containerize (try both pallet-first and oversize-first, choose fewer)
        var caps = currentClient.getDeliveryCapabilities();
        boolean acceptsPallets = caps.doesAcceptPallets();
        boolean acceptsCrates = caps.doesAcceptCrates();

        List<Container> containersPallet = constructContainersForBoxesLocal(boxes, acceptsPallets, acceptsCrates);
        List<Container> containersOversize = constructContainersForBoxesLocalOversize(boxes, acceptsPallets,
                acceptsCrates);
        List<Container> containers = (containersOversize.size() < containersPallet.size())
                ? containersOversize
                : containersPallet;

        // 3) Weights
        int totalArtworkWeight = sumArtworkWeight(arts);
        int finalShipmentWeight = computeTotalWeight(arts, boxes, containers);
        int totalPackagingWeight = Math.max(0, finalShipmentWeight - totalArtworkWeight);

        // 4) Counts & metrics for schema
        int totalPieces = arts.size();
        int standardSizePieces = (int) arts.stream().filter(a -> !a.isOversized()).count();
        int customPieceCount = (int) arts.stream().filter(Art::isCustom).count();

        int standardBoxCount = (int) boxes.stream().filter(b -> !b.isOversized() && !b.isCustom()).count();
        int largeBoxCount = (int) boxes.stream().filter(Box::isOversized).count();

        int standardPalletCount = (int) containers.stream().filter(c -> c.getType() == Container.Type.Pallet).count();
        int oversizedPalletCount = (int) containers.stream().filter(c -> c.getType() == Container.Type.Oversize)
                .count();
        int crateCount = (int) containers.stream().filter(c -> c.getType() == Container.Type.Crate).count();

        oversizeObjects[] oversizedPieces = buildOversizePieces(arts);

        // 5) Build the Response (no summary string here—serializers will handle
        // presentation)
        return new Response(
                arts,
                boxes,
                containers,
                totalPieces,
                standardSizePieces,
                oversizedPieces,
                standardBoxCount,
                largeBoxCount,
                customPieceCount,
                standardPalletCount,
                oversizedPalletCount,
                crateCount,
                totalArtworkWeight,
                totalPackagingWeight,
                finalShipmentWeight);
    }

    /* ======================== metrics helpers ======================== */

    private static int sumArtworkWeight(List<Art> arts) {
        int sum = 0;
        for (Art a : arts) {
            if (a != null)
                sum += a.getWeight();
        }
        return sum;
    }

    /**
     * Groups oversized pieces by (longerSide, shorterSide) and counts quantity.
     * side lengths are rounded up to integers to match the example schema.
     */
    private static oversizeObjects[] buildOversizePieces(List<Art> arts) {
        Map<String, oversizeObjects> bucket = new LinkedHashMap<>();
        for (Art a : arts) {
            if (!a.isOversized())
                continue;
            int w = (int) Math.ceil(a.getWidth());
            int h = (int) Math.ceil(a.getHeight());
            int s1 = Math.max(w, h);
            int s2 = Math.min(w, h);
            String key = s1 + "x" + s2;
            oversizeObjects cur = bucket.get(key);
            if (cur == null) {
                bucket.put(key, new oversizeObjects(s1, s2, 1));
            } else {
                bucket.put(key, new oversizeObjects(cur.side1, cur.side2, cur.quantity + 1));
            }
        }
        return bucket.values().toArray(new oversizeObjects[0]);
    }

    private int computeTotalWeight(List<Art> arts, List<Box> boxes, List<Container> containers) {
        try {
            if (!containers.isEmpty()) {
                int sum = 0;
                for (Container c : containers)
                    sum += safeContainerWeight(c);
                if (sum > 0)
                    return sum;
            }
            if (!boxes.isEmpty()) {
                int sum = 0;
                for (Box b : boxes)
                    sum += safeBoxWeight(b);
                if (sum > 0)
                    return sum;
            }
        } catch (Exception ignored) {
        }
        return sumArtworkWeight(arts);
    }

    /* ===================== packing helpers ===================== */

    /**
     * Build boxes for all arts. Mirrors are modeled as hypothetical boxes:
     * one mirror per box; no mixing mirrors with other art inside a box.
     * Non-mirrors are packed greedily (largest side first) using Box.canArtFit().
     */
    private static List<Box> constructBoxesForArtsHypo(List<Art> items) {
        List<Box> boxes = new ArrayList<>();
        if (items == null || items.isEmpty())
            return boxes;

        // partition
        List<Art> mirrors = new ArrayList<>();
        List<Art> others = new ArrayList<>();
        for (Art a : items) {
            if (a.getType() == Art.Type.Mirror)
                mirrors.add(a);
            else
                others.add(a);
        }

        // non-mirrors: greedy largest-first
        others.sort(Comparator.comparingDouble(a -> -Math.max(a.getWidth(), a.getHeight())));
        for (Art a : others) {
            Box placedBox = null;
            for (Box b : boxes) {
                try {
                    if (b.canArtFit(a)) {
                        b.addArt(a);
                        placedBox = b;
                        break;
                    }
                } catch (Exception ignored) {
                }
            }
            if (placedBox == null) {
                Box b = new Box();
                b.addArt(a);
                boxes.add(b);
            }
        }

        // mirrors: one per box
        for (Art m : mirrors) {
            Box b = new Box();
            b.addArt(m);
            boxes.add(b);
        }
        return boxes;
    }

    /** Pallet-default strategy */
    private static List<Container> constructContainersForBoxesLocal(
            List<Box> myBoxes, boolean acceptsPallets, boolean canAcceptCrates) {

        List<Container> result = new ArrayList<>();
        if (myBoxes == null || myBoxes.isEmpty())
            return result;
        if (!acceptsPallets && !canAcceptCrates)
            return result;

        myBoxes.sort(Comparator.comparing(Box::isOversized).thenComparing(Box::isCustom));

        for (Box box : myBoxes) {
            boolean added = false;

            for (Container cont : result) {
                try {
                    if (cont.canBoxFit(box)) {
                        cont.addBox(box);
                        added = true;
                        break;
                    }
                } catch (Exception ignored) {
                }
            }

            if (!added) {
                Container.Type preferred = (isMirrorOnly(box) && canAcceptCrates) ? Container.Type.Crate
                        : acceptsPallets ? Container.Type.Pallet : canAcceptCrates ? Container.Type.Crate : null;
                if (preferred == null)
                    continue;

                if (!tryCreateAndPlace(box, preferred, canAcceptCrates, result)) {
                    if (preferred == Container.Type.Pallet && canAcceptCrates) {
                        tryCreateAndPlace(box, Container.Type.Crate, canAcceptCrates, result);
                    } else if (preferred == Container.Type.Crate && acceptsPallets) {
                        tryCreateAndPlace(box, Container.Type.Pallet, canAcceptCrates, result);
                    }
                }
            }
        }
        return result;
    }

    /** Oversize-pallet-default strategy */
    private static List<Container> constructContainersForBoxesLocalOversize(
            List<Box> myBoxes, boolean acceptsPallets, boolean canAcceptCrates) {

        List<Container> result = new ArrayList<>();
        if (myBoxes == null || myBoxes.isEmpty())
            return result;
        if (!acceptsPallets && !canAcceptCrates)
            return result;

        myBoxes.sort(Comparator.comparing(Box::isOversized).thenComparing(Box::isCustom));

        for (Box box : myBoxes) {
            boolean added = false;

            for (Container cont : result) {
                try {
                    if (cont.canBoxFit(box)) {
                        cont.addBox(box);
                        added = true;
                        break;
                    }
                } catch (Exception ignored) {
                }
            }

            if (!added) {
                Container.Type preferred = (isMirrorOnly(box) && canAcceptCrates) ? Container.Type.Crate
                        : acceptsPallets ? Container.Type.Oversize : canAcceptCrates ? Container.Type.Crate : null;
                if (preferred == null)
                    continue;

                if (!tryCreateAndPlace(box, preferred, canAcceptCrates, result)) {
                    if (preferred == Container.Type.Oversize && canAcceptCrates) {
                        tryCreateAndPlace(box, Container.Type.Crate, canAcceptCrates, result);
                    } else if (preferred == Container.Type.Crate && acceptsPallets) {
                        tryCreateAndPlace(box, Container.Type.Pallet, canAcceptCrates, result);
                    }
                }
            }
        }
        return result;
    }

    // Create a container, try to place, only append if successful.
    private static boolean tryCreateAndPlace(Box box, Container.Type type, boolean canAcceptCrates,
            List<Container> out) {
        Container fresh = new Container(type, canAcceptCrates);
        try {
            if (fresh.canBoxFit(box)) {
                fresh.addBox(box);
                out.add(fresh);
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    private static boolean isMirrorOnly(Box b) {
        List<Art> arts = b.getArts();
        if (arts == null || arts.isEmpty())
            return false;
        for (Art a : arts)
            if (a.getType() != Art.Type.Mirror)
                return false;
        return true;
    }

    /* ============================== safe weights ============================== */

    private static double safeBoxWeight(Box b) {
        try {
            return (b != null) ? b.getWeight() : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    private static double safeContainerWeight(Container c) {
        try {
            return (c != null) ? c.getWeight() : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
}
