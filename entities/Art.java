package entities;

import java.util.List;

public class Art {
    
    private enum Type { PaperPrint } //Add all
    private enum Glazing { Glass } //Add all
    private int Hardware;
    private final int lineNumber; 
    private Type type;
    private Glazing glazing;
    private double depth;
    private double width;
    private double height;
    private double weight;
    private boolean specialHandling;
    private boolean isCustom;

    public Art(Type type, Glazing glazing, int lineNumber, int width, int height) {
        super();
        this.lineNumber = lineNumber;
        // UNfinished
    }

    public Type getType(){
        return null;
    }

    public boolean setType(){
        return false;
    }

    public Glazing getGlazing(){
        return null;
    }

    public boolean setGlazing(){
        return false;
    }

    public int getLineNumber(){
        return -1;
    }

    public double getwidth(){
        return -1;
    }

    public boolean setWdith(){
        return false;
    }

    public double getHeight(){
        return -1;
    }

    public boolean setHeight(){
        return false;
    }

    public double getDepth(){
        return -1;
    }

    public boolean setDepth(){
        return false;
    }

    public double getHardware(){
        return -1;
    };

    public boolean setHardware(){
        return false;
    };

    public double getWeight(){
        return -1;
    }

    public boolean setWeight(){
        return false;
    }

    public boolean needSpecialHandling(){
        //C. Flag Tactile Panels and Raised Float Mounts for Special Handling.
        return false;
    }

    public boolean setSpecialHandling(){
        return false;
    }

    public boolean isCustom(){
        //B. Any item with a dimension over 44 inches requires "Custom" packaging.
        return false;
    }

    public boolean setCustom(){
        return false;
    }

}
