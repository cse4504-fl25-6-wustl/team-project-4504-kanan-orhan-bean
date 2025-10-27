package ArchDesign.entities;

public class Art {
    
    // TODO: Figure out how to handle the Type, Material, and Glazing Enums depending on https://piazza.com/class/mf6woqytzb81zq/post/14
    public enum Type { 
        // AcousticPanelGalleryWrapped,
        // TwoInchAcousticPanelFloatFrame,
        // OneInchAcousticPanelFloatFrame,
        // VECanvasFloatFrame,
        // UVCanvasGalleryWrapped,
        // UVCanvasFramed,
        // UVCanvasFloatFrame,
        // HandEmbellishedCanvasGalleryWrapped,
        // HandEmbellishedCanvasFramed,
        // HandEmbellishedCanvasFloatFrame,
        // CanvasGalleryWrapped,
        // CanvasFramed,
        // CanvasFloatFrame,
        // PrintRaisedMat,
        // PrintRaisedFloatMountwithTitlePlate,
        // PrintRaisedFloatMountandRaisedMat,
        // PrintRaisedFloatMountandDeckledEdge,
        // PrintRaisedFloatMount,
        // PrintFramedwithTitlePlate,
        // PrintFloatMountwithTitlePlate,
        // PrintFloatMountandDeckledEdge,
        // PrintFloatMount,
        // PaperPrintFramed,
        // MirrorBeveled,
        // Mirror,
        // Signage,
        // WoodPrint,
        // MetalPrint,
        // AcrylicwithStandoffsSquared,
        // AcrylicwithStandoffsRounded,
        // AcrylicFloatingSquared,
        // AcrylicFloatingRounded,
        // SintraFramed,
        // PatientBoard,
        // Whiteboard,
        // MagneticBoardFabricWrapped,
        // CorkBoardLinenWrapped,
        // CorkBoard,
        // Chalkboard,
        // TactilePanelFlat,
        // TactilePanelDimensional,
        // Shadowbox,
        // ObjectFramed,
        // HardwareOnly,
        // HangingSystem,
        // SpecialtyProduct,
        // SpecialtyFramingItem,
        // Commission,
        // PrintServices,
        // PlaqueOnly,
        // WallDécor,
        // WallpaperCovering,
        // VinylWindowFilm,
        // VinylWallGraphic,
        // UNKNOWN
        PaperPrintFramed(1.83), 
        PaperPrintFramedWithTitlePlate(1.83), 
        CanvasGalleryWrapped(2.75),
        CanvasFramed(2.75),
        CanvasFloatFrame(2.75), 
        WallDecor(0.0), 
        AcousticPanel(2.50), 
        AcousticPanelFramed(2.50), 
        MetalPrint(1.83), 
        Mirror(1.83),
        UNKNOWN(0.0);

        public final double depth;

        private Type(double depth) {
            this.depth = depth;
        }
    }
    public enum Material {
        Glass(0.0098), 
        Acyrlic(0.0094), 
        CanvasFramed(0.0085), 
        CanvasGallery(0.0061), 
        Mirror(0.0191), 
        AcousticPanel(0.0038), 
        AcousticPanelFramed(0.0037), 
        PatientBoard(0.0347),
        // Signage(0.0),
        // SolidSurface(0.0),
        // FramedItemDeep(0.0),
        // RolledProduct(0.0),
        // Plaque(0.0),
        UNKNOWN(0.0);

        public final double LBpSQIN;

        private Material(double LBpSQIN) {
            this.LBpSQIN = LBpSQIN;
        }
    }
    public enum Glazing { Glass, Acrylic, NoGlaze, UNKNOWN }
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
    private boolean isOversized;

    protected final double CUSTOM_THRESHOLD = 43.5;
    protected final double IS_OVERSIZE_THRESHOLD = 44;

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
        this.isOversized = setIsOversized(width, height);
        this.isCustom = setIsCustom(width, height);
        this.depth = setDepth();
    }

    public boolean materialContains(Material material){
        return this.getMaterial() == material;
    }

    public boolean materialContains(String string){
        return this.material.toString().contains(string);
    }

    // -------- getters and setters --------

    public Material getMaterial(){
        return this.material;
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

    public int getHardware(){
        return this.hardware;
    };

    public int getWeight(){
        this.weight = this.width * this.height * this.material.LBpSQIN;
        return (int) Math.ceil(this.weight); 
    }

    public boolean needSpecialHandling(){
        //C. Flag Tactile Panels and Raised Float Mounts for Special Handling.
        return this.specialHandling;
    }

    public boolean isCustom(){
        //B. Any item with a dimension over 44 inches requires "Custom" packaging.
        return this.isCustom;
    }

    public boolean isOversized(){
        return this.isOversized;
    }

    // ---------------- helpers ----------------

    private boolean setIsCustom(double width, double height){
        if (width > CUSTOM_THRESHOLD && this.height > CUSTOM_THRESHOLD){
            return true;
        }
        return false;
    }

    private boolean setIsOversized(double width, double height){
        if (width > IS_OVERSIZE_THRESHOLD || height > IS_OVERSIZE_THRESHOLD){
            return true;
        }
        return false;
    }

    private boolean setSpecialHandling(Type type){
        this.specialHandling = false;
        if (this.getType() == Type.AcousticPanel || this.getType() == Type.AcousticPanelFramed 
        || this.getType() == Type.CanvasFloatFrame){
            this.specialHandling = true;
        }
        return this.specialHandling;
    }

    private double setDepth(){
        this.depth = this.getType().depth;
        return this.depth;
    }

    private Material setMaterial(Type type, Glazing glazing){
        // TODO: waiting on https://piazza.com/class/mf6woqytzb81zq/post/14 will Fix later
        switch (type) {
            case PaperPrintFramed:
            case PaperPrintFramedWithTitlePlate:
                switch (glazing) {
                    case Glass:
                        return Material.Glass;
                    case Acrylic:
                        return Material.Acyrlic;
                    case NoGlaze:
                        return Material.CanvasFramed;
                }
                break;
    
            case CanvasFloatFrame:
                return Material.CanvasFramed;
    
            case WallDecor:
                return Material.CanvasGallery;
    
            case AcousticPanel:
                return Material.AcousticPanel;
    
            case AcousticPanelFramed:
                return Material.AcousticPanelFramed;
    
            case MetalPrint:
                return Material.PatientBoard;
    
            case Mirror:
                return Material.Mirror;
    
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
        throw new IllegalArgumentException("Unsupported glazing for type: " + type);
    }

    // ----- Static Helpers (BEAN) ----- 

    // helper method for setting type
    public static Art.Type assignType(String typeStr) {
        // TODO: Update
        if (typeStr != null) {
            typeStr = typeStr.toLowerCase();
            if (typeStr.contains("print")) {
                if (typeStr.contains("metal")) {
                    return Art.Type.MetalPrint;
                }
                else if (typeStr.contains("title")) {
                    return Art.Type.PaperPrintFramedWithTitlePlate;
                }
                else { return Art.Type.PaperPrintFramed; }
            }
            else if (typeStr.contains("canvas")) {
                return Art.Type.CanvasFloatFrame;
            }
            else if (typeStr.contains("wall")) {
                return Art.Type.WallDecor;
            }
            else if (typeStr.contains("acoustic")) {
                if (typeStr.contains("framed")) {
                    return Art.Type.AcousticPanelFramed;
                }
                else { return Art.Type.AcousticPanel; }
            }
            else if (typeStr.contains("metal")) {
                return Art.Type.MetalPrint;
            }
            else if (typeStr.contains("mirror")) {
                return Art.Type.Mirror;
            }
        }
        return Art.Type.UNKNOWN;
    }

    // helper method for setting glaze
    public static Art.Glazing assignGlazingType(String glazingStr) {
        if (glazingStr != null) {
            glazingStr = glazingStr.toLowerCase();
            if (glazingStr.contains("glass")) {
                return Art.Glazing.Glass;
            } else if (glazingStr.contains("acrylic")) {
                return Art.Glazing.Acrylic;
            } else if (glazingStr.contains("glaze")) {
                return Art.Glazing.NoGlaze;
            }
        }
        return Art.Glazing.UNKNOWN;
    }
    // helper method for setting hardware number
    public static int assignHardware(String hardwareStr) {
        if (hardwareStr == null || hardwareStr.isEmpty()) {
            return -1;
        }

        // Split on whitespace to isolate the first word
        String firstWord = hardwareStr.trim().split("\\s+")[0];
        // Keep only digits from the first word
        String digits = firstWord.replaceAll("\\D+", ""); // \D = non-digit
        if (digits.isEmpty()) {
            return -1;
        }

        return Integer.parseInt(digits);
    }
}
