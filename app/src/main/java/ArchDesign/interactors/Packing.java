package ArchDesign.interactors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import ArchDesign.entities.Art;
import ArchDesign.entities.Box;
import ArchDesign.entities.Container;
import ArchDesign.entities.Client;
import ArchDesign.requests.Request;
import ArchDesign.responses.Response;

public class Packing {

    private Client currentClient;

    public Response packEverything(Request request) {
        Objects.requireNonNull(request, "request must not be null");
        this.currentClient = Objects.requireNonNull(request.getClient(), "client must not be null");

        final List<Art> arts = Objects.requireNonNull(request.getArts(), "arts must not be null");

        List<Box> boxes = constructBoxesForArtsHypo(arts);

        var caps = currentClient.getDeliveryCapabilities();
        boolean acceptsPallets = caps.doesAcceptPallets();
        boolean acceptsCrates = caps.doesAcceptCrates();
        List<Container> containersOversize = constructContainersForBoxesLocalOversize(boxes, acceptsPallets, acceptsCrates);
        List<Container> containersPallet = constructContainersForBoxesLocal(boxes, acceptsPallets, acceptsCrates);
        List<Container> containers;
        if (containersOversize.size() < containersPallet.size()){
            containers = containersOversize;
        }
        else {
            containers = containersPallet;
        }

        int totalWeight = computeTotalWeight(arts, boxes, containers);
        String summary = buildSummary(arts, boxes, containers, totalWeight);

        return new Response(arts, boxes, containers, totalWeight, summary);
    }

    /* ======================== totals and summary ======================== */

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
        int sumArts = 0;
        for (Art a : arts)
            if (a != null)
                sumArts += a.getWeight();
        return sumArts;
    }

    private String buildSummary(List<Art> arts, List<Box> boxes, List<Container> containers, double totalWeight) {
        String nl = System.lineSeparator();
        StringBuilder sb = new StringBuilder();
        sb.append("=== PACKING SUMMARY ===").append(nl);

        // Items
        sb.append(String.format("Items: %d total", arts.size())).append(nl);
        int maxItemsToShow = Math.min(arts.size(), 10);
        for (int i = 0; i < maxItemsToShow; i++) {
            Art a = arts.get(i);
            sb.append(String.format(
                    "  - Art #%d (line %d): %.2f\" x %.2f\" %s | weight=%d lb%s",
                    i + 1, a.getLineNumber(), a.getWidth(), a.getHeight(),
                    a.getType(), a.getWeight(), a.isCustom() ? " | CUSTOM" : "")).append(nl);
        }
        if (arts.size() > maxItemsToShow) {
            sb.append(String.format("  ... and %d more items", arts.size() - maxItemsToShow)).append(nl);
        }

        // Boxes
        sb.append(String.format("Boxes: %d", boxes.size())).append(nl);
        for (int i = 0; i < boxes.size(); i++) {
            Box b = boxes.get(i);
            double bw = safeBoxWeight(b);
            String sizeTag = b.isOversized() ? "OVERSIZE" : (b.isCustom() ? "CUSTOM" : "STANDARD");
            sb.append(String.format(
                    "  - Box #%d: %s | %.0f\" L x %.0f\" W x %.0f\" H | weight=%.2f lb",
                    i + 1, sizeTag, b.getLength(), b.getWidth(), b.getHeight(), bw)).append(nl);
        }

        // Containers
        sb.append(String.format("Containers: %d", containers.size())).append(nl);
        for (int i = 0; i < containers.size(); i++) {
            Container c = containers.get(i);
            double cw = safeContainerWeight(c);
            sb.append(String.format(
                    "  - Container #%d: %s | %.0f\" L x %.0f\" W x %.0f\" H | weight=%.2f lb",
                    i + 1, c.getType(), c.getLength(), c.getWidth(), c.getHeight(), cw)).append(nl);
        }

        sb.append(String.format("TOTAL SHIPMENT WEIGHT: %.2f lb", totalWeight)).append(nl);
        sb.append("========================");
        return sb.toString();
    }

    /* ===================== local factory helpers ===================== */

    /**
     * Build boxes for all arts. Mirrors are modeled as hypothetical boxes:
     * one mirror per box; no mixing mirrors with other art inside a box.
     * Non-mirrors are packed greedily (largest side first) using Box.canArtFit().
     */
    private static List<Box> constructBoxesForArtsHypo(List<Art> items) {
        List<Box> boxes = new ArrayList<>();
        if (items == null || items.isEmpty())
            return boxes;

        // Partition
        List<Art> mirrors = new ArrayList<>();
        List<Art> others = new ArrayList<>();
        for (Art a : items) {
            if (a.getType() == Art.Type.Mirror)
                mirrors.add(a);
            else
                others.add(a);
        }

        // 1) Non-mirrors: greedy largest-first
        others.sort(Comparator.comparingDouble(a -> -Math.max(a.getWidth(), a.getHeight())));
        for (Art a : others) {
            Box placed = null;
            for (Box b : boxes) {
                try {
                    if (b.canArtFit(a)) {
                        b.addArt(a);
                        placed = b;
                        break;
                    }
                } catch (Exception ignored) {
                    /* try next box */ }
            }
            if (placed == null) {
                Box b = new Box();
                b.addArt(a);
                boxes.add(b);
            }
        }

        // 2) Mirrors: one per box
        for (Art m : mirrors) {
            Box b = new Box();
            b.addArt(m);
            boxes.add(b);
        }

        return boxes;
    }

    /**
     * Containerize boxes. If a box is mirror-only and site allows crates,
     * start a Crate for it; otherwise prefer Pallet (if site allows).
     * All subsequent placement uses Container.canBoxFit(...) + addBox(...).
     *
     * **Bug fix**: only add a newly created container to the result
     * if we successfully place the box into it.
     */
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

            // Try to place into any existing container first
            for (Container cont : result) {
                try {
                    if (cont.canBoxFit(box)) {
                        cont.addBox(box);
                        added = true;
                        break;
                    }
                } catch (Exception ignored) {
                    /* try next container */
                }
            }

            if (!added) {
                // Choose preferred fresh type for this box
                Container.Type preferred;
                if (isMirrorOnly(box) && canAcceptCrates) {
                    preferred = Container.Type.Crate;
                } else if (acceptsPallets) {
                    preferred = Container.Type.Pallet;
                } else if (canAcceptCrates) {
                    preferred = Container.Type.Crate;
                } else {
                    // Nowhere to place
                    continue;
                }

                // Try preferred type
                if (tryCreateAndPlace(box, preferred, canAcceptCrates, result)) {
                    added = true;
                } else {
                    // Fallback to the other allowed type if available
                    if (preferred == Container.Type.Pallet && canAcceptCrates) {
                        added = tryCreateAndPlace(box, Container.Type.Crate, canAcceptCrates, result);
                    } else if (preferred == Container.Type.Crate && acceptsPallets) {
                        added = tryCreateAndPlace(box, Container.Type.Pallet, canAcceptCrates, result);
                    }
                }

                // If still not added, we skip; DO NOT append empty containers.
            }
        }
        return result;
    }

    /**
     * Containerize boxes. If a box is mirror-only and site allows crates,
     * start a Crate for it; otherwise prefer Pallet (if site allows).
     * All subsequent placement uses Container.canBoxFit(...) + addBox(...).
     *
     * **Bug fix**: only add a newly created container to the result
     * if we successfully place the box into it.
     */
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

            // Try to place into any existing container first
            for (Container cont : result) {
                try {
                    if (cont.canBoxFit(box)) {
                        cont.addBox(box);
                        added = true;
                        break;
                    }
                } catch (Exception ignored) {
                    /* try next container */
                }
            }

            if (!added) {
                // Choose preferred fresh type for this box
                Container.Type preferred;
                if (isMirrorOnly(box) && canAcceptCrates) {
                    preferred = Container.Type.Crate;
                } else if (acceptsPallets) {
                    preferred = Container.Type.Oversize;
                } else if (canAcceptCrates) {
                    preferred = Container.Type.Crate;
                } else {
                    // Nowhere to place
                    continue;
                }

                // Try preferred type
                if (tryCreateAndPlace(box, preferred, canAcceptCrates, result)) {
                    added = true;
                } else {
                    // Fallback to the other allowed type if available
                    if (preferred == Container.Type.Pallet && canAcceptCrates) {
                        added = tryCreateAndPlace(box, Container.Type.Crate, canAcceptCrates, result);
                    } else if (preferred == Container.Type.Crate && acceptsPallets) {
                        added = tryCreateAndPlace(box, Container.Type.Pallet, canAcceptCrates, result);
                    }
                }

                // If still not added, we skip; DO NOT append empty containers.
            }
        }
        return result;
    }

    // Create a container of 'type', attempt to add the box, and only append to
    // 'out'
    // if the placement succeeds.
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
            // fall through -> return false
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

    /* ============================== helpers ============================== */

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
