package ArchDesign.entities;

import java.util.ArrayList;
import java.util.List;

public class Box {

    private boolean isOversized;
    private boolean isNormal;
    private boolean isFull;
    private double length;
    private double width;
    private double height;
    private int weight;
    private List<Art> arts;
    private double remainingCapacity;
    private boolean isCustom;

    // TODO: UPDATE VALUE LATER
    protected static final double SMALLEST_ART_DEPTH = 4;

    protected static final double STANDARD_LENGTH = 37;
    protected static final double STANDARD_WIDTH = 11;
    protected static final double STANDARD_HEIGHT = 31;
    protected static final double OVERSIZE_LENGTH = 44;
    protected static final double OVERSIZE_WIDTH = 13;
    protected static final double OVERSIZE_HEIGHT = 48;
    protected static final double STANDARD_BOX_LIMIT = 36;
    protected static final double OVERSIZE_BOX_LIMIT = 44; // or 43.5 ?!?!

    // constructor
    protected Box() {
        super();
        this.isFull = false;
        this.arts = new ArrayList<>();
        this.weight = 0;
    }


// ----- PUBLIC FUNCTIONS -----

// --- getters ---

    public boolean isFull(){
        return this.isFull;
    }
    public boolean isEmpty(){
        return this.arts.isEmpty();
    }
    public boolean isCustom(){
        return this.isCustom;
    }
    public boolean isOversized(){
        return this.isOversized;
    };
    public boolean isNormal(){
        return this.isNormal;
    };

    public double getLength(){
        return this.length;
    };
    public double getWidth(){
        return this.width;
    };
    public double getHeight(){
        return this.height;
    };

    public List<Art> getArts(){
        return this.arts;
    };

    // BEAN - dont really need this since we have an isFull() method
    public double getCapacity(){
        return this.remainingCapacity;
    }
    public int getWeight(){
        return this.weight;
    }


// --- utility functions ---

    // BEAN - factory function that creates standard, oversized, or custom boxes (set to size 0) based on an art piece
    public static Box createBoxForArt(Art art) {
        Box box = new Box();

        if (isArtWithinStandardLimit(art)) {
            box.setBoxNormal();
        }
        else if (isArtWithinOversiveLimit(art)) {
            box.setBoxOversize();
        }
        // can't do custom boxes
        else {
            box.setBoxCustom(0, 0, 0);
        }
        box.remainingCapacity = box.width;
        return box;
    }    
    
    // BEAN - adds an art to a box, if possible.
    public boolean addArt(Art art){
        if (canArtFit(art)) {
            this.arts.add(art);
            setRemainingCapacity(art);
            setWeight(art.getWeight());
            if (this.remainingCapacity <= SMALLEST_ART_DEPTH) {
                this.isFull = true;
            }
            return true;
        }
        return false;
    };    
    
    // BEAN - purely checks for whether or not an art can be added to this box instance
    public boolean canArtFit(Art art){
        // TODO: Might have to rework depending on https://piazza.com/class/mf6woqytzb81zq/post/13

        if (this.isFull) {
            return false;
        }
        else if((this.remainingCapacity - art.getDepth()) >= 0) {
            if (isArtCorrectSize(art)) {
                return true;
            }
        }
        return false;
    }    
    
    
    
// ----- PRIVATE FUNCTIONS -----

// --- setters ---

    private void setWeight(double addedWeight){
        this.weight += addedWeight;
    }
    private void setRemainingCapacity(Art art) {
        this.remainingCapacity -= art.getDepth();
    }

    private void setBoxNormal(){
        this.length = STANDARD_LENGTH;
        this.width = STANDARD_WIDTH;
        this.height = STANDARD_HEIGHT;

        this.isNormal = true;
        this.isOversized = false;
        this.isCustom = false;
    }
    private void setBoxOversize(){
        this.length = OVERSIZE_LENGTH;
        this.width = OVERSIZE_WIDTH;
        this.height = OVERSIZE_HEIGHT;

        this.isNormal = false;
        this.isOversized = true;
        this.isCustom = false;
    }
    private void setBoxCustom(double length, double width, double height){
        this.length = length;
        this.width = width;
        this.height = height;

        this.isNormal = false;
        this.isOversized = false;
        this.isCustom = true;
    }
    
// --- checkers ---

    private static boolean isArtWithinStandardLimit(Art art) {
        if (art.getHeight() <= STANDARD_BOX_LIMIT && art.getWidth() <= STANDARD_BOX_LIMIT) {
            return true;
        }
        return false;
    }
    private static boolean isArtWithinOversiveLimit(Art art) {
        if (art.getHeight() <= OVERSIZE_BOX_LIMIT && art.getWidth() <= OVERSIZE_BOX_LIMIT) {
            return true;
        }
        return false;
    };

    private boolean isArtCorrectSize(Art art) {
        if (this.isNormal) {
            if (isArtWithinStandardLimit(art)) {
                return true;
            }
            return false;
        }
        else if (this.isOversized) {
            if (isArtWithinOversiveLimit(art)) {
                return true;
            }
            return false;
        }
        else if (this.isCustom) {
            return true;
        }
        System.err.println("Error: box type not determined before asking if art can fit");
        return false;
    }
}