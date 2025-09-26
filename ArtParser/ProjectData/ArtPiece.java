/**
 * ArtPiece.java
 *
 * an indivdual art piece from a project work order, with all relevant input data
 */

package ProjectData;

// similar to client, should we make instantiation protected?
public class ArtPiece {
    private final int lineNumber; // object key
    private int quantity;
    private int tagNumber;
    private String finalMedium;
    private double width;
    private double height;
    private ArtPiece.GlazingType glazing;
    private String frameMoulding;
    private String hardware;

        // enum for glazing
    public static enum GlazingType {
        REGULAR_GLASS,
        ACRYLIC,
        NONE
    }

    public ArtPiece(int lineNumber, int quantity, int tagNumber, String finalMedium,
                    double width, double height, ArtPiece.GlazingType glazing,
                    String frameMoulding, String hardware) {
        this.lineNumber = lineNumber;
        this.quantity = quantity;
        this.tagNumber = tagNumber;
        this.finalMedium = finalMedium;
        this.width = width;
        this.height = height;
        this.glazing = glazing;
        this.frameMoulding = frameMoulding;
        this.hardware = hardware;
    }

    // Getters
    public int getLineNumber() { return lineNumber; }
    public int getQuantity() { return quantity; }
    public int getTagNumber() { return tagNumber; }
    public String getFinalMedium() { return finalMedium; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public ArtPiece.GlazingType getGlazing() { return glazing; }
    public String getFrameMoulding() { return frameMoulding; }
    public String getHardware() { return hardware; }

    // Setters
    // public void setLineNumber(int lineNumber) { this.lineNumber = lineNumber; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setTagNumber(int tagNumber) { this.tagNumber = tagNumber; }
    public void setFinalMedium(String finalMedium) { this.finalMedium = finalMedium; }
    public void setWidth(double width) { this.width = width; }
    public void setHeight(double height) { this.height = height; }
    public void setGlazing(ArtPiece.GlazingType glazing) { this.glazing = glazing; }
    public void setFrameMoulding(String frameMoulding) { this.frameMoulding = frameMoulding; }
    public void setHardware(String hardware) { this.hardware = hardware; }
}