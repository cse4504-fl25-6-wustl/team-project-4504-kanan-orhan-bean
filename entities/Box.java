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

    public Box(boolean isOversized) {
        super();
        this.isOversized = isOversized;
        // UNfinished
        this.isFull = false;
    }

    public void setBoxFull(){
        this.isFull = true;
    }

    public void setBoxEmpty(){
        this.isFull = false;
    }

    public void setBoxNormal(){
        this.length = 37;
        this.width = 11;
        this.height = 31;
    }

    public void setBoxOversize(){
        this.length = 44;
        this.width = 13;
        this.height = 48;
    }

    public boolean isBoxFull(){
        return this.isFull;
    }

    public double getLength(){
        return -1;
    };

    public boolean setLength(){
        return false;
    }

    public double getWidth(){
        return -1;
    };

    public boolean setWdith(){
        return false;
    }

    public double getHeight(){
        return -1;
    };

    public boolean setHeight(){
        return false;
    }

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

    public boolean setWeight(){
        return false;
    }

    public int getCapacity(){
        return -1;
    }

    public boolean setCapacity(){
        //A. Apply Strict Box Capacity Limits based on Product Type.
            //i. Framed Prints: Max 6 pieces per standard box.
            //ii. Canvases & Acoustic Panels: Max 4 pieces per standard box (due to their greater depth).
        return false;
    }
}
