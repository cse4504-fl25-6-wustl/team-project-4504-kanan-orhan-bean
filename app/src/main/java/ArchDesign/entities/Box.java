package ArchDesign.entities;

import java.util.ArrayList;
import java.util.List;

public class Box {

    private boolean isOversized;
    private boolean isFull;
    private double length;
    private double width;
    private double height;
    private double weight;
    private List<Art> arts;
    private int capacity;
    private boolean isCustom;

    private final double STANDARD_LENGTH = 37;
    private final double STANDARD_WIDTH = 11;
    private final double STANDARD_HEIGHT = 31;
    private final double OVERSIZE_LENGTH = 44;
    private final double OVERSIZE_WIDTH = 13;
    private final double OVERSIZE_HEIGHT = 48;
    private final double OVERSIZE_BOX_LIMIT = 36;

    public Box() {
        super();
        this.isFull = false;
        this.arts = new ArrayList<>();
    }

    public boolean isFull(){
        return this.isFull;
    }

    public boolean isEmpty(){
        return this.arts.isEmpty();
    }

    public boolean isCustom(){
        return this.isCustom;
    }

    private void setBoxNormal(){
        this.length = STANDARD_LENGTH;
        this.width = STANDARD_WIDTH;
        this.height = STANDARD_HEIGHT;
    }

    private void setBoxOversize(){
        this.length = OVERSIZE_LENGTH;
        this.width = OVERSIZE_WIDTH;
        this.height = OVERSIZE_HEIGHT;
    }

    public void setBoxCustom(double length, double width, double height){
        this.length = length;
        this.width = width;
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
    };

    public boolean isOversized(){
        return this.isOversized;
    };

    public List<Art> getArts(){
        return this.arts;
    };

    public List<Art> addArt(Art art){
        this.arts.add(art);
        boolean isArtOversized = art.getHeight() > OVERSIZE_BOX_LIMIT || art.getWidth() > OVERSIZE_BOX_LIMIT;
        if (isArtOversized){
            this.setBoxOversize();
        }
        else {
            this.setBoxNormal();
        }
        return this.arts;
    };

    public List<Art> removeArt(Art art){
        this.arts.remove(art);
        return this.arts;
    };

    public boolean canArtFit(Art art){
        // TODO: Might have to rework depending on https://piazza.com/class/mf6woqytzb81zq/post/13

        boolean isArtOversized = art.getHeight() > OVERSIZE_BOX_LIMIT || art.getWidth() > OVERSIZE_BOX_LIMIT;

        // Art is Custom, needs Custom Box
        if (art.isCustom()){
            if (this.isEmpty()){
                this.isCustom = true;
                return true;
            }
            return false;
        }
        // Box is Full, can't fit Art
        else if (this.isFull()){
            return false;
        }
        // Box is empty
        else if (this.isEmpty()){
            // Art is oversize, Box needs to be Oversize
            if (isArtOversized) {
                this.isOversized = true;
                return true;
            }
            // Art isn't oversize, Box can be Standard
            this.isOversized = false;
            return true;
        }
        // If box isn't oversized and art is oversized 
        else if ((!this.isOversized()) && isArtOversized){
            return false;
        }
        // Assuming we sorted the art by size before checking, all other cases are Box is Oversized, Art isn't 
        // or that they are both the same size. So in all other cases Art can fit
        return true;
    }

    public double getWeight(){
        double weight = 0.0;
        for (Art art : arts) {
            weight += art.getWeight();
        }
        this.setWeight(weight);
        return this.weight;
    }

    private void setWeight(double weight){
        this.weight = weight;
    }

    public int getCapacity(){
        // return its capacity if it can be calculated, else return an IllegalStateException 
        // if we can't determine the capacity due to it being empty. Or we have mirrors in it
        if (!setCapacity()) {
            throw new IllegalStateException("Capacity cannot be determined for this box.");
        }
        return this.capacity;
    }

    // Adding this as a way for Packing to override Capacity in case of a Client 
    // (Like Sunrise requiring allowing 8 art per box)
    public void overrideCapacity(int overridenCapacity){
        this.capacity = overridenCapacity;
    }

    private boolean setCapacity(){
        //A. Apply Strict Box Capacity Limits based on Product Type.
            //i. Framed Prints: Max 6 pieces per standard box.
            //ii. Canvases & Acoustic Panels: Max 4 pieces per standard box (due to their greater depth).

        if (this.isEmpty()){
            // Return a void value if the box is empty. Because if the box is empty we don't know the items in it 
            // which means we don't know its capacity
            return false;
        }

        Art boxFirstArt = arts.get(0);
        // Assuming the Box only has Art that is of same Material
        if (boxFirstArt.materialContains("Glass") || boxFirstArt.materialContains("Acrylic")){
            this.capacity = 6;
            return true;
        }
        else if (boxFirstArt.materialContains("Canvas")){
            // TODO: Might rework under "Canvas Rule Discrepency" on this post https://piazza.com/class/mf6woqytzb81zq/post/7
            this.capacity = 6;
            return true;
        }
        else if (boxFirstArt.materialContains("Acoustic")){
            this.capacity = 4;
            return true;
        }
        // We have a mirror. It is best practice to use Crates not boxes. Return false
        return false;
    }
}
