package entities;

import java.util.List;

public class Crate {

    // Create a Pallet Class

    private enum Type { Pallet, Crate , Custom}
    private Type type;
    private double length;
    private double width;
    private double height;
    private List<Box> boxes;
    private final int overheadPalletWeight = 60;
    private final int overheadCrateWeight = 125;

    public Crate(Type type) {
        super();
        // UNfinished
    }

    public Type getType(){
        return null;
    }

    public boolean setType(){
        return false;
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
    }

    public boolean setHeight(){
        return false;
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

    public boolean canAcceptPallets(Client client){
        return false;
    }

    public boolean canAcceptCrates(Client client){
        return false;
    }

}
