package ArchDesign.interactors;

import java.util.ArrayList;
import java.util.Collections;
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
        public final int side1; // longer side (height)
        public final int side2; // shorter side (width)
        public final int quantity;
        public final transient int weight;

        public oversizeObjects(int side1, int side2, int quantity, int weight) {
            this.side1 = side1;
            this.side2 = side2;
            this.quantity = quantity;
            this.weight = weight;
        }
    }

    private Client currentClient;

    public Response packEverything(Request request) {
        Objects.requireNonNull(request, "request must not be null");
        this.currentClient = Objects.requireNonNull(request.getClient(), "client must not be null");
        final List<Art> arts = Objects.requireNonNull(request.getArts(), "arts must not be null");

        List<Art> sortedArts = new ArrayList<>(arts);
        Collections.sort(sortedArts);

        List<Box> boxes = null;
        List<Container> containers = null;
        if (this.currentClient.getDeliveryCapabilities().doesAcceptCrates()){
            boxes = new ArrayList<>();
            containers = constructCratesForArtsHypo(sortedArts, true);
        }
        else {
            // 1) Box up the art
            boxes = constructBoxesForArtsHypo(sortedArts);

            List<Box> sortedBoxes = new ArrayList<>(boxes);
            Collections.sort(sortedBoxes);

            // 2) Containerize (try both pallet-first and oversize-first, choose fewer)
            var caps = currentClient.getDeliveryCapabilities();
            boolean acceptsPallets = caps.doesAcceptPallets();
            boolean acceptsCrates = caps.doesAcceptCrates();

            containers = constructContainersForBoxesLocalStandardAndOversize(sortedBoxes, acceptsPallets, acceptsCrates);

            // List<Container> containersPallet = constructContainersForBoxesLocal(boxes, acceptsPallets, acceptsCrates);
            // List<Container> containersOversize = constructContainersForBoxesLocalOversize(boxes, acceptsPallets,
            //         acceptsCrates);
            // containers = (containersOversize.size() < containersPallet.size())
            //         ? containersOversize
            //         : containersPallet;
        }

        // 3) Weights
        int totalArtworkWeight = sumArtworkWeight(sortedArts);
        int finalShipmentWeight = computeTotalWeight(sortedArts, boxes, containers);
        int totalPackagingWeight = Math.max(0, finalShipmentWeight - totalArtworkWeight);

        // 4) Counts & metrics for schema
        int totalPieces = sortedArts.size();
        int standardSizePieces = (int) sortedArts.stream().filter(a -> !a.isOversized()).count();
        int customPieceCount = (int) sortedArts.stream().filter(Art::isCustom).count();

        int standardBoxCount = (int) boxes.stream().filter(Box::isNormal).count();
        int largeBoxCount = (int) boxes.stream().filter(Box::isOversized).count();

        int standardPalletCount = (int) containers.stream().filter(c -> c.getType() == Container.Type.Pallet).count();
        int oversizedPalletCount = (int) containers.stream().filter(c -> c.getType() == Container.Type.Oversize)
                .count();
        int crateCount = (int) containers.stream().filter(c -> c.getType() == Container.Type.Crate).count();

        oversizeObjects[] oversizedPieces = buildOversizePieces(arts);

        
        List<Art> customArts = new ArrayList();
        List<Art> nonCustomArts = new ArrayList<>();
        int standard_size_pieces_weight = 0;
        int oversized_pieces_weight = 0;

        for (Art art : sortedArts){
            if(art.isCustom()){
                customArts.add(art);
            }
            else {
                nonCustomArts.add(art);
                if(art.isOversized()){
                    oversized_pieces_weight += art.getWeight();
                }
                if(!art.isOversized()){
                    standard_size_pieces_weight += art.getWeight();
                }
            }
        }

        // 5) Build the Response (no summary string here—serializers will handle
        // presentation)
        return new Response(
                sortedArts,
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
                finalShipmentWeight,
                customArts,
                standard_size_pieces_weight,
                oversized_pieces_weight,
                this.currentClient);
    }

    /* ======================== metrics helpers ======================== */

    private static int sumArtworkWeight(List<Art> arts) {
        int sum = 0;
        for (Art a : arts) {
            if (a != null && !a.isCustom())
                sum += a.getWeight();
        }
        return sum;
    }

    /**
     * Groups oversized pieces by (longerSide, shorterSide) and counts quantity.
     * side lengths are rounded up to integers to match the example schema.
     */
    protected static oversizeObjects[] buildOversizePieces(List<Art> arts) {
        Map<String, oversizeObjects> bucket = new LinkedHashMap<>();
        for (Art a : arts) {
            if (!a.isOversized())
                continue;
            int w = (int) Math.ceil(a.getWidth());
            int h = (int) Math.ceil(a.getHeight());
            int s1 = Math.max(w, h);
            int s2 = Math.min(w, h);
            int weight = a.getWeight();
            String key = s1 + "x" + s2;
            oversizeObjects cur = bucket.get(key);
            if (cur == null) {
                bucket.put(key, new oversizeObjects(s1, s2, 1, weight));
            } else {
                bucket.put(key, new oversizeObjects(cur.side1, cur.side2, cur.quantity + 1, weight));
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
    private static List<Container> constructCratesForArtsHypo(List<Art> items, boolean canAcceptCrate) {
        List<Container> crates = new ArrayList<>();
        if (items == null || items.isEmpty())
            return crates;

        // partition
        List<Art> customs = new ArrayList<>();
        List<Art> others = new ArrayList<>();
        for (Art a : items) {
            if (a.getWidth() > 46)
                customs.add(a);
            else
                others.add(a);
        }

        // others.sort(Comparator.comparing(Art::getHeight).thenComparing(Art::getWidth));
        for (Art a : others) {
            Container placedContainer = null;
            for (Container crate : crates) {
                try {
                    if (crate.canArtFit(a)) {
                        crate.addArt(a);
                        placedContainer = crate;
                        break;
                    }
                } catch (Exception ignored) {
                }
            }
            if (placedContainer == null) {
                Container newCrate = Container.constructContainerForArt(a, canAcceptCrate);
                newCrate.addArt(a);
                crates.add(newCrate);
            }
        }
        // throw new IllegalArgumentException("Given Art size: " + others.size()+ " Current2 Crate size: " + crates.size());
        return crates;
    }

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
            if (a.getType() == Art.Type.Mirror){
                mirrors.add(a);
                others.add(a);
            }
            else{
                others.add(a);
            }
        }

        // non-mirrors: greedy largest-first
        // others.sort(Comparator.comparing(Art::getHeight).thenComparing(Art::getWidth));
        others.reversed();
        // *DON'T PACK CUSTOM ARTS !!!
        others.removeIf(Art::isCustom);
        for (Art a : others) {
            Box placedBox = null;
            for (Box b : boxes) {
                if (b.canArtFit(a)) {
                    b.addArt(a);
                    placedBox = b;
                    break;
                }
            }
            if (placedBox == null) {
                // Box b = new Box();
                // b.addArt(a);
                Box b = Box.createBoxForArt(a);
                b.addArt(a);
                boxes.add(b);
            }
        }

        // mirrors: one per box
        // for (Art m : mirrors) {
        //     Box b = new Box();
        //     b.addArt(m);
        //     boxes.add(b);
        // }
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

    /** Mix-pallet-default strategy */
    private static List<Container> constructContainersForBoxesLocalStandardAndOversize(
            List<Box> myBoxes, boolean acceptsPallets, boolean canAcceptCrates) {

        List<Container> result = new ArrayList<>();
        if (myBoxes == null || myBoxes.isEmpty())
            return result;
        if (!acceptsPallets && !canAcceptCrates)
            return result;

        int remainingBoxes = myBoxes.size();

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
                if (box.isOversized()){
                    tryCreateAndPlace(box, Container.Type.Pallet, canAcceptCrates, result);
                }
                else if (remainingBoxes%5 == 0){
                    tryCreateAndPlace(box, Container.Type.Oversize, canAcceptCrates, result);
                }
                else if (remainingBoxes%4 == 0){
                    tryCreateAndPlace(box, Container.Type.Pallet, canAcceptCrates, result);
                }
                else {
                    tryCreateAndPlace(box, Container.Type.Pallet, canAcceptCrates, result);
                }
                // Container.Type preferred = (isMirrorOnly(box) && canAcceptCrates) ? Container.Type.Crate
                //         : acceptsPallets ? Container.Type.Oversize : canAcceptCrates ? Container.Type.Crate : null;
                // if (preferred == null)
                //     continue;

                // if (!tryCreateAndPlace(box, preferred, canAcceptCrates, result)) {
                //     if (preferred == Container.Type.Oversize && canAcceptCrates) {
                //         tryCreateAndPlace(box, Container.Type.Crate, canAcceptCrates, result);
                //     } else if (preferred == Container.Type.Crate && acceptsPallets) {
                //         tryCreateAndPlace(box, Container.Type.Pallet, canAcceptCrates, result);
                //     }
                // }
            }
            remainingBoxes--;
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
