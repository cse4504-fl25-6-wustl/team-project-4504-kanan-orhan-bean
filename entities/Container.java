package entities;

import java.util.List;

public class Container {

    // Create a Pallet Class

    public enum Type { Pallet, Crate, Glass, Oversize, Custom} 
    private Type type;
    private double length;
    private double width;
    private double height;
    private double weight;
    private List<Box> boxes;
    private int capacity;
    private boolean isFull;
    private boolean isCarryingOversizeBox;

    private final double STANDARD_PALLET_LENGTH = 48;
    private final double STANDARD_PALLET_WIDTH = 40;
    private final double GLASS_PALLET_LENGTH = 43;
    private final double GLASS_PALLET_WIDTH = 35;
    private final double OVERSIZE_PALLET_LENGTH = 60;
    private final double OVERSIZE_PALLET_WIDTH = 40;
    private final double CRATE_LENGTH = 50;
    private final double CRATE_WIDTH = 38;
    private final double CRATE_HEIGHT_OVERHEAD = 8;
    private final double OVERHEAD_PALLET_WEIGHT = 60;
    private final double OVERHEAD_OVERSIZE_PALLET_WEIGHT = 75;
    private final double OVERHEAD_CRATE_WEIGHT = 125;
    private final boolean canAcceptCrate;

    // TODO: Figure out where to use these
    private final double OVERSIZE_CRATE_LIMIT = 46;
    private final double CRATE_RECOMMENDED_HEIGHT_LIMIT = 84;
    private final double CRATE_ABSOLUTE_HEIGHT_LIMIT = 102;

    public Container(Type type, boolean canAcceptCrate) {
        super();
        this.type = type;
        this.canAcceptCrate = canAcceptCrate;
    }

    public boolean isFull(){
        return this.isFull;
    }

    public boolean isEmpty(){
        return this.boxes.isEmpty();
    }

    private boolean isCarryingOversizeBox(){
        return this.isCarryingOversizeBox;
    }

    public Type getType(){
        return this.type;
    }

    public boolean canAcceptCrate(){
        return this.canAcceptCrate;
    }

    private void setPalletNormal(){
        this.length = STANDARD_PALLET_LENGTH;
        this.width = STANDARD_PALLET_WIDTH;
    }

    private void setPalletGlass(){
        this.length = GLASS_PALLET_LENGTH;
        this.width = GLASS_PALLET_WIDTH;
    }

    private void setPalletOversize(){
        this.length = OVERSIZE_PALLET_LENGTH;
        this.width = OVERSIZE_PALLET_WIDTH;
    }

    private void setCrateNormal(){
        this.length = CRATE_LENGTH;
        this.width = CRATE_WIDTH;
    }

    private void setContainerHeight(double height){
        this.height = height;
    }

    public double getLength(){
        return this.length;
    };

    public double getWidth(){
        return this.width;
    };

    public double getHeight(){
        return this.height;
    }

    public List<Box> getBoxes(){
        return this.boxes;
    };

    public List<Box> addBox(Box box){
        this.boxes.add(box);
        if (canAcceptCrate()){
            this.setCrateNormal();
        }
        else if (box.isOversized()){
            this.setPalletOversize();
        }
        else {
            this.setPalletNormal();
        }
        // TODO: When do we use Glass Pallets?
        return this.boxes;
    };

    public List<Box> removeBox(Box box){
        this.boxes.remove(box);
        return this.boxes;
    };

    public boolean canBoxFit(Box box){
        // TODO: Figure out how to Handle Art directly into Containers

        boolean isBoxOversized = box.isOversized();

        // Crate is Full, can't fit Box
        if (this.isFull()){
            return false;
        }
        // Crate is empty
        else if (this.isEmpty()){
            // Box is oversize, Crate needs to be Oversize
            if (isBoxOversized) {
                this.isCarryingOversizeBox = true;
                return true;
            }
            // Box isn't oversize, Crate can be Standard
            this.isCarryingOversizeBox = false;
            this.setContainerHeight(box.getHeight() + CRATE_HEIGHT_OVERHEAD);
            return true;
        }
        // If Container isn't oversized and art is oversized 
        else if ((!this.isCarryingOversizeBox()) && isBoxOversized){
            return false;
        }
        // Assuming we sorted the Box by size before checking, all other cases are Container is Oversized, Box isn't 
        // or that they are both the same size. So in all other cases Box can fit
        return true;
    }

    public double getWeight(){
        double weight = 0.0;
        for (Box box : boxes) {
            weight += box.getWeight();
        }
        this.setWeight(weight + getTareWeight());
        return this.weight;
    }

    private double getTareWeight(){
        Type containerType = this.getType();
        if (containerType == Type.Crate){
            return OVERHEAD_CRATE_WEIGHT;
        }
        else if (containerType == Type.Oversize){
            return OVERHEAD_OVERSIZE_PALLET_WEIGHT;
        }
        return OVERHEAD_PALLET_WEIGHT;
    }

    private void setWeight(double weight){
        this.weight = weight;
    }

    public int getCapacity(){
        // return its capacity if it can be calculated, else return -1
        return setCapacity() ? this.capacity : -1;
    }

    private boolean setCapacity(){
        
        if (this.isEmpty()){
            // Return a void value if the box is empty. Because if the box is empty we don't know the items in it 
            // which means we don't know its capacity
            return false;
        }

        Box containerFirstBox = boxes.getFirst();
        Type containerType = this.getType();
        boolean boxOversized = containerFirstBox.isOversized();
        // Assuming the Container only has Box that is of same or smaller type than first
        
        if (containerType != Type.Custom){
            int containerInitialCapacity = 0;
            if (containerType == Type.Pallet || containerType == Type.Crate){
                containerInitialCapacity = 4;
            }
            else if (containerType == Type.Oversize){
                containerInitialCapacity = 5;
            }
            if (boxOversized){
                containerInitialCapacity--;
            }

            this.capacity = containerInitialCapacity;
            return true;
        }
        else {
            //TODO Figure out how to pack Arts directly into Containers
            return false;
        }

    }
}
