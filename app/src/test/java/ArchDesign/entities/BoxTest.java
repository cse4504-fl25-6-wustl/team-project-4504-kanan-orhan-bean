package ArchDesign.entities;

import ArchDesign.entities.Box;
import ArchDesign.entities.Art;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class BoxTest {
    private Box emptyBox;
    private Box normalBox;
    private Box oversizedBox;
    private Box customBox;
    private Box fullBox;

    private Box boxWithGlassMaterial;
    private Box boxWithAcrylicMaterial;
    private Box boxWithCanvasMaterial;
    private Box boxWithAcousticMaterial;
    private Box boxWithMirrorMaterial;

    private Art normalSizedArt;
    private Art oversizedArt;
    private Art customArt;

    private final int glassAcrylicCanvasCap = 6;
    private final int acousticCap = 4;

    private static final double STANDARD_LENGTH = 37;
    private static final double STANDARD_WIDTH = 11;
    private static final double STANDARD_HEIGHT = 31;
    private static final double OVERSIZE_LENGTH = 44;
    private static final double OVERSIZE_WIDTH = 13;
    private static final double OVERSIZE_HEIGHT = 48;

    
    @Before
    public void setUp() {
        emptyBox = new Box();

        normalSizedArt = new Art(Art.Type.PaperPrintFramedWithTitlePlate, Art.Glazing.Acrylic, 1, 31.75, 36.0, 4);
        oversizedArt = new Art(Art.Type.PaperPrintFramed, Art.Glazing.Acrylic, 2, 23.75, 41.8, 4);
        customArt = new Art(Art.Type.PaperPrintFramed, Art.Glazing.Acrylic, 3, 37.8, 57.2, 4);

        normalBox = Box.createBoxForArt(normalSizedArt);
        oversizedBox = Box.createBoxForArt(oversizedArt);
        customBox = Box.createBoxForArt(customArt);

        normalBox.addArt(normalSizedArt);
        oversizedBox.addArt(oversizedArt);
        customBox.addArt(customArt);

        boxWithGlassMaterial = Box.createBoxForArt(normalSizedArt);
        boxWithGlassMaterial.addArt(new Art(Art.Type.PaperPrintFramedWithTitlePlate, Art.Glazing.Glass, 1, 20, 20, 4));
        boxWithAcrylicMaterial = Box.createBoxForArt(normalSizedArt);
        boxWithAcrylicMaterial.addArt(new Art(Art.Type.PaperPrintFramedWithTitlePlate, Art.Glazing.Acrylic, 1, 20, 20, 4));
        boxWithCanvasMaterial = Box.createBoxForArt(normalSizedArt);
        boxWithCanvasMaterial.addArt(new Art(Art.Type.PaperPrintFramedWithTitlePlate, Art.Glazing.NoGlaze, 1, 20, 20, 4));
        boxWithAcousticMaterial = Box.createBoxForArt(normalSizedArt);
        boxWithAcousticMaterial.addArt(new Art(Art.Type.AcousticPanel, Art.Glazing.Glass, 1, 20, 20, 4));
        boxWithMirrorMaterial = Box.createBoxForArt(normalSizedArt);
        boxWithMirrorMaterial.addArt(new Art(Art.Type.Mirror, Art.Glazing.Glass, 1, 20, 20, 4));

        fullBox = Box.createBoxForArt(normalSizedArt);
        for (int i=0; i<6; ++i) {
            fullBox.addArt(normalSizedArt);
        }
        // while(!fullBox.isFull()) {
        //     fullBox.addArt(normalSizedArt);
        //     System.out.println(fullBox.getArts().size());
        // }
    }

    // 1) creation tests
    // 1.1) constructor 
    @Test
    public void testBox_successfulCreation_constructor() {
        assertNotNull("instantiated Box object should not be null", emptyBox);
        assertTrue("newly created Box list of arts should be empty", emptyBox.isEmpty());
        assertFalse("newly created Box should not be full", emptyBox.isFull());
    }

    // 1.2) factory method, normal sized
    @Test
    public void testBox_successfulCreation_factoryMethods() {
        assertNotNull("normal box was null", normalBox);
        assertFalse("normal box set to oversized", normalBox.isOversized());
        assertFalse("normal box set to custom", normalBox.isCustom());
        assertNotNull("oversized box was null", oversizedBox);
        assertTrue("oversized box not set to oversized", oversizedBox.isOversized());
        assertFalse("oversized box set to custom", oversizedBox.isCustom());
        assertNotNull("custom box was null", customBox);
        assertFalse("custom box set to oversized", customBox.isOversized());
        assertTrue("custom box not set to custom", customBox.isCustom());
    }

    // 2) addition tests
    // 2.1) first box
    @Test
    public void testBox_successfulFirstAddition() {
        assertFalse("normal box should not be empty after adding art", normalBox.isEmpty());
        assertFalse("oversized box should not be empty after adding art", oversizedBox.isEmpty());
        assertFalse("custom box should not be empty after adding art", customBox.isEmpty());
    }

    // 2.2) second Box
    @Test
    public void testBox_successfulNextAddition() {
        assertEquals("next box not added to normal box", 2, normalBox.addArt(normalSizedArt).size());
        assertEquals("next box not added to oversized box", 2, oversizedBox.addArt(oversizedArt).size());
        assertEquals("next box not added to custom box", 2, customBox.addArt(customArt).size());
    }

    // 3) check capacities
    // 3.1) empty box
    @Test(expected = IllegalStateException.class)
    public void testBox_checkCapacity_emptyBox() {
        emptyBox.getCapacity();
    }

    // 3.2) glass material, normal sized
    // setCapacities method has to be fixed
    @Test
    public void testBox_checkCapacity_diffMaterials() throws Exception {
        assertEquals("wrong capacity set for box with glass materials", glassAcrylicCanvasCap, boxWithGlassMaterial.getCapacity());
        assertEquals("wrong capacity set for box with acrylic materials", glassAcrylicCanvasCap, boxWithAcrylicMaterial.getCapacity());
        assertEquals("wrong capacity set for box with canvas materials", glassAcrylicCanvasCap, boxWithCanvasMaterial.getCapacity());
        assertEquals("wrong capacity set for box with acoustic materials", acousticCap, boxWithAcousticMaterial.getCapacity());
    }

    // 3.3) mirrors
    @Test(expected = IllegalStateException.class)
    public void testBox_checkCapacity_mirrors() {
        boxWithMirrorMaterial.getCapacity();
    }

    // 4) invlaid additions
    // 4.1) art too big
    @Test
    public void testBox_invalidAddition_wrongSizeArt() {
        assertFalse("can't add oversized art to normal box", normalBox.canArtFit(oversizedArt));
        assertFalse("can't add custom art to normal box", normalBox.canArtFit(customArt));
        assertFalse("can't add custom art to oversized box", oversizedBox.canArtFit(customArt));
    }

    // 4.2) box is full
    // setCapacities method has to be fixed
    @Test
    public void testBox_invalidAddition_fullBox() throws Exception {
        assertTrue("full box should be full", fullBox.isFull());
        assertFalse("can't add to a full box", fullBox.canArtFit(normalSizedArt));
        assertEquals("list of art in box object should not exceed capacity", fullBox.getCapacity(), fullBox.addArt(normalSizedArt).size());
    }
    
    // 5) dimensions check
    // 5.1) normal box
    @Test
    public void testBox_dimensionsCheck_normalBox() {
        assertEquals("wrong length set for normal box", STANDARD_LENGTH, normalBox.getLength(), 0.00001);
        assertEquals("wrong width set for normal box", STANDARD_WIDTH, normalBox.getWidth(), 0.00001);
        assertEquals("wrong height set for normal box", STANDARD_HEIGHT, normalBox.getHeight(), 0.00001);
    }

    // 5.2) oversized box
    @Test
    public void testBox_dimensionsCheck_oversizedBox() {
        assertEquals("wrong length set for oversized box", OVERSIZE_LENGTH, oversizedBox.getLength(), 0.00001);
        assertEquals("wrong width set for oversized box", OVERSIZE_WIDTH, oversizedBox.getWidth(), 0.00001);
        assertEquals("wrong height set for oversized box", OVERSIZE_HEIGHT, oversizedBox.getHeight(), 0.00001);
    }
}
