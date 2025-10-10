package entities;

import java.util.ArrayList;
import java.util.List;
import entities.Art;

public class Container {

    public enum Type { Pallet, Crate, Glass, Oversize, Custom} 
    private Type type;
    private double length;
    private double width;
    private double height;
    private double weight;
    private List<Box> boxes;
    private List<Art> arts;
    private int capacity;
    private boolean isFull;
    private boolean isEmpty;
    private boolean isCarryingOversizeBox;
    private boolean isMirrorCrate;
    private boolean canAcceptCrate;

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
    private final int MIRROR_CRATE_LIMIT = 24;
    private final int NORMAL_GLASS_ACRYLIC_CRATE_LIMIT = 25;
    private final int OVERSIZE_GLASS_ACRYLIC_CRATE_LIMIT = 18;
    private final int NORMAL_CANVAS_CRATE_LIMIT = 18;
    private final int OVERSIZE_CANVAS_CRATE_LIMIT = 12;
    private final int CUSTOM_BOX_DIMENSION_LIMIT = 33;

    // TODO: Figure out where to use these
    private final double OVERSIZE_CRATE_LIMIT = 46;
    private final double CRATE_RECOMMENDED_HEIGHT_LIMIT = 84;
    private final double CRATE_ABSOLUTE_HEIGHT_LIMIT = 102;

    public Container(Type type, boolean canAcceptCrate) {
        super();
        this.type = type;
        this.canAcceptCrate = canAcceptCrate;
        this.arts = new ArrayList<>();
        this.boxes = new ArrayList<>();
    }

    // v--- This is how packing could implement packBoxIntoContainers ---v

    public List<Container> constructContainersForBoxes(List<Box> myBoxes, boolean canAcceptCrates) {
        List<Container> result = new ArrayList<>();
        for (Box box : myBoxes){
            boolean added = false;
            for (Container myContainer : result){
                if (myContainer.canBoxFit(box) && !added){
                    myContainer.addBox(box);
                    added = true;
                }
            }
            if (!added){
                result.add(constructContainerForBox(box));
            }
        }
        return result;
    }

    public List<Container> constructContainersForMirrors(List<Art> myArts, boolean canAcceptCrates) {
        List<Container> result = new ArrayList<>();
        for (Art art : myArts) {
            if (!art.materialContains(Art.Material.Mirror)){
                throw new IllegalArgumentException("One or more Items in arts is NOT a Mirror. All Arts must be Mirror to put directly into a Crate");
            }
            if (result.isEmpty() || result.getLast().isFull()){
                result.add(constructContainerForArt(art));
            }
            else {
                result.getLast().addArt(art);
            }
        }
        return result;
    }

    // ^--- This is how packing could implement packBoxIntoContainers ---^

    public Container constructContainerForArt(Art art) {
        if (art.getType() == Art.Type.Mirror){
            Container myContainer = new Container(Type.Crate, canAcceptCrate);
            myContainer.setMirrorCrate(true);
            myContainer.addArt(art);
            return myContainer;
        }
        else {
            throw new IllegalArgumentException("You can only construct a direct Container for Mirrors, for all other art use Boxes");
        }
    }

    public Container constructContainerForBox(Box box) {
        if (box.isCustom() || box.getWidth() > OVERSIZE_CRATE_LIMIT || box.getLength() > OVERSIZE_CRATE_LIMIT 
        || box.getHeight() > OVERSIZE_CRATE_LIMIT){
            Container myContainer = new Container(Type.Custom, canAcceptCrate);
            myContainer.setMirrorCrate(false);
            myContainer.addBox(box);
            return myContainer;
        }
        else if (this.canAcceptCrate() && !this.isSmallEnoughForGlassPallet(box)){
            Container myContainer = new Container(Type.Crate, canAcceptCrate);
            myContainer.setMirrorCrate(false);
            myContainer.addBox(box);
            myContainer.setCrateNormal();
            this.setContainerHeight(box.getHeight() + CRATE_HEIGHT_OVERHEAD);
            return myContainer;
        }
        else if (this.isSmallEnoughForGlassPallet(box)) {
            Container myContainer = new Container(Type.Glass, canAcceptCrate);
            myContainer.setMirrorCrate(false);
            myContainer.addBox(box);
            myContainer.setPalletGlass();
            this.setContainerHeight(box.getHeight());
            return myContainer;
        }
        else if (box.isOversized()){
            Container myContainer = new Container(Type.Oversize, canAcceptCrate);
            myContainer.setMirrorCrate(false);
            myContainer.addBox(box);
            myContainer.setPalletOversize();
            this.setContainerHeight(box.getHeight());
            return myContainer;
        }
        else {
            Container myContainer = new Container(Type.Pallet, canAcceptCrate);
            myContainer.addBox(box);
            myContainer.setMirrorCrate(false);
            myContainer.setPalletNormal();
            this.setContainerHeight(box.getHeight());
            return myContainer;
        }
    }

    public List<Box> addBox(Box box){
        if (this.isMirrorCrate()){
            throw new IllegalStateException("Can not add a Box to a Mirror Crate.");
        }
        this.boxes.add(box);
        if (this.getType() == Type.Crate){
            this.setContainerHeight(Math.max(this.getHeight(), box.getHeight() + CRATE_HEIGHT_OVERHEAD));
        }
        else {
            this.setContainerHeight(Math.max(this.getHeight(), box.getHeight()));
        }
        this.updateFullness();
        return this.boxes;
    };

    public List<Art> addArt(Art art){
        if (!this.isMirrorCrate()){
            throw new IllegalStateException("Can not add Arts in a Non-Mirror Crate.");
        }
        this.arts.add(art);
        this.updateFullness();
        return this.arts;
    };

    public boolean canBoxFit(Box box){

        if (this.isMirrorCrate()){
            throw new IllegalStateException("Can not check Boxes in a Mirror Crate.");
        }

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
            return true;
        }
        // Crate has something in it
        else if (this.getType() == Type.Glass && this.isSmallEnoughForGlassPallet(box)){
            // It is a non-full Glass Crate, and the Box is small enough to fit in it
            return true;
        }
        else if (this.getType() == Type.Custom){
            // Custom Crate, It is for something that is custom, can't put stuff in it
            return false;
        }
        // If Container is Normal Pallet and box is oversized 
        else if ((!this.isCarryingOversizeBox()) && isBoxOversized){
            return false;
        }
        // Assuming we sorted the Box by size before checking, all other cases are Container is Oversized, Box isn't 
        // or that they are both Normal or Both Oversized. So in all other cases Box can fit
        return true;
    }

    public boolean canArtFit(Art art){
        if (!this.isMirrorCrate()){
            throw new IllegalStateException("Can not check Arts in a Non-Mirror Crate.");
        }
        if (art.getType() != Art.Type.Mirror){
            throw new IllegalStateException("Can not put any Art other than Mirror in a Mirror Crate.");
        }
        // Crate is Full, can't fit Box
        if (this.isFull()){
            return false;
        }
        else {
            // Can put Mirrors into the box
            return true;
        }
    }

    // -------- getters and setters --------

    public boolean isFull(){
        return this.isFull;
    }

    public boolean isEmpty(){
        return this.isEmpty;
    }

    public boolean isMirrorCrate(){
        return this.isMirrorCrate;
    }

    public int getCurrentSize(){
        return (isMirrorCrate()) ? this.arts.size() : this.boxes.size();
    }

    public Type getType(){
        return this.type;
    }

    public boolean canAcceptCrate(){
        return this.canAcceptCrate;
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
        if (this.isMirrorCrate()){
            throw new IllegalStateException("Can not access Boxes in a Mirror Crate.");
        }
        return this.boxes;
    };

    public List<Art> getArts(){
        if (!this.isMirrorCrate()){
            throw new IllegalStateException("Can not access Arts in a Non-Mirror Crate.");
        }
        return this.arts;
    };

    public double getWeight(){
        double weight = 0.0;
        if (this.isMirrorCrate()){
            for (Art art : arts){
                weight +=art.getWeight();
            }
        }
        else {
            for (Box box : boxes) {
                weight += box.getWeight();
            }
        }
        this.setWeight(weight + getTareWeight());
        return this.weight;
    }

    public int getCapacity(){
        // return its capacity if it can be calculated, else return an IllegalStateException 
        if (!this.setCapacity()) {
            throw new IllegalStateException("Capacity cannot be determined for this Container as it is empty.");
        }
        return this.capacity;
    }

    // ---------------- helpers ----------------

    private boolean setMirrorCrate(boolean bool){
        this.isMirrorCrate = bool;
        return this.isMirrorCrate;
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

    private void setWeight(double weight){
        this.weight = weight;
    }
    
    private boolean isCarryingOversizeBox(){
        if (this.isMirrorCrate()){
            throw new IllegalStateException("There are no Boxes in a Mirror Crate.");
        }
        return this.isCarryingOversizeBox;
    }

    private double getTareWeight(){
        if (this.getType() == Type.Crate){
            return OVERHEAD_CRATE_WEIGHT;
        }
        else if (this.getType() == Type.Oversize){
            return OVERHEAD_OVERSIZE_PALLET_WEIGHT;
        }
        return OVERHEAD_PALLET_WEIGHT;
    }

    private void updateFullness(){
        if (!this.setCapacity()){
            this.isEmpty = true;
            this.isFull = false;
        }
        else if (this.isMirrorCrate()){
            if (this.arts.size() >= this.getCapacity()){
                this.isEmpty = false;
                this.isFull = true;
            }
            else {
                this.isEmpty = true;
                this.isFull = false;
            }
        }
        else {
            if (this.boxes.size() >= this.getCapacity()){
                this.isEmpty = false;
                this.isFull  = true;
            }
            else {
                this.isEmpty = true;
                this.isFull = false;
            }
        }
        this.isEmpty = false;
        this.isFull = true;
    }

    private boolean isSmallEnoughForGlassPallet(Box box){
        return (box.getLength() > GLASS_PALLET_LENGTH || box.getWidth() > GLASS_PALLET_WIDTH) ? false : true;
    }

    private boolean setCapacity(){
        
        if (this.isEmpty()){
            // Return a void value if the box is empty. Because if the box is empty we don't know the items in it 
            // which means we don't know its capacity
            return false;
        }

        Box containerFirstBox = this.getBoxes().get(0);
        Type containerType = this.getType();
        boolean boxOversized = containerFirstBox.isOversized();

        // Assuming the Container only has Box that is of same or smaller type than first
        
        if (containerType != Type.Custom && !this.isMirrorCrate()){
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
        else if (this.isMirrorCrate()){
            this.capacity = MIRROR_CRATE_LIMIT;
            return true;
        }
        else {
            // TODO: Is this considered a Custom Box? Does it need human evaluation?
            boolean isDimensionsOversize = false;
            for (Box box : this.getBoxes()){
                if (box.isCustom() && (box.getHeight() > CUSTOM_BOX_DIMENSION_LIMIT || box.getWidth() > CUSTOM_BOX_DIMENSION_LIMIT)){
                    isDimensionsOversize = true;
                }
            }
            if (containerFirstBox.getArts().get(0).materialContains(Art.Material.Glass)
            || containerFirstBox.getArts().get(0).materialContains(Art.Material.Acyrlic)){
                if (isDimensionsOversize){
                    this.capacity = OVERSIZE_GLASS_ACRYLIC_CRATE_LIMIT;
                }
                else {
                    this.capacity = NORMAL_GLASS_ACRYLIC_CRATE_LIMIT;
                }
            }
            else if (containerFirstBox.getArts().get(0).materialContains(Art.Material.CanvasFramed) 
            || containerFirstBox.getArts().get(0).materialContains(Art.Material.CanvasGallery)){
                if (isDimensionsOversize){
                    this.capacity = OVERSIZE_CANVAS_CRATE_LIMIT;
                }
                else {
                    this.capacity = NORMAL_CANVAS_CRATE_LIMIT;
                }
            }
            return true;
        }
    }

    // private List<Box> removeBox(Box box){
    //     if (this.isMirrorCrate()){
    //         throw new IllegalStateException("Can not remove a Box from a Mirror Crate.");
    //     }
    //     this.boxes.remove(box);
    //     this.updateFullness();
    //     return this.boxes;
    // };

    // private List<Art> removeArt(Art art){
    //     if (!this.isMirrorCrate()){
    //         throw new IllegalStateException("Can not remove Arts from a Non-Mirror Crate.");
    //     }
    //     this.arts.remove(art);
    //     this.updateFullness();
    //     return this.arts;
    // };
}
