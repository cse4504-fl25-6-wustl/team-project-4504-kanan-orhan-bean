package responses;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringJoiner;

import entities.Art;
import entities.Box;
import entities.Container;

public class Response {

    private final List<Art> arts;
    private final List<Box> boxes;
    private final List<Container> containers;
    private final double totalWeight;

    /**
     * @param arts       items to be shipped (may be empty, not null)
     * @param boxes      boxes produced by packing (may be empty, not null)
     * @param containers containers (pallets/crates) produced by packing (may be
     *                   empty, not null)
     */
    public Response(List<Art> arts, List<Box> boxes, List<Container> containers) {
        // a. Weight of each individual item
        // b. How items are packed in boxes (which items are packed together)
        // c. The weight of each box
        // d. How boxes are packed in crates or pallets (which boxes are packed together
        // and are they on a pallet or a crate)
        // e. The weight and height of each pallet/crate.
        // f. Total weight of the shipment

        this.arts = Collections
                .unmodifiableList(new ArrayList<>(Objects.requireNonNull(arts, "arts must not be null")));
        this.boxes = Collections
                .unmodifiableList(new ArrayList<>(Objects.requireNonNull(boxes, "boxes must not be null")));
        this.containers = Collections
                .unmodifiableList(new ArrayList<>(Objects.requireNonNull(containers, "containers must not be null")));

        this.totalWeight = calculateTotalWeight();
    }

    public List<Art> getArts() {
        return arts;
    }

    public List<Box> getBoxes() {
        return boxes;
    }

    public List<Container> getContainers() {
        return containers;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    /**
     * Shipment weight strategy:
     * 1) If containers exist, sum container.getWeight() (includes boxes + tare).
     * 2) Else if boxes exist, sum box.getWeight().
     * 3) Else sum all art.getWeight().
     */
    private double calculateTotalWeight() {
        try {
            if (!containers.isEmpty()) {
                double sum = 0.0;
                for (Container c : containers) {
                    sum += safeContainerWeight(c);
                }
                if (sum > 0)
                    return sum;
            }
            if (!boxes.isEmpty()) {
                double sum = 0.0;
                for (Box b : boxes) {
                    sum += safeBoxWeight(b);
                }
                if (sum > 0)
                    return sum;
            }
        } catch (Exception ignored) {
            // fall through to art-only sum
        }
        double sumArts = 0.0;
        for (Art a : arts) {
            sumArts += (a != null ? a.getWeight() : 0.0);
        }
        return sumArts;
    }

    private double safeBoxWeight(Box b) {
        try {
            return (b != null) ? b.getWeight() : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    private double safeContainerWeight(Container c) {
        try {
            return (c != null) ? c.getWeight() : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    @Override
    public String toString() {
        String nl = System.lineSeparator();
        StringJoiner sj = new StringJoiner(nl);

        // Header
        sj.add("=== PACKING SUMMARY ===");

        // Items
        sj.add(String.format("Items: %d total", arts.size()));
        // Show a brief per-item weight list (truncate if large)
        int maxItemsToShow = Math.min(arts.size(), 10);
        for (int i = 0; i < maxItemsToShow; i++) {
            Art a = arts.get(i);
            sj.add(String.format("  - Art #%d (line %d): %.2f\" x %.2f\" %s | weight=%.2f lb%s",
                    i + 1,
                    a.getLineNumber(),
                    a.getWidth(),
                    a.getHeight(),
                    a.getType(),
                    a.getWeight(),
                    a.isCustom() ? " | CUSTOM" : ""));
        }
        if (arts.size() > maxItemsToShow) {
            sj.add(String.format("  ... and %d more items", arts.size() - maxItemsToShow));
        }

        // Boxes
        sj.add(String.format("Boxes: %d", boxes.size()));
        for (int i = 0; i < boxes.size(); i++) {
            Box b = boxes.get(i);
            double bw = safeBoxWeight(b);
            String sizeTag = b.isOversized() ? "OVERSIZE" : (b.isCustom() ? "CUSTOM" : "STANDARD");
            sj.add(String.format("  - Box #%d: %s | %.0f\" L x %.0f\" W x %.0f\" H | weight=%.2f lb",
                    i + 1, sizeTag, b.getLength(), b.getWidth(), b.getHeight(), bw));
        }

        // Containers
        sj.add(String.format("Containers: %d", containers.size()));
        for (int i = 0; i < containers.size(); i++) {
            Container c = containers.get(i);
            double cw = safeContainerWeight(c);
            sj.add(String.format("  - Container #%d: %s | %.0f\" L x %.0f\" W x %.0f\" H | weight=%.2f lb",
                    i + 1, c.getType(), c.getLength(), c.getWidth(), c.getHeight(), cw));
        }

        // Totals
        sj.add(String.format("TOTAL SHIPMENT WEIGHT: %.2f lb", totalWeight));
        sj.add("========================");
        return sj.toString();
    }
}
