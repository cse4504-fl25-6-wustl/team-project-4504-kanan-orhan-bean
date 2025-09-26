package entities;

import java.util.List;

public class Container {

    // Create a Pallet Class

    public enum Type { Pallet, Crate , Custom}
    private Type type;
    private double length;
    private double width;
    private double height;
    private List<Box> boxes;
    private final int overheadPalletWeight = 60;
    private final int overheadCrateWeight = 125;

    public Container(Type type, boolean canAcceptCrate) {
        super();
        // UNfinished
    }

    public Type getType(){
        return null;
    }

    public double getLength(){
        return -1;
    };

    public double getWidth(){
        return -1;
    };

    public double getHeight(){
        return -1;
    }

    public List<Box> getBoxes(){
        return null;
    };

    public List<Box> addBox(Box box){
        return null;
    };

    public List<Box> removeBox(Box box){
        return null;
    };

    public boolean canBoxFit(Box box){
        return false;
    }
}
