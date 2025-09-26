package entities;

import java.util.List;

public class Art {
    
    public enum Type { PaperPrint } //Add all
    public enum Glazing { Glass } //Add all
    private int hardware;
    private final int lineNumber; 
    private Type type;
    private Glazing glazing;
    private double depth;
    private double width;
    private double height;
    private double weight;
    private boolean specialHandling;
    private boolean isCustom;

    public Art(Type type, Glazing glazing, int lineNumber, int width, int height, int hardware) {
        super();
        this.lineNumber = lineNumber;
        // UNfinished
    }

    public Type getType(){
        return null;
    }

    public Glazing getGlazing(){
        return null;
    }

    public int getLineNumber(){
        return -1;
    }

    public double getwidth(){
        return -1;
    }

    public double getHeight(){
        return -1;
    }

    public double getDepth(){
        return -1;
    }

    private boolean setDepth(){
        return false;
    }

    public double getHardware(){
        return -1;
    };

    public double getWeight(){
        return -1;
    }

    public boolean needSpecialHandling(){
        //C. Flag Tactile Panels and Raised Float Mounts for Special Handling.
        return false;
    }

    public boolean isCustom(){
        //B. Any item with a dimension over 44 inches requires "Custom" packaging.
        return false;
    }

}
