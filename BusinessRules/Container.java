package BusinessRules;

import java.util.List;

/**
 * Container
 *
 * Represents a shipping container, either a pallet or a crate.
 */
public class Container {
    public enum Type { PALLET, CRATE }

    private Type type;
    private List<Box> boxes;
    private double weight;
    private double height;

    public Container(Type type, List<Box> boxes, double heightBuffer) {
        this.type = type;
        this.boxes = boxes;
        this.height = calculateHeight(heightBuffer);
        this.weight = calculateWeight();
    }

    private double calculateWeight() {
        double boxWeight = boxes.stream().mapToDouble(Box::getWeight).sum();
        double tare = (type == Type.PALLET) ? 60 : 75; // default, can customize
        return boxWeight + tare;
    }

    private double calculateHeight(double heightBuffer) {
        double maxBoxHeight = boxes.stream()
                .flatMap(b -> b.getItems().stream())
                .mapToDouble(i -> i.getHeight())
                .max().orElse(0);
        return maxBoxHeight + heightBuffer;
    }

    // === Getters ===
    public Type getType() { return type; }
    public List<Box> getBoxes() { return boxes; }
    public double getWeight() { return weight; }
    public double getHeight() { return height; }
}
