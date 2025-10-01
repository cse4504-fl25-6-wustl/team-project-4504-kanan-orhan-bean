package entities;

public class Art {
    
    // TODO: Figure out how to handle the Type, Material, and Glazing Enums depending on https://piazza.com/class/mf6woqytzb81zq/post/14
    public enum Type { 
        PaperPrintFramed, 
        PaperPrintFramedWithTitlePlate, 
        CanvasFloatFrame, 
        WallDecor, 
        AcousticPanel, 
        AcousticPanelFramed, 
        MetalPrint, 
        Mirror 
    }
    public enum Material {
        Glass(0.0098), 
        Acyrlic(0.0094), 
        CanvasFramed(0.0085), 
        CanvasGallery(0.0061), 
        Mirror(0.0191), 
        AcousticPanel(0.0038), 
        AcousticPanelFramed(0.0037), 
        PatientBoard(0.0347);

        public final double LBpSQIN;

        private Material(double LBpSQIN) {
            this.LBpSQIN = LBpSQIN;
        }
    }
    public enum Glazing { Glass, Acrylic, NoGlaze }
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
    private Material material;

    private final double CUSTOM_THRESHOLD = 43.5;

    public Art(Type type, Glazing glazing, int lineNumber, double width, double height, int hardware) {
        super();
        this.lineNumber = lineNumber;
        this.type = type;
        this.glazing = glazing;
        this.width = width;
        this.height = height;
        this.hardware = hardware;
        this.material = setMaterial(type, glazing);
        this.specialHandling = setSpecialHandling(type);
        setDepth(4.0);
    }

    private Material setMaterial(Type type, Glazing glazing){
        // TODO
        // How to implement this waiting on https://piazza.com/class/mf6woqytzb81zq/post/14
        return null;
    }

    public Material getMaterial(){
        return this.material;
    }

    private boolean setSpecialHandling(Type type){
        this.specialHandling = typeContains("Acoustic") || typeContains("Float");
        return this.specialHandling;
    }

    private boolean typeContains(String string){
        return this.type.toString().contains(string);
    }

    public Type getType(){
        return this.type;
    }

    public Glazing getGlazing(){
        return this.glazing;
    }

    public int getLineNumber(){
        return this.lineNumber;
    }

    public double getWidth(){
        return this.width;
    }

    public double getHeight(){
        return this.height;
    }

    public double getDepth(){
        return this.depth;
    }

    private boolean setDepth(double depth){
        this.depth = depth;
        return true;
    }

    public double getHardware(){
        return this.hardware;
    };

    public double getWeight(){
        this.weight = this.width * this.height * this.material.LBpSQIN;
        return this.weight;
    }

    public boolean needSpecialHandling(){
        //C. Flag Tactile Panels and Raised Float Mounts for Special Handling.
        return this.specialHandling;
    }

    public boolean isCustom(){
        //B. Any item with a dimension over 44 inches requires "Custom" packaging.
        this.isCustom = (this.width > CUSTOM_THRESHOLD || this.height > CUSTOM_THRESHOLD);
        return this.isCustom;
    }

    public boolean materialContains(String string){
        return this.material.toString().contains(string);
    }

}
