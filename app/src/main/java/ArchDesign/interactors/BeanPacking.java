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
import ArchDesign.interactors.Packing.oversizeObjects;
import ArchDesign.entities.Client.DeliveryCapabilities;
import ArchDesign.entities.Client;
import ArchDesign.requests.Request;
import ArchDesign.responses.Response;


public class BeanPacking {

// * CHANGED!!!
    public static Response packEverything(Request request) {
        Objects.requireNonNull(request, "request must not be null");
        final Client currentClient = Objects.requireNonNull(request.getClient(), "client must not be null");
        final List<Art> arts = Objects.requireNonNull(request.getArts(), "arts must not be null");

        DeliveryCapabilities caps = currentClient.getDeliveryCapabilities();
        List<Box> boxes = null;

        // if they DO accept crates, will use crate boxes directly NOT standard/oversized boxes !!
        if (!caps.doesAcceptCrates()) {
            if (caps.doesAcceptPallets()) {
                boxes = constructBoxesForArts(arts);
            }
            // can't currently handle cases where neither crates nor pallets are accepted
        }
        else {
            // TODO: implement logic for filling crate boxes
        }
        int standardBoxCount = (int) boxes.stream().filter(Box::isNormal).count();

        // NOTHING FROM THIS POINT ON IN PACK_EVERYTHING WAS CHANGED
        List<Container> containersPallet = constructContainersForBoxesLocal(boxes, caps.doesAcceptPallets(), caps.doesAcceptCrates());
        List<Container> containersOversize = constructContainersForBoxesLocalOversize(boxes, caps.doesAcceptPallets(), caps.doesAcceptCrates());
        List<Container> containers = (containersOversize.size() < containersPallet.size()) ? containersOversize : containersPallet;

        // 3) Weights
        int totalArtworkWeight = sumArtworkWeight(arts);
        int finalShipmentWeight = computeTotalWeight(arts, boxes, containers);
        int totalPackagingWeight = Math.max(0, finalShipmentWeight - totalArtworkWeight);

        // 4) Counts & metrics for schema
        int totalPieces = arts.size();
        int standardSizePieces = (int) arts.stream().filter(a -> !a.isOversized()).count();
        int customPieceCount = (int) arts.stream().filter(Art::isCustom).count();

        int largeBoxCount = (int) boxes.stream().filter(Box::isOversized).count();

        int standardPalletCount = (int) containers.stream().filter(c -> c.getType() == Container.Type.Pallet).count();
        int oversizedPalletCount = (int) containers.stream().filter(c -> c.getType() == Container.Type.Oversize).count();
        int crateCount = (int) containers.stream().filter(c -> c.getType() == Container.Type.Crate).count();

        oversizeObjects[] oversizedPieces = buildOversizePieces(arts);

        // 5) Build the Response (no summary string here—serializers will handle
        // presentation)
        return new Response(arts, boxes, containers, totalPieces, standardSizePieces, oversizedPieces,
                standardBoxCount, largeBoxCount, customPieceCount, standardPalletCount,oversizedPalletCount,
                crateCount, totalArtworkWeight, totalPackagingWeight, finalShipmentWeight);
    }

// * CHANGED!!!
    private static List<Box> constructBoxesForArts(List<Art> items) {
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
        others.sort(Comparator.comparingDouble(Art::getWidth).reversed());
        // *DON'T PACK CUSTOM ARTS !!!
        others.removeIf(Art::isCustom);
        boxes.add(Box.createBoxForArt(others.get(0)));
        int boxIndex = 0;

        for (Art a : others) {
            if (!boxes.get(boxIndex).addArt(a)) {
                boxes.add(Box.createBoxForArt(a));
                ++boxIndex;
                boxes.get(boxIndex).addArt(a);
            }
        }

        // TODO: figure out how to handle mirrors if crates not allowed (typically goes straight into crate box?)
        return boxes;
    }

// * EVERYTHING BELOW IS UNCHANGED    
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

    private static int computeTotalWeight(List<Art> arts, List<Box> boxes, List<Container> containers) {
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
