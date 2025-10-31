package ArchDesign.entities;

public class Art {
    
    // TODO: Figure out how to handle the Type, Material, and Glazing Enums depending on https://piazza.com/class/mf6woqytzb81zq/post/14
    public enum Type { 
        AcousticPanelGalleryWrapped(2.5, Art.Material.AcousticPanel, false, true),
        TwoInchAcousticPanelFloatFrame(2.5, Art.Material.AcousticPanelFramed, false, true),
        OneInchAcousticPanelFloatFrame(2.5, Art.Material.AcousticPanelFramed, false, true),
        VECanvasFloatFrame(2.5, Art.Material.CanvasFramed, false, true),
        UVCanvasGalleryWrapped(2.5, Art.Material.CanvasGallery),
        UVCanvasFramed(2.5, Art.Material.CanvasFramed),
        UVCanvasFloatFrame(2.5, Art.Material.CanvasFramed, false, true),
        HandEmbellishedCanvasGalleryWrapped(2.5, Art.Material.CanvasGallery),
        HandEmbellishedCanvasFramed(2.5, Art.Material.CanvasFramed),
        HandEmbellishedCanvasFloatFrame(2.5, Art.Material.CanvasFramed, false, true),
        CanvasGalleryWrapped(2.5, Art.Material.CanvasGallery),
        CanvasFramed(2.5, Art.Material.CanvasFramed),
        CanvasFloatFrame(2.5, Art.Material.CanvasFramed, false, true),
        PrintRaisedMat(1.0, Art.Material.KNOWNBUTGLAZING, true),
        PrintRaisedFloatMountwithTitlePlate(0.625, Art.Material.KNOWNBUTGLAZING, true),
        PrintRaisedFloatMountandRaisedMat(1.0, Art.Material.KNOWNBUTGLAZING, true),
        PrintRaisedFloatMountandDeckledEdge(0.625, Art.Material.KNOWNBUTGLAZING, true),
        PrintRaisedFloatMount(0.625, Art.Material.KNOWNBUTGLAZING, true),
        PrintFramedwithTitlePlate(1.83, Art.Material.KNOWNBUTGLAZING),
        PrintFloatMountwithTitlePlate(0.75, Art.Material.KNOWNBUTGLAZING, true),
        PrintFloatMountandDeckledEdge(0.75, Art.Material.KNOWNBUTGLAZING, true),
        PrintFloatMount(0.75, Art.Material.KNOWNBUTGLAZING, true),
        PaperPrintFramed(1.83, Art.Material.KNOWNBUTGLAZING),
        MirrorBeveled(1.83, Art.Material.Mirror),
        Mirror(1.83, Art.Material.Mirror),
        Signage(0.0, Art.Material.Signage, true),
        WoodPrint(1.83, Art.Material.SolidSurface),
        MetalPrint(1.83, Art.Material.SolidSurface),
        AcrylicwithStandoffsSquared(1.83, Art.Material.SolidSurface),
        AcrylicwithStandoffsRounded(1.83, Art.Material.SolidSurface),
        AcrylicFloatingSquared(1.83, Art.Material.SolidSurface),
        AcrylicFloatingRounded(1.83, Art.Material.SolidSurface),
        SintraFramed(1.83, Art.Material.SolidSurface),
        PatientBoard(1.83, Art.Material.PatientBoard),
        Whiteboard(1.83, Art.Material.Board),
        MagneticBoardFabricWrapped(1.83, Art.Material.Board),
        CorkBoardLinenWrapped(1.83, Art.Material.Board),
        CorkBoard(1.83, Art.Material.Board),
        Chalkboard(1.83, Art.Material.Board),
        TactilePanelFlat(1.83, Art.Material.FramedItemDeep),
        TactilePanelDimensional(8.0, Art.Material.FramedItemDeep, true),
        Shadowbox(3.0, Art.Material.FramedItemDeep),
        ObjectFramed(3.0, Art.Material.FramedItemDeep),
        HardwareOnly(0.0, Art.Material.Hardware, true),
        HangingSystem(0.0, Art.Material.Hardware, true),
        SpecialtyProduct(0.0, Art.Material.Misc, true),
        SpecialtyFramingItem(0.0, Art.Material.Misc, true),
        Commission(0.0, Art.Material.Misc, true),
        PrintServices(0.0, Art.Material.Misc, true),
        PlaqueOnly(0.0, Art.Material.Plaque, true),
        WallDecor(0.0, Art.Material.Misc, true),
        WallpaperCovering(0.0, Art.Material.Misc, true),
        VinylWindowFilm(0.0, Art.Material.RolledProduct, true),
        VinylWallGraphic(0.0, Art.Material.RolledProduct, true),
        UNKNOWN(0.0, Art.Material.RolledProduct, true);
        // PaperPrintFramed(1.83), 
        // PaperPrintFramedWithTitlePlate(1.83), 
        // CanvasGalleryWrapped(2.75),
        // CanvasFramed(2.75),
        // CanvasFloatFrame(2.75), 
        // WallDecor(0.0), 
        // AcousticPanel(2.50), 
        // AcousticPanelFramed(2.50), 
        // MetalPrint(1.83), 
        // Mirror(1.83),
        // UNKNOWN(0.0);

        public final double depth;
        public final boolean requiresCustom;
        public final boolean requiresSpecialHandling;
        public final Material preliminaryMaterial;

        private Type(double depth, Material preliminaryMaterial) {
            this.depth = depth;
            this.preliminaryMaterial = preliminaryMaterial;
            this.requiresCustom = false;
            this.requiresSpecialHandling = false;
        }

        private Type(double depth, Material preliminaryMaterial, boolean requiresCustom) {
            this.depth = depth;
            this.preliminaryMaterial = preliminaryMaterial;
            this.requiresCustom = requiresCustom;
            this.requiresSpecialHandling = false;
        }

        private Type(double depth, Material preliminaryMaterial, boolean requiresCustom, boolean requiresSpecialHandling) {
            this.depth = depth;
            this.preliminaryMaterial = preliminaryMaterial;
            this.requiresCustom = requiresCustom;
            this.requiresSpecialHandling = requiresSpecialHandling;
        }
    }
    public enum Material {
        // TODO: Find the LBpSQIN for all Materials listed as 0.0
        AcousticPanel(0.0038),
        AcousticPanelFramed(0.0037),
        CanvasFramed(0.0085),
        CanvasGallery(0.0061),
        Glass(0.0098),
        Acyrlic(0.0094),
        Mirror(0.0191),
        PatientBoard(0.0347),
        Board(0.0),
        Signage(0.0),
        SolidSurface(0.0),
        FramedItemDeep(0.0),
        Hardware(0.0),
        Misc(0.0),
        Plaque(0.0),
        RolledProduct(0.0),
        KNOWNBUTGLAZING(0.0),
        UNKNOWN(0.0);
        // Glass(0.0098), 
        // Acyrlic(0.0094), 
        // CanvasFramed(0.0085), 
        // CanvasGallery(0.0061), 
        // Mirror(0.0191), 
        // AcousticPanel(0.0038), 
        // AcousticPanelFramed(0.0037), 
        // PatientBoard(0.0347),
        // UNKNOWN(0.0);

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
        this.material = setMaterial();
        this.specialHandling = setSpecialHandling();
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

    private boolean setSpecialHandling(){
        this.specialHandling = this.getType().requiresSpecialHandling;
        return this.specialHandling;
    }

    private double setDepth(){
        this.depth = this.getType().depth;
        return this.depth;
    }

    private Material setMaterial(){
        // TODO: waiting on https://piazza.com/class/mf6woqytzb81zq/post/14 will Fix later
        if (this.getType().preliminaryMaterial == Art.Material.UNKNOWN){
            throw new IllegalArgumentException("Unknown type: " + type);
        }
        else if (this.getType().preliminaryMaterial == Art.Material.KNOWNBUTGLAZING){
            switch (this.glazing) {
                case Glass:
                    this.material = Art.Material.Glass;
                    return Material.Glass;
                case Acrylic:
                    this.material = Art.Material.Glass;
                    return Material.Acyrlic;
                default:
                    throw new IllegalArgumentException("Unkown Glazing for Paper Print: " + this.glazing);
            }
        }
        else {
            this.material = this.getType().preliminaryMaterial;
            return this.material;
        }
    }

    // ----- Static Helpers (BEAN) ----- 

    // helper method for setting type
    public static Art.Type assignType(String typeStr) {
        if (typeStr != null) {
            typeStr.replaceAll("-", "");
            typeStr = typeStr.toLowerCase();
            if (typeStr.contains("acoustic")){
                if(typeStr.contains("float")){
                    if(typeStr.contains("two") || typeStr.contains("2")){
                        return Art.Type.TwoInchAcousticPanelFloatFrame;
                    }
                    else {
                        return Art.Type.OneInchAcousticPanelFloatFrame;
                    }
                }
                else {
                    return Art.Type.AcousticPanelGalleryWrapped;
                }
            }
            else if (typeStr.contains("canvas")){
                if (typeStr.contains("handembellished")){
                    if (typeStr.contains("gallery")){
                        return Art.Type.HandEmbellishedCanvasGalleryWrapped;
                    }
                    else if (typeStr.contains("float")){
                        return Art.Type.HandEmbellishedCanvasFloatFrame;
                    }
                    else{
                        return Art.Type.HandEmbellishedCanvasFramed;
                    }
                }
                else if (typeStr.contains("uvcanvas")){
                    if (typeStr.contains("gallery")){
                        return Art.Type.UVCanvasGalleryWrapped;
                    }
                    else if (typeStr.contains("float")){
                        return Art.Type.UVCanvasFloatFrame;
                    }
                    else{
                        return Art.Type.UVCanvasFramed;
                    }
                }
                else if (typeStr.contains("vecanvas")){
                    return Art.Type.VECanvasFloatFrame;
                }
                else {
                    if (typeStr.contains("gallery")){
                        return Art.Type.CanvasGalleryWrapped;
                    }
                    else if (typeStr.contains("float")){
                        return Art.Type.CanvasFloatFrame;
                    }
                    else{
                        return Art.Type.CanvasFramed;
                    }
                }
            }
            else if (typeStr.contains("print")){
                if (typeStr.contains("raised")){
                    if (typeStr.contains("mat")){
                        if (typeStr.contains("floatmount")){
                            return Art.Type.PrintRaisedFloatMountandRaisedMat;
                        }
                        else{
                            return Art.Type.PrintRaisedMat;
                        }
                    }
                    else if (typeStr.contains("titleplate")){
                        return Art.Type.PrintRaisedFloatMountwithTitlePlate;
                    }
                    else if (typeStr.contains("decklededge")){
                        return Art.Type.PrintRaisedFloatMountandDeckledEdge;
                    }
                    else {
                        return Art.Type.PrintRaisedFloatMount;
                    }
                }
                else if (typeStr.contains("framed")){
                    if (typeStr.contains("titleplate")){
                        return Art.Type.PrintFramedwithTitlePlate;
                    }
                    else{
                        return Art.Type.PaperPrintFramed;
                    }
                }
                else if (typeStr.contains("printfloat")){
                    if (typeStr.contains("titleplate")){
                        return Art.Type.PrintFloatMountwithTitlePlate;
                    }
                    else if (typeStr.contains("decklededge")){
                        return Art.Type.PrintFloatMountandDeckledEdge;
                    }
                    else {
                        return Art.Type.PrintFloatMount;
                    }
                }
                else if (typeStr.contains("woodprint")){
                    return Art.Type.WoodPrint;
                }
                else if (typeStr.contains("metalprint")){
                    return Art.Type.MetalPrint;
                }
                else if (typeStr.contains("printservices")){
                    return Art.Type.PrintServices;
                }
            }
            else if (typeStr.contains("mirror")){
                if (typeStr.contains("beveled")){
                    return Art.Type.MirrorBeveled;
                }
                else {
                    return Art.Type.Mirror;
                }
            }
            else if (typeStr.contains("signage")){
                return Art.Type.Signage;
            }
            else if (typeStr.contains("acrylic")){
                if (typeStr.contains("floating")){
                    if (typeStr.contains("squared")){
                        return Art.Type.AcrylicFloatingSquared;
                    }
                    else {
                        return Art.Type.AcrylicFloatingRounded;
                    }
                }
                else {
                    if (typeStr.contains("squared")){
                        return Art.Type.AcrylicwithStandoffsSquared;
                    }
                    else {
                        return Art.Type.AcrylicwithStandoffsRounded;
                    }
                }
            }
            else if (typeStr.contains("sintraframed")){
                return Art.Type.SintraFramed;
            }
            else if (typeStr.contains("patientboard")){
                return Art.Type.PatientBoard;
            }
            else if (typeStr.contains("whiteboard")){
                return Art.Type.Whiteboard;
            }
            else if (typeStr.contains("magneticboard")){
                return Art.Type.MagneticBoardFabricWrapped;
            }
            else if (typeStr.contains("corkboardlinenwrapped")){
                return Art.Type.CorkBoardLinenWrapped;
            }
            else if (typeStr.contains("corkboard")){
                return Art.Type.CorkBoard;
            }
            else if (typeStr.contains("chalkboard")){
                return Art.Type.Chalkboard;
            }
            else if (typeStr.contains("tactilepanelflat")){
                return Art.Type.TactilePanelFlat;
            }
            else if (typeStr.contains("tactilepaneldimensional")){
                return Art.Type.TactilePanelDimensional;
            }
            else if (typeStr.contains("tactilepaneldimensional")){
                return Art.Type.Shadowbox;
            }
            else if (typeStr.contains("tactilepaneldimensional")){
                return Art.Type.ObjectFramed;
            }
            else if (typeStr.contains("tactilepaneldimensional")){
                return Art.Type.HardwareOnly;
            }
            else if (typeStr.contains("tactilepaneldimensional")){
                return Art.Type.HangingSystem;
            }
            else if (typeStr.contains("tactilepaneldimensional")){
                return Art.Type.SpecialtyProduct;
            }
            else if (typeStr.contains("tactilepaneldimensional")){
                return Art.Type.SpecialtyFramingItem;
            }
            else if (typeStr.contains("tactilepaneldimensional")){
                return Art.Type.Commission;
            }
            else if (typeStr.contains("tactilepaneldimensional")){
                return Art.Type.PlaqueOnly;
            }
            else if (typeStr.contains("tactilepaneldimensional")){
                return Art.Type.WallDecor;
            }
            else if (typeStr.contains("tactilepaneldimensional")){
                return Art.Type.WallpaperCovering;
            }
            else if (typeStr.contains("tactilepaneldimensional")){
                return Art.Type.VinylWindowFilm;
            }
            else if (typeStr.contains("tactilepaneldimensional")){
                return Art.Type.VinylWallGraphic;
            }
            // if (typeStr.contains("print")) {
            //     if (typeStr.contains("metal")) {
            //         return Art.Type.MetalPrint;
            //     }
            //     else if (typeStr.contains("title")) {
            //         return Art.Type.PaperPrintFramedWithTitlePlate;
            //     }
            //     else { return Art.Type.PaperPrintFramed; }
            // }
            // else if (typeStr.contains("canvas")) {
            //     return Art.Type.CanvasFloatFrame;
            // }
            // else if (typeStr.contains("wall")) {
            //     return Art.Type.WallDecor;
            // }
            // else if (typeStr.contains("acoustic")) {
            //     if (typeStr.contains("framed")) {
            //         return Art.Type.AcousticPanelFramed;
            //     }
            //     else { return Art.Type.AcousticPanel; }
            // }
            // else if (typeStr.contains("metal")) {
            //     return Art.Type.MetalPrint;
            // }
            // else if (typeStr.contains("mirror")) {
            //     return Art.Type.Mirror;
            // }
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
