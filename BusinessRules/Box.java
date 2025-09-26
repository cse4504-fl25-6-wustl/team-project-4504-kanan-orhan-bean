package BusinessRules;

import Models.LineItem;
import java.util.List;

/**
 * Box
 *
 * Represents a physical box containing one or more LineItems.
 * 
 * Rules:
 * - Standard Box Height = 31"
 * - Oversized Box Height = 48"
 */
public class Box {
    private List<LineItem> items;
    private double weight;
    private boolean isOversized;
    private double height; // box height in inches

    public Box(List<LineItem> items) {
        this.items = items;
        this.weight = calculateWeight();
        this.isOversized = items.stream().anyMatch(LineItem::isOversized);
        this.height = determineHeight();
    }

    /** Sum weights of all LineItems in this box */
    private double calculateWeight() {
        return items.stream().mapToDouble(LineItem::getWeight).sum();
    }

    /** Determine height based on box type */
    private double determineHeight() {
        return isOversized ? 48.0 : 31.0;
    }

    // === Getters ===
    public List<LineItem> getItems() { return items; }
    public double getWeight() { return weight; }
    public boolean isOversized() { return isOversized; }
    public double getHeight() { return height; }
}
