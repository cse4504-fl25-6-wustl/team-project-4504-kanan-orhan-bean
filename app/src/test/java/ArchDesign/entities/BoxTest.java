package ArchDesign.entities;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class BoxTest {
    private Box emptyBox;
    private Box normalBox;
    private Box oversizedBox;
    private Box customBox;
    private Box fullBox;

    private Art normalSizedArt;
    private Art oversizedArt;
    private Art customArt;

    
    @Before
    public void setUp() {
        emptyBox = new Box();

        normalSizedArt = new Art(Art.Type.PaperPrintFramed, Art.Glazing.Acrylic, 1, 31.75, 36.0, 4);
        oversizedArt = new Art(Art.Type.PaperPrintFramed, Art.Glazing.Acrylic, 2, 37.50, 41.8, 4);
        customArt = new Art(Art.Type.PaperPrintFramed, Art.Glazing.Acrylic, 3, 57.2, 57.2, 4);

        normalBox = Box.createBoxForArt(normalSizedArt);
        oversizedBox = Box.createBoxForArt(oversizedArt);
        customBox = Box.createBoxForArt(customArt);

        normalBox.addArt(normalSizedArt);
        oversizedBox.addArt(oversizedArt);
        customBox.addArt(customArt);

        fullBox = Box.createBoxForArt(normalSizedArt);
        boolean canAdd = true;
        while (canAdd) {
            canAdd = fullBox.addArt(normalSizedArt);
        }
    }

    // 1) creation tests
    // 1.1) constructor 
    @Test
    public void testBox_successfulCreation_constructor() {
        assertNotNull("instantiated Box object should not be null", emptyBox);
        assertTrue("newly created Box list of arts should be empty", emptyBox.isEmpty());
        assertFalse("newly created Box should not be full", emptyBox.isFull());
        assertEquals("newly created box should have no weight", 0, emptyBox.getWeight());
    }

    // 1.2) factory method, normal sized
    @Test
    public void testBox_successfulCreation_factoryMethods_normal() {
        assertNotNull("normal box was null", normalBox);
        assertTrue("normal box not set to normal", normalBox.isNormal());
        assertFalse("normal box set to oversized", normalBox.isOversized());
        assertFalse("normal box set to custom", normalBox.isCustom());
    }
    // 1.3) factory method, oversized
    @Test
    public void testBox_successfulCreation_factoryMethods_oversized() {
        assertNotNull("oversized box was null", oversizedBox);
        assertFalse("oversized box set to normal", oversizedBox.isNormal());
        assertTrue("oversized box not set to oversized", oversizedBox.isOversized());
        assertFalse("oversized box set to custom", oversizedBox.isCustom());
    }
    // 1.4) factory method, custom sized
    @Test
    public void testBox_successfulCreation_factoryMethods_custom() {
        assertNotNull("custom box was null", customBox);
        assertFalse("custom box set to normal", customBox.isNormal());
        assertFalse("custom box set to oversized", customBox.isOversized());
        assertTrue("custom box not set to custom", customBox.isCustom());
    }

    // 2) addition tests
    // 2.1) first art
    @Test
    public void testBox_successfulFirstAddition() {
        assertFalse("normal art should not be empty after adding art", normalBox.isEmpty());
        assertFalse("oversized art should not be empty after adding art", oversizedBox.isEmpty());
        assertTrue("custom art should always be empty", customBox.isEmpty());
    }

    // 2.2) second art
    @Test
    public void testBox_successfulNextAddition() {
        assertTrue("next art not added to normal box", normalBox.addArt(normalSizedArt));
        assertTrue("next art not added to oversized box", oversizedBox.addArt(oversizedArt));
        assertFalse("next art added to custom box", customBox.addArt(customArt));
    }
    
    // second art, different materials
    // 2.3) 
    @Test
    public void testBox_successfulNextAddition_differentMaterials_PPFWTP() {
        assertTrue("next art of different material not added to normal box, PPFWTP", normalBox.addArt(new Art(Art.Type.PrintFloatMountwithTitlePlate, Art.Glazing.Glass, 4, 20, 20, 4)));
        assertTrue("next art of different material not added to oversized box, PPFWTP", oversizedBox.addArt(new Art(Art.Type.PrintFloatMountwithTitlePlate, Art.Glazing.Glass, 4, 20, 20, 4)));
    }
    // 2.4)
    @Test
    public void testBox_successfulNextAddition_differentMaterials_canvas() {
        assertTrue("next art of different material not added to normal box, canvas", normalBox.addArt(new Art(Art.Type.CanvasFloatFrame, Art.Glazing.NoGlaze, 4, 20, 20, 4)));
        assertTrue("next art of different material not added to oversized box, canvas", oversizedBox.addArt(new Art(Art.Type.CanvasFloatFrame, Art.Glazing.NoGlaze, 4, 20, 20, 4)));
    }
    // 2.5)
    @Test
    public void testBox_successfulNextAddition_differentMaterials_AP() {
        assertTrue("next art of different material not added to normal box, acoustic panel", normalBox.addArt(new Art(Art.Type.AcousticPanelGalleryWrapped, Art.Glazing.NoGlaze, 4, 20, 20, 4)));
        assertTrue("next art of different material not added to oversized box, acoustic panel", oversizedBox.addArt(new Art(Art.Type.AcousticPanelGalleryWrapped, Art.Glazing.NoGlaze, 4, 20, 20, 4)));
    }

    // TODO: ADD CHECK THAT YOU CANT ADD MIRRORS

    // 3) check capacities
    // 3.1) empty box
    @Test
    public void testBox_checkCapacity_emptyBox() {
        assertEquals("Empty unassigned box should have no capacity", 0, emptyBox.getCapacity(), 0.000001);
    }

    // 3.2) after 1 addition, normal sized
    @Test
    public void testBox_checkCapacity_normal() {
        double expected = normalBox.getWidth() - (normalSizedArt.getDepth());
        assertEquals("wrong remaining capacity calculated for normal box after 1st addition", expected, normalBox.getCapacity(), 0.00001);
    }

    // 3.3) after 1 addition, normal sized
    @Test
    public void testBox_checkCapacity_oversized() {
        double expected = 11 - (oversizedArt.getDepth());
        assertEquals("wrong remaining capacity calculated for oversized box after 1st addition", expected, oversizedBox.getCapacity(), 0.00001);
    }

    // 3.4) custom
    @Test
    public void testBox_checkCapacity_custom() {
        assertEquals("custom box capacity should always be 0", 0, customBox.getCapacity(), 0.00001);
    }

    // 3.5)) full box
    @Test
    public void testBox_checkCapacity_fullBox() {
        assertTrue("wrong remaining capacity calculated for full box " + fullBox.getCapacity() + " " + normalSizedArt.getDepth(), fullBox.getCapacity() <= normalSizedArt.getDepth());
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
    @Test
    public void testBox_invalidAddition_fullBox() {
        assertTrue("full box should be full", fullBox.isFull());
        assertFalse("can't add to a full box", fullBox.canArtFit(normalSizedArt));
        double totalSize = fullBox.getArts().size() * normalSizedArt.getDepth();
        assertTrue("total size of arts in box object should not exceed width", fullBox.getWidth() >= totalSize);
    }
    
    // 5) dimensions check
    // 5.1) normal box
    @Test
    public void testBox_dimensionsCheck_normalBox() {
        assertEquals("wrong length set for normal box", Box.STANDARD_LENGTH, normalBox.getLength(), 0.00001);
        assertEquals("wrong width set for normal box", Box.STANDARD_WIDTH, normalBox.getWidth(), 0.00001);
        assertEquals("wrong height set for normal box", Box.STANDARD_HEIGHT, normalBox.getHeight(), 0.00001);
    }

    // 5.2) oversized box
    @Test
    public void testBox_dimensionsCheck_oversizedBox() {
        assertEquals("wrong length set for oversized box", Box.OVERSIZE_LENGTH, oversizedBox.getLength(), 0.00001);
        assertEquals("wrong width set for oversized box", Box.OVERSIZE_WIDTH, oversizedBox.getWidth(), 0.00001);
        assertEquals("wrong height set for oversized box", Box.OVERSIZE_HEIGHT, oversizedBox.getHeight(), 0.00001);
    }

    // 5.2) custom box
    @Test
    public void testBox_dimensionsCheck_customBox() {
        assertEquals("wrong length set for custom box", 0, customBox.getLength(), 0.00001);
        assertEquals("wrong width set for custom box", 0, customBox.getWidth(), 0.00001);
        assertEquals("wrong height set for custom box", 0, customBox.getHeight(), 0.00001);
    }
}