package ArchDesign.entities;

import java.util.ArrayList;
import java.util.List;
import ArchDesign.entities.Art;

public class Container {

    public enum Type {
        Pallet, Crate, Glass, Oversize, Custom,
    }

    private Type type;
    private double length;
    private double width;
    private double height;
    private int weight;
    private List<Box> boxes;
    private List<Art> arts;
    private int capacity;
    private boolean isFull;
    private boolean isEmpty;
    private boolean isCarryingOversizeBox;
    private boolean isMirrorCrate;
    private boolean isArtCrate;
    private boolean canAcceptCrate;
    private Art.Material crateMedium; 
    private boolean isShortCrate;

    private final double STANDARD_PALLET_LENGTH = 48;
    private final double STANDARD_PALLET_WIDTH = 40;
    private final double GLASS_PALLET_LENGTH = 43;
    private final double GLASS_PALLET_WIDTH = 35;
    private final double OVERSIZE_PALLET_LENGTH = 60;
    private final double OVERSIZE_PALLET_WIDTH = 40;
    private final double CRATE_LENGTH = 50;
    private final double CRATE_WIDTH = 38;
    private final double CRATE_HEIGHT_OVERHEAD = 8;
    private final int OVERHEAD_PALLET_WEIGHT = 60;
    private final int OVERHEAD_OVERSIZE_PALLET_WEIGHT = 75;
    private final int OVERHEAD_CRATE_WEIGHT = 125;
    private final int MIRROR_CRATE_LIMIT = 24;
    private final int NORMAL_GLASS_ACRYLIC_MIRROR_CRATE_LIMIT = 25;
    private final int OVERSIZE_GLASS_ACRYLIC_MIRROR_CRATE_LIMIT = 19;
    private final int NORMAL_CANVAS_ACOUSTIC_CRATE_LIMIT = 18;
    private final int OVERSIZE_CANVAS_ACOUSTIC_CRATE_LIMIT = 14;
    private final int CUSTOM_BOX_DIMENSION_LIMIT = 33;
    private final int GLASS_PALLET_WIDTH_THRESHOLD = 35;
    private final int GLASS_PALLET_LENGTH_THRESHOLD = 35;

    // TODO: Figure out where to use these
    private final double OVERSIZE_CRATE_LIMIT = 46;
    private final double CRATE_RECOMMENDED_HEIGHT_LIMIT = 84;
    private final double CRATE_ABSOLUTE_HEIGHT_LIMIT = 102;

    public Container(Type type, boolean canAcceptCrate) {
        super();
        this.type = type;
        this.canAcceptCrate = canAcceptCrate;
        if (!canAcceptCrate() && getType() == Type.Crate){
            throw new IllegalArgumentException("Can not set Container to Crate if Customer can't accept Crates");
        }
        this.arts = new ArrayList<>();
        this.boxes = new ArrayList<>();
        this.isEmpty = true;
        this.isFull = false;
        this.setPalletDimensions();
    }

    // v--- This is how packing could implement packBoxIntoContainers ---v

    // public List<Container> constructContainersForBoxes(List<Box> myBoxes, boolean
    // canAcceptCrates) {
    // List<Container> result = new ArrayList<>();
    // for (Box box : myBoxes){
    // boolean added = false;
    // for (Container myContainer : result){
    // if (myContainer.canBoxFit(box) && !added){
    // myContainer.addBox(box);
    // added = true;
    // }
    // }
    // if (!added){
    // result.add(constructContainerForBox(box));
    // }
    // }
    // return result;
    // }

    // public List<Container> constructContainersForMirrors(List<Art> myArts,
    // boolean canAcceptCrates) {
    // List<Container> result = new ArrayList<>();
    // for (Art art : myArts) {
    // if (!art.materialContains(Art.Material.Mirror)){
    // throw new IllegalArgumentException("One or more Items in arts is NOT a
    // Mirror. All Arts must be Mirror to put directly into a Crate");
    // }
    // if (result.isEmpty() || result.getLast().isFull()){
    // result.add(constructContainerForArt(art));
    // }
    // else {
    // result.getLast().addArt(art);
    // }
    // }
    // return result;
    // }

    // ^--- This is how packing could implement packBoxIntoContainers ---^

    public Container constructContainerForArt(Art art) {
        if (this.canAcceptCrate){
            Container myContainer = new Container(Type.Crate, this.canAcceptCrate);
            myContainer.setArtCrate(true);
            myContainer.addArt(art);
            myContainer.setContainerHeight(Math.max(myContainer.getHeight(), art.getHeight() + 8));
            myContainer.setCrateMedium(art);
            if (art.getWidth() <= 36){
                myContainer.setShortCrate(false);
            }
            else if (art.getWidth() <= 46){
                myContainer.setShortCrate(true);
            }
            else {
                throw new IllegalArgumentException(
                    "Art Too Long, can't fit into Crate");
            }
            return myContainer;
        } else {
            throw new IllegalArgumentException(
                    "You can only construct a direct Container for Mirrors, for all other art use Boxes");
        }
    }

    public static Container constructContainerForArt(Art art, boolean canAcceptCrate) {
        if (canAcceptCrate){
            Container myContainer = new Container(Type.Crate, canAcceptCrate);
            myContainer.setArtCrate(true);
            myContainer.addArt(art);
            myContainer.setContainerHeight(Math.max(myContainer.getHeight(), art.getHeight() + 8));
            myContainer.setCrateMedium(art);
            if (art.getWidth() <= 36){
                myContainer.setShortCrate(false);
            }
            else if (art.getWidth() <= 46){
                myContainer.setShortCrate(true);
            }
            else {
                throw new IllegalArgumentException(
                    "Art Too Long, can't fit into Crate");
            }
            return myContainer;
        } else {
            throw new IllegalArgumentException(
                    "You can only construct a direct Container for Mirrors, for all other art use Boxes");
        }
    }

    // public Container constructContainerForArt(Art art) {
    //     if (art.getType() == Art.Type.Mirror){
    //         Container myContainer = new Container(Type.Crate, this.canAcceptCrate);
    //         myContainer.setMirrorCrate(true);
    //         myContainer.addArt(art);
    //         myContainer.setContainerHeight(Math.max(myContainer.getHeight(), art.getHeight() + CRATE_HEIGHT_OVERHEAD));
    //         myContainer.setCrateNormal();
    //         return myContainer;
    //     } else {
    //         throw new IllegalArgumentException(
    //                 "You can only construct a direct Container for Mirrors, for all other art use Boxes");
    //     }
    // }

    public Container constructContainerForBox(Box box) {
        if (box.isCustom() && !isSmallEnoughForGlassPallet(box)){
            // Leave Large Custom Boxes to Bri
            return null;
        }
        if (this.isSmallEnoughForGlassPallet(box)) {
            Container myContainer = new Container(Type.Glass, this.canAcceptCrate);
            myContainer.setMirrorCrate(false);
            myContainer.addBox(box);
            myContainer.setPalletGlass();
            myContainer.setContainerHeight(box.getHeight());
            return myContainer;
        }
        else if (box.getWidth() > OVERSIZE_CRATE_LIMIT || box.getLength() > OVERSIZE_CRATE_LIMIT 
        || box.getHeight() > CRATE_ABSOLUTE_HEIGHT_LIMIT){
            Container myContainer = new Container(Type.Custom, this.canAcceptCrate);
            myContainer.setMirrorCrate(false);
            myContainer.addBox(box);
            return myContainer;
        }
        else if (this.canAcceptCrate()){
            Container myContainer = new Container(Type.Crate, this.canAcceptCrate);
            myContainer.setMirrorCrate(false);
            myContainer.addBox(box);
            myContainer.setCrateNormal();
            myContainer.setContainerHeight(box.getHeight() + CRATE_HEIGHT_OVERHEAD);
            return myContainer;
        }
        else if (box.isOversized()){
            Container myContainer = new Container(Type.Oversize, this.canAcceptCrate);
            myContainer.setMirrorCrate(false);
            myContainer.addBox(box);
            myContainer.setPalletOversize();
            myContainer.setContainerHeight(box.getHeight());
            return myContainer;
        }
        else {
            Container myContainer = new Container(Type.Pallet, this.canAcceptCrate);
            myContainer.addBox(box);
            myContainer.setMirrorCrate(false);
            myContainer.setPalletNormal();
            myContainer.setContainerHeight(box.getHeight());
            return myContainer;
        }
    }

    public boolean addBox(Box box){
        if (this.isArtCrate()){
            return false;
            // throw new IllegalStateException("Can not add a Box to a Mirror Crate.");
        }
        if (this.canBoxFit(box)){
            this.boxes.add(box);
        }
        else {
            return false;
            // throw new IllegalArgumentException("Box can't fit into this container");
        }
        if (this.getType() == Type.Crate){
            this.setContainerHeight(Math.max(this.getHeight(), box.getHeight() + CRATE_HEIGHT_OVERHEAD));
        }
        else {
            this.setContainerHeight(Math.max(this.getHeight(), box.getHeight()));
        }
        this.updateFullness();
        return true;
    };

    // public boolean addBox(Box box){
    //     if (this.isMirrorCrate()){
    //         return false;
    //         // throw new IllegalStateException("Can not add a Box to a Mirror Crate.");
    //     }
    //     if (this.canBoxFit(box)){
    //         this.boxes.add(box);
    //     }
    //     else {
    //         return false;
    //         // throw new IllegalArgumentException("Box can't fit into this container");
    //     }
    //     if (this.getType() == Type.Crate){
    //         this.setContainerHeight(Math.max(this.getHeight(), box.getHeight() + CRATE_HEIGHT_OVERHEAD));
    //     }
    //     else {
    //         this.setContainerHeight(Math.max(this.getHeight(), box.getHeight()));
    //     }
    //     this.updateFullness();
    //     return true;
    // };

    public boolean addArt(Art art){
        if (this.canArtFit(art)){
            this.arts.add(art);
            this.updateFullness();
            return true;
        }
        return false;
    };

    // public boolean addArt(Art art){
    //     if (this.getType() == Type.Crate && this.isEmpty() && art.getType() == ArchDesign.entities.Art.Type.Mirror){
    //         this.setMirrorCrate(true);
    //     }
    //     if (!this.isMirrorCrate()){
    //         return false;
    //         // throw new IllegalStateException("Can not add Arts in a Non-Mirror Crate.");
    //     }
    //     if (this.isFull() || art.getType() != ArchDesign.entities.Art.Type.Mirror){
    //         return false;
    //     }
    //     this.arts.add(art);
    //     this.updateFullness();
    //     return true;
    // };

    public boolean canBoxFit(Box box) {

        // TODO: fix this

        if (this.isArtCrate()){
            return false;
            // throw new IllegalStateException("Can not check Boxes in a Mirror Crate.");
        }

        boolean isBoxOversized = box.isOversized();

        // Crate is Full, can't fit Box
        if (this.isFull()) {
            // throw new IllegalStateException("Container is Full");
            return false;
        }
        // Crate is empty
        else if (this.isEmpty()) {
            if (box.isCustom() && this.getType() != Type.Custom){
                return false;
            }
            else if (box.isCustom() && this.getType() == Type.Glass && !this.isSmallEnoughForGlassPallet(box)){
                return false;
            }
            else if (box.isCustom() && this.getType() == Type.Glass && this.isSmallEnoughForGlassPallet(box)){
                return true;
            } 
            else if (!box.isCustom() && this.getType() == Type.Custom){
                return false;
            }
            else if (this.getType() == Type.Custom){
                // Custom Crate, It is for something that is custom, can't put stuff in it
                return true;
            }
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
        else if (this.getType() == Type.Glass && this.isSmallEnoughForGlassPallet(box)) {
            // It is a non-full Glass Crate, and the Box is small enough to fit in it
            return true;
        }
        else if (this.getType() == Type.Glass && !this.isSmallEnoughForGlassPallet(box)){
            // It is a non-full Glass Crate, and the Box is not small enough to fit in it
            return false;
        }
        else if (box.isCustom()){
            // Custombox is only for custom crate unless it is smalle enough
            return false;
        }
        else if (this.getType() == Type.Custom){
            // Custom Crate, It is for something that is custom, can't put stuff in it
            return false;
        }
        // If Container is Normal Pallet and box is oversized 
        else if (this.getType() == Type.Pallet && (isBoxOversized) && (this.getCapacity() - this.getCurrentSize() > 1)){
            return true;
        }
        else if (this.getType() == Type.Oversize && (isBoxOversized) && (this.getCapacity() - this.getCurrentSize() > 2)){
            return true;
        }
        else {
            // Assuming we sorted the Box by size before checking, all other cases are
            // Container is Oversized, Box isn't
            // or that they are both Normal or Both Oversized. So in all other cases Box can
            // fit
            return true;
        }
    }

    // public boolean canBoxFit(Box box) {

    //     // TODO: fix this

    //     if (this.isMirrorCrate()){
    //         throw new IllegalStateException("Can not check Boxes in a Mirror Crate.");
    //     }

    //     boolean isBoxOversized = box.isOversized();

    //     // Crate is Full, can't fit Box
    //     if (this.isFull()) {
    //         // throw new IllegalStateException("Container is Full");
    //         return false;
    //     }
    //     // Crate is empty
    //     else if (this.isEmpty()) {
    //         if (box.isCustom() && this.getType() != Type.Custom){
    //             return false;
    //         }
    //         else if (box.isCustom() && this.getType() == Type.Glass && !this.isSmallEnoughForGlassPallet(box)){
    //             return false;
    //         }
    //         else if (box.isCustom() && this.getType() == Type.Glass && this.isSmallEnoughForGlassPallet(box)){
    //             return true;
    //         } 
    //         else if (!box.isCustom() && this.getType() == Type.Custom){
    //             return false;
    //         }
    //         else if (this.getType() == Type.Custom){
    //             // Custom Crate, It is for something that is custom, can't put stuff in it
    //             return true;
    //         }
    //         // Box is oversize, Crate needs to be Oversize
    //         if (isBoxOversized) {
    //             this.isCarryingOversizeBox = true;
    //             return true;
    //         }
    //         // Box isn't oversize, Crate can be Standard
    //         this.isCarryingOversizeBox = false;
    //         return true;
    //     }
    //     // Crate has something in it
    //     else if (this.getType() == Type.Glass && this.isSmallEnoughForGlassPallet(box)) {
    //         // It is a non-full Glass Crate, and the Box is small enough to fit in it
    //         return true;
    //     }
    //     else if (this.getType() == Type.Glass && !this.isSmallEnoughForGlassPallet(box)){
    //         // It is a non-full Glass Crate, and the Box is not small enough to fit in it
    //         return false;
    //     }
    //     else if (box.isCustom()){
    //         // Custombox is only for custom crate unless it is smalle enough
    //         return false;
    //     }
    //     else if (this.getType() == Type.Custom){
    //         // Custom Crate, It is for something that is custom, can't put stuff in it
    //         return false;
    //     }
    //     // If Container is Normal Pallet and box is oversized 
    //     else if (this.getType() == Type.Pallet && (isBoxOversized) && (this.getCapacity() - this.getCurrentSize() > 1)){
    //         return true;
    //     }
    //     else {
    //         // Assuming we sorted the Box by size before checking, all other cases are
    //         // Container is Oversized, Box isn't
    //         // or that they are both Normal or Both Oversized. So in all other cases Box can
    //         // fit
    //         return true;
    //     }
    // }

    public boolean canArtFit(Art art) {
        if (!this.isArtCrate()) {
            return false;
            // throw new IllegalStateException("Can not check Arts in a Non-Mirror Crate.");
        }
        // Crate is Full, can't fit Box
        if (this.isFull()) {
            return false;
        }
        if (this.getCrateMedium() != art.getMaterial()){
            // Can put Art into the box
            return false;
        }
        return true;
    }

    // public boolean canArtFit(Art art) {
    //     if (!this.isMirrorCrate()) {
    //         throw new IllegalStateException("Can not check Arts in a Non-Mirror Crate.");
    //     }
    //     if (art.getType() != Art.Type.Mirror) {
    //         throw new IllegalStateException("Can not put any Art other than Mirror in a Mirror Crate.");
    //     }
    //     // Crate is Full, can't fit Box
    //     if (this.isFull()) {
    //         return false;
    //     } else {
    //         // Can put Mirrors into the box
    //         return true;
    //     }
    // }

    // -------- getters and setters --------

    public boolean isFull() {
        return this.isFull;
    }

    public boolean isEmpty() {
        return this.isEmpty;
    }

    public boolean isMirrorCrate() {
        return this.isMirrorCrate;
    }

    public boolean isArtCrate() {
        return this.isArtCrate;
    }

    public boolean isShortCrate() {
        return this.isShortCrate;
    }

    public void setShortCrate(boolean bool) {
        this.isShortCrate = bool;
    }

    public void setCrateMedium(Art art) {
        this.crateMedium = art.getMaterial();
    }

    public Art.Material getCrateMedium() {
        return this.crateMedium;
    }

    public int getCurrentSize() {
        return (isArtCrate()) ? this.arts.size() : this.boxes.size();
    }

    // public int getCurrentSize() {
    //     return (isMirrorCrate()) ? this.arts.size() : this.boxes.size();
    // }

    public Type getType() {
        return this.type;
    }

    public boolean canAcceptCrate() {
        return this.canAcceptCrate;
    }

    public double getLength() {
        return this.length;
    };

    public double getWidth() {
        return this.width;
    };

    public double getHeight() {
        return this.height;
    }

    public List<Box> getBoxes() {
        if (this.isArtCrate()) {
            throw new IllegalStateException("Can not access Boxes in a Mirror Crate.");
        }
        return this.boxes;
    };

    public List<Art> getArts() {
        if (!this.isArtCrate()) {
            throw new IllegalStateException("Can not access Arts in a Non-Mirror Crate.");
        }
        return this.arts;
    };

    public int getWeight() {
        int weight = 0;
        if (this.isArtCrate()) {
            for (Art art : arts) {
                weight += art.getWeight();
            }
        } else {
            for (Box box : boxes) {
                weight += box.getWeight();
            }
        }
        this.setWeight(weight + getTareWeight());
        return this.weight;
    }

    // public List<Box> getBoxes() {
    //     if (this.isMirrorCrate()) {
    //         throw new IllegalStateException("Can not access Boxes in a Mirror Crate.");
    //     }
    //     return this.boxes;
    // };

    // public List<Art> getArts() {
    //     if (!this.isMirrorCrate()) {
    //         throw new IllegalStateException("Can not access Arts in a Non-Mirror Crate.");
    //     }
    //     return this.arts;
    // };

    // public int getWeight() {
    //     int weight = 0;
    //     if (this.isMirrorCrate()) {
    //         for (Art art : arts) {
    //             weight += art.getWeight();
    //         }
    //     } else {
    //         for (Box box : boxes) {
    //             weight += box.getWeight();
    //         }
    //     }
    //     this.setWeight(weight + getTareWeight());
    //     return this.weight;
    // }

    public int getCapacity() {
        // return its capacity if it can be calculated, else return an
        // IllegalStateException
        if (!this.setCapacity()) {
            //throw new IllegalStateException("Capacity cannot be determined for this Container as it is empty.");
            return -1;
        }
        return this.capacity;
    }

    public int getJustContainerWeight(){
        return this.getTareWeight();
    }

    // ---------------- helpers ----------------

    private boolean setPalletDimensions(){
        if (this.getType() == ArchDesign.entities.Container.Type.Pallet){
            this.setPalletNormal();
            return true;
        }
        else if (this.getType() == ArchDesign.entities.Container.Type.Oversize){
            this.setPalletOversize();
            return true;
        }
        else {
            return false;
        }
    }

    private boolean setMirrorCrate(boolean bool) {
        this.isMirrorCrate = bool;
        return this.isMirrorCrate;
    }

    private boolean setArtCrate(boolean bool) {
        this.isArtCrate = bool;
        return this.isArtCrate;
    }

    private void setPalletNormal() {
        this.length = STANDARD_PALLET_LENGTH;
        this.width = STANDARD_PALLET_WIDTH;
    }

    private void setPalletGlass() {
        this.length = GLASS_PALLET_LENGTH;
        this.width = GLASS_PALLET_WIDTH;
    }

    private void setPalletOversize() {
        this.length = OVERSIZE_PALLET_LENGTH;
        this.width = OVERSIZE_PALLET_WIDTH;
    }

    private void setCrateNormal() {
        this.length = CRATE_LENGTH;
        this.width = CRATE_WIDTH;
    }

    private void setContainerHeight(double height) {
        this.height = height;
    }

    private void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isCarryingOversizeBox(){
        if (this.isArtCrate()){
            return false;
            // throw new IllegalStateException("There are no Boxes in a Mirror Crate.");
        }
        return this.isCarryingOversizeBox;
    }
    
    // public boolean isCarryingOversizeBox(){
    //     if (this.isMirrorCrate()){
    //         throw new IllegalStateException("There are no Boxes in a Mirror Crate.");
    //     }
    //     return this.isCarryingOversizeBox;
    // }

    private int getTareWeight() {
        if (this.getType() == Type.Crate) {
            return OVERHEAD_CRATE_WEIGHT;
        } else if (this.getType() == Type.Oversize) {
            return OVERHEAD_OVERSIZE_PALLET_WEIGHT;
        }
        return OVERHEAD_PALLET_WEIGHT;
    }

    private void updateFullness() {
        if (this.isArtCrate()) {
            if (this.arts.size() == 0){
                this.isEmpty = true;
                this.isFull = false;
                return;
            }
            else if (this.arts.size() >= this.getCapacity() && this.getCapacity() !=-1) {
                this.isEmpty = false;
                this.isFull = true;
                return;
            } else {
                this.isEmpty = false;
                this.isFull = false;
                return;
            }
        } 
        else {
            if (this.boxes.size() == 0){
                this.isEmpty = true;
                this.isFull = false;
                return;
            }
            else if (this.boxes.size() >= this.getCapacity() && this.getCapacity() !=-1) {
                this.isEmpty = false;
                this.isFull = true;
                return;
            } else {
                this.isEmpty = false;
                this.isFull = false;
                return;
            }
        }
    }

    // private void updateFullness() {
    //     if (this.isMirrorCrate()) {
    //         if (this.arts.size() == 0){
    //             this.isEmpty = true;
    //             this.isFull = false;
    //             return;
    //         }
    //         else if (this.arts.size() >= this.getCapacity() && this.getCapacity() !=-1) {
    //             this.isEmpty = false;
    //             this.isFull = true;
    //             return;
    //         } else {
    //             this.isEmpty = false;
    //             this.isFull = false;
    //             return;
    //         }
    //     } 
    //     else {
    //         if (this.boxes.size() == 0){
    //             this.isEmpty = true;
    //             this.isFull = false;
    //             return;
    //         }
    //         else if (this.boxes.size() >= this.getCapacity() && this.getCapacity() !=-1) {
    //             this.isEmpty = false;
    //             this.isFull = true;
    //             return;
    //         } else {
    //             this.isEmpty = false;
    //             this.isFull = false;
    //             return;
    //         }
    //     }
    // }

    protected boolean isSmallEnoughForGlassPallet(Box box){
        //TODO: Ask about the Threshold. It says for small shipments, but never specifies the dimensions so I made it up
        if (box.getLength() > GLASS_PALLET_LENGTH_THRESHOLD || box.getWidth() > GLASS_PALLET_WIDTH_THRESHOLD){
            return false;
        }
        return true;
    }

    private boolean setCapacity() {

        if (this.isEmpty()) {
            // Return a void value if the box is empty. Because if the box is empty we don't
            // know the items in it
            // which means we don't know its capacity
            return false;
        }

        Box containerFirstBox = null;
        Type containerType = null;
        boolean boxOversized = false;
        if (!this.isArtCrate()){
            containerFirstBox = this.getBoxes().get(0);
            containerType = this.getType();
            for (Box box : this.getBoxes()){
                boxOversized = boxOversized | box.isOversized();
            }
        }

        // Assuming the Container only has Box that is of same or smaller type than
        // first

        if (containerType != Type.Custom && !this.isArtCrate()) {
            if (containerType == Type.Pallet) {
                this.capacity = 4;
            } else if (containerType == Type.Oversize) {
                this.capacity = 5;
            }
            if (boxOversized){
                this.capacity = 3;
            }
            return true;
        } else if (this.isArtCrate()) {
            if (this.isShortCrate()){
                if (this.getCrateMedium() == Art.Material.Glass || this.getCrateMedium() == Art.Material.Acyrlic || this.getCrateMedium() == Art.Material.Mirror){
                    this.capacity = OVERSIZE_GLASS_ACRYLIC_MIRROR_CRATE_LIMIT;
                }
                else {
                    this.capacity = OVERSIZE_CANVAS_ACOUSTIC_CRATE_LIMIT;
                }
            }
            else {
                if (this.getCrateMedium() == Art.Material.Glass || this.getCrateMedium() == Art.Material.Acyrlic || this.getCrateMedium() == Art.Material.Mirror){
                    this.capacity = NORMAL_GLASS_ACRYLIC_MIRROR_CRATE_LIMIT;
                }
                else {
                    this.capacity = NORMAL_CANVAS_ACOUSTIC_CRATE_LIMIT;
                }
            }
            return true;
        }
        else {
            this.capacity = 1;
            return true;
        } 
        // else {
        //     // TODO: Is this considered a Custom Box? Does it need human evaluation?
        //     boolean isDimensionsOversize = false;
        //     for (Box box : this.getBoxes()){
        //         if (box.isCustom() && (box.getHeight() > CUSTOM_BOX_DIMENSION_LIMIT || box.getWidth() > CUSTOM_BOX_DIMENSION_LIMIT)){
        //             isDimensionsOversize = true;
        //         }
        //     }
        //     if (containerFirstBox.getArts().get(0).materialContains(Art.Material.Glass)
        //     || containerFirstBox.getArts().get(0).materialContains(Art.Material.Acyrlic)){
        //         if (isDimensionsOversize){
        //             this.capacity = OVERSIZE_GLASS_ACRYLIC_CRATE_LIMIT;
        //         } else {
        //             this.capacity = NORMAL_GLASS_ACRYLIC_CRATE_LIMIT;
        //         }
        //     }
        //     else if (containerFirstBox.getArts().get(0).materialContains(Art.Material.CanvasFramed) 
        //     || containerFirstBox.getArts().get(0).materialContains(Art.Material.CanvasGallery)){
        //         if (isDimensionsOversize){
        //             this.capacity = OVERSIZE_CANVAS_CRATE_LIMIT;
        //         } else {
        //             this.capacity = NORMAL_CANVAS_CRATE_LIMIT;
        //         }
        //     }
        //     return true;
        // }
    }

    // private boolean setCapacity() {

    //     if (this.isEmpty()) {
    //         // Return a void value if the box is empty. Because if the box is empty we don't
    //         // know the items in it
    //         // which means we don't know its capacity
    //         return false;
    //     }

    //     Box containerFirstBox = null;
    //     Type containerType = null;
    //     boolean boxOversized = false;
    //     if (!this.isMirrorCrate()){
    //         containerFirstBox = this.getBoxes().get(0);
    //         containerType = this.getType();
    //         for (Box box : this.getBoxes()){
    //             boxOversized = box.isOversized();
    //         }
    //     }

    //     // Assuming the Container only has Box that is of same or smaller type than
    //     // first

    //     if (containerType != Type.Custom && !this.isMirrorCrate()) {
    //         int containerInitialCapacity = 0;
    //         if (containerType == Type.Pallet || containerType == Type.Crate || containerType == Type.Glass) {
    //             //TODO: What is the capacity for Glass? Since not given I made it up
    //             containerInitialCapacity = 4;
    //         } else if (containerType == Type.Oversize) {
    //             containerInitialCapacity = 5;
    //         }
    //         if (boxOversized) {
    //             containerInitialCapacity--;
    //         }

    //         this.capacity = containerInitialCapacity;
    //         return true;
    //     } else if (this.isMirrorCrate()) {
    //         this.capacity = MIRROR_CRATE_LIMIT;
    //         return true;
    //     }
    //     else {
    //         this.capacity = 1;
    //         return true;
    //     } 
    //     // else {
    //     //     // TODO: Is this considered a Custom Box? Does it need human evaluation?
    //     //     boolean isDimensionsOversize = false;
    //     //     for (Box box : this.getBoxes()){
    //     //         if (box.isCustom() && (box.getHeight() > CUSTOM_BOX_DIMENSION_LIMIT || box.getWidth() > CUSTOM_BOX_DIMENSION_LIMIT)){
    //     //             isDimensionsOversize = true;
    //     //         }
    //     //     }
    //     //     if (containerFirstBox.getArts().get(0).materialContains(Art.Material.Glass)
    //     //     || containerFirstBox.getArts().get(0).materialContains(Art.Material.Acyrlic)){
    //     //         if (isDimensionsOversize){
    //     //             this.capacity = OVERSIZE_GLASS_ACRYLIC_CRATE_LIMIT;
    //     //         } else {
    //     //             this.capacity = NORMAL_GLASS_ACRYLIC_CRATE_LIMIT;
    //     //         }
    //     //     }
    //     //     else if (containerFirstBox.getArts().get(0).materialContains(Art.Material.CanvasFramed) 
    //     //     || containerFirstBox.getArts().get(0).materialContains(Art.Material.CanvasGallery)){
    //     //         if (isDimensionsOversize){
    //     //             this.capacity = OVERSIZE_CANVAS_CRATE_LIMIT;
    //     //         } else {
    //     //             this.capacity = NORMAL_CANVAS_CRATE_LIMIT;
    //     //         }
    //     //     }
    //     //     return true;
    //     // }
    // }

    // private List<Box> removeBox(Box box){
    // if (this.isMirrorCrate()){
    // throw new IllegalStateException("Can not remove a Box from a Mirror Crate.");
    // }
    // this.boxes.remove(box);
    // this.updateFullness();
    // return this.boxes;
    // };

    // private List<Art> removeArt(Art art){
    // if (!this.isMirrorCrate()){
    // throw new IllegalStateException("Can not remove Arts from a Non-Mirror
    // Crate.");
    // }
    // this.arts.remove(art);
    // this.updateFullness();
    // return this.arts;
    // };
}
