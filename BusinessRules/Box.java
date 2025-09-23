package BusinessRules;

import Models.LineItem;
import java.util.List;

/**
 * Box
 *
 * Represents a physical box containing one or more LineItems.
 */
public class Box {
    private List<LineItem> items;
    private double weight;
    private boolean isOversized;

    public Box(List<LineItem> items) {
        this.items = items;
        this.weight = calculateWeight();
        this.isOversized = items.stream().anyMatch(LineItem::isOversized);
    }

    private double calculateWeight() {
        return items.stream().mapToDouble(LineItem::getWeight).sum();
    }

    // === Getters ===
    public List<LineItem> getItems() { return items; }
    public double getWeight() { return weight; }
    public boolean isOversized() { return isOversized; }
}
