package entities;

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

    public Box() {
        super();
        // UNfinished
        this.isFull = false;
    }

    public boolean isFull(){
        return isFull;
    }

    private void setBoxNormal(){
        this.length = 37;
        this.width = 11;
        this.height = 31;
    }

    private void setBoxOversize(){
        this.length = 44;
        this.width = 13;
        this.height = 48;
    }

    public double getLength(){
        return -1;
    };

    public double getWidth(){
        return -1;
    };

    public double getHeight(){
        return -1;
    };

    public boolean isOversized(){
        return false;
    };

    public List<Art> getArts(){
        return null;
    };

    public List<Art> addArt(Art art){
        return null;
    };

    public List<Art> removeArt(Art art){
        return null;
    };

    public boolean canArtFit(Art art){
        return false;
    }

    public double getWeight(){
        return -1;
    }

    private boolean setWeight(){
        return false;
    }

    public int getCapacity(){
        return -1;
    }

    private boolean setCapacity(){
        //A. Apply Strict Box Capacity Limits based on Product Type.
            //i. Framed Prints: Max 6 pieces per standard box.
            //ii. Canvases & Acoustic Panels: Max 4 pieces per standard box (due to their greater depth).
        return false;
    }
}
