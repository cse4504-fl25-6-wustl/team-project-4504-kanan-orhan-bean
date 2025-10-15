package ArchDesign.entities;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ArchDesign.entities.Art.Glazing;
import ArchDesign.entities.Art.Type;

public class ContainerTest {
    protected Container nullContainer;
    protected Container palletContainer;
    protected Container crateContainer;
    protected Container glassContainer;
    protected Container oversizeContainer;
    protected Container customContainer;
    protected Container nullContainerNoCrate;
    protected Container palletContainerNoCrate;
    protected Container glassContainerNoCrate;
    protected Container oversizeContainerNoCrate;
    protected Container customContainerNoCrate;
    protected Art mirrorArt;
    protected Art nonMirrorArt;
    protected Box standardBox;
    protected Box oversizeBox;
    protected Box customSmallBox;
    protected Box customLargeBox;

    protected static final double tooSmallWidth = 34.5;
    protected static final double standardWidth = 36.5;
    protected static final double oversizeWidth = 44;
    protected static final double tooLargeWidth = 45;

    // From Container.java
    protected final double STANDARD_PALLET_LENGTH = 48;
    protected final double STANDARD_PALLET_WIDTH = 40;
    protected final double GLASS_PALLET_LENGTH = 43;
    protected final double GLASS_PALLET_WIDTH = 35;
    protected final double OVERSIZE_PALLET_LENGTH = 60;
    protected final double OVERSIZE_PALLET_WIDTH = 40;
    protected final double CRATE_LENGTH = 50;
    protected final double CRATE_WIDTH = 38;
    protected final double CRATE_HEIGHT_OVERHEAD = 8;
    protected final double OVERHEAD_PALLET_WEIGHT = 60;
    protected final double OVERHEAD_OVERSIZE_PALLET_WEIGHT = 75;
    protected final double OVERHEAD_CRATE_WEIGHT = 125;
    protected final int MIRROR_CRATE_LIMIT = 24;
    protected final int NORMAL_GLASS_ACRYLIC_CRATE_LIMIT = 25;
    protected final int OVERSIZE_GLASS_ACRYLIC_CRATE_LIMIT = 18;
    protected final int NORMAL_CANVAS_CRATE_LIMIT = 18;
    protected final int OVERSIZE_CANVAS_CRATE_LIMIT = 12;
    protected final int CUSTOM_BOX_DIMENSION_LIMIT = 33;

    // TODO: Figure out where to use these
    protected final double OVERSIZE_CRATE_LIMIT = 46;
    protected final double CRATE_RECOMMENDED_HEIGHT_LIMIT = 84;
    protected final double CRATE_ABSOLUTE_HEIGHT_LIMIT = 102;

    @Before
    public void setUp(){
        nullContainer = new Container(null, true);
        palletContainer = new Container(ArchDesign.entities.Container.Type.Pallet, true);
        crateContainer = new Container(ArchDesign.entities.Container.Type.Crate, true);
        glassContainer = new Container(ArchDesign.entities.Container.Type.Glass, true);
        oversizeContainer = new Container(ArchDesign.entities.Container.Type.Oversize, true);
        customContainer = new Container(ArchDesign.entities.Container.Type.Custom, true);
        nullContainerNoCrate = new Container(null, false);
        palletContainerNoCrate = new Container(ArchDesign.entities.Container.Type.Pallet, false);
        glassContainerNoCrate = new Container(ArchDesign.entities.Container.Type.Glass, false);
        oversizeContainerNoCrate = new Container(ArchDesign.entities.Container.Type.Oversize, false);
        customContainerNoCrate = new Container(ArchDesign.entities.Container.Type.Custom, false);
        mirrorArt = new Art(Type.Mirror, Glazing.NoGlaze, 1, standardWidth, standardWidth, 3);
        nonMirrorArt = new Art(Type.PaperPrintFramed, Glazing.Glass, 2, standardWidth, standardWidth, 3);
        standardBox = new Box();
        oversizeBox = new Box();
        customSmallBox = new Box();
        customLargeBox = new Box();
    }

    // ---------------- testing constructContainerForArt ----------------
    // Since the method returns a new Container rather than modifying a 
    // current Container The different types of containers shouldn't matter, 
    // but I wanted to test them just in case. And since it takes in Art, but 
    // it only makes decisions based on whether it is Mirror or not I only 
    // checked those two Art Types. If it can't accept crates we shouldn't 
    // be trying to create Container's with Mirrors, but I tested it anyway 
    // for all containers except CrateNoCrate, because we can't physically 
    // create a crate with acceptsCrates as false
    // Total Tests: 22; 6 Containers (2 canAcceptCrate Options on 5), 2 Arts,
    @Test
    public void testConstructContainerForArtWithMirrorOnNullContainer(){
        Container container = nullContainer.constructContainerForArt(mirrorArt);
        assertTrue("Wrong expected Type. Expected Crate, was given " + container.getType().toString(), 
        container.getType() == ArchDesign.entities.Container.Type.Crate);
        assertTrue("Wrong expected is Mirror Crate. Expected Crate to be a Mirror Crate, but it wasn't",
        container.isMirrorCrate());
        assertFalse("Expected the Crate to not be empty", container.isEmpty());
        assertTrue("Expected the Crate to have a capacity of " + MIRROR_CRATE_LIMIT + ", but it was " + container.getCapacity(),
        container.getCapacity() == MIRROR_CRATE_LIMIT);
        assertTrue("Expected the first Art in Crate to be mirrorArt, it wasn't", 
        container.getArts().get(0) == mirrorArt);
        assertTrue("Expected there to only be one Mirror, there wasn't", 
        container.getCurrentSize() == 1);
        assertTrue("Expected the height to be " + CRATE_HEIGHT_OVERHEAD + " more than the Mirror; Container Height: " + container.getHeight()
        + "Mirror height: " + mirrorArt.getHeight(), container.getHeight() == mirrorArt.getHeight() + CRATE_HEIGHT_OVERHEAD);
        assertTrue("Expected a standard crate to be " + CRATE_WIDTH + " wide, it wasn't", 
        container.getWidth() == CRATE_WIDTH);
        assertTrue("Expected a standard crate to be " + CRATE_LENGTH+ " in length, it wasn't", 
        container.getLength() == CRATE_LENGTH);
    }

    @Test
    public void testConstructContainerForArtWithMirrorOnPalletContainer(){
        Container container = palletContainer.constructContainerForArt(mirrorArt);
        assertTrue("Wrong expected Type. Expected Crate, was given " + container.getType().toString(), 
        container.getType() == ArchDesign.entities.Container.Type.Crate);
        assertTrue("Wrong expected is Mirror Crate. Expected Crate to be a Mirror Crate, but it wasn't",
        container.isMirrorCrate());
        assertFalse("Expected the Crate to not be empty", container.isEmpty());
        assertTrue("Expected the Crate to have a capacity of " + MIRROR_CRATE_LIMIT + ", but it was " + container.getCapacity(),
        container.getCapacity() == MIRROR_CRATE_LIMIT);
        assertTrue("Expected the first Art in Crate to be mirrorArt, it wasn't", 
        container.getArts().get(0) == mirrorArt);
        assertTrue("Expected there to only be one Mirror, there wasn't", 
        container.getCurrentSize() == 1);
        assertTrue("Expected the height to be " + CRATE_HEIGHT_OVERHEAD + " more than the Mirror; Container Height: " + container.getHeight()
        + "Mirror height: " + mirrorArt.getHeight(), container.getHeight() == mirrorArt.getHeight() + CRATE_HEIGHT_OVERHEAD);
        assertTrue("Expected a standard crate to be " + CRATE_WIDTH + " wide, it wasn't", 
        container.getWidth() == CRATE_WIDTH);
        assertTrue("Expected a standard crate to be " + CRATE_LENGTH+ " in length, it wasn't", 
        container.getLength() == CRATE_LENGTH);
    }

    @Test
    public void testConstructContainerForArtWithMirrorOnCrateContainer(){
        Container container = crateContainer.constructContainerForArt(mirrorArt);
        assertTrue("Wrong expected Type. Expected Crate, was given " + container.getType().toString(), 
        container.getType() == ArchDesign.entities.Container.Type.Crate);
        assertTrue("Wrong expected is Mirror Crate. Expected Crate to be a Mirror Crate, but it wasn't",
        container.isMirrorCrate());
        assertFalse("Expected the Crate to not be empty", container.isEmpty());
        assertTrue("Expected the Crate to have a capacity of " + MIRROR_CRATE_LIMIT + ", but it was " + container.getCapacity(),
        container.getCapacity() == MIRROR_CRATE_LIMIT);
        assertTrue("Expected the first Art in Crate to be mirrorArt, it wasn't", 
        container.getArts().get(0) == mirrorArt);
        assertTrue("Expected there to only be one Mirror, there wasn't", 
        container.getCurrentSize() == 1);
        assertTrue("Expected the height to be " + CRATE_HEIGHT_OVERHEAD + " more than the Mirror; Container Height: " + container.getHeight()
        + "Mirror height: " + mirrorArt.getHeight(), container.getHeight() == mirrorArt.getHeight() + CRATE_HEIGHT_OVERHEAD);
        assertTrue("Expected a standard crate to be " + CRATE_WIDTH + " wide, it wasn't", 
        container.getWidth() == CRATE_WIDTH);
        assertTrue("Expected a standard crate to be " + CRATE_LENGTH+ " in length, it wasn't", 
        container.getLength() == CRATE_LENGTH);
    }

    @Test
    public void testConstructContainerForArtWithMirrorOnGlassContainer(){
        Container container = glassContainer.constructContainerForArt(mirrorArt);
        assertTrue("Wrong expected Type. Expected Crate, was given " + container.getType().toString(), 
        container.getType() == ArchDesign.entities.Container.Type.Crate);
        assertTrue("Wrong expected is Mirror Crate. Expected Crate to be a Mirror Crate, but it wasn't",
        container.isMirrorCrate());
        assertFalse("Expected the Crate to not be empty", container.isEmpty());
        assertTrue("Expected the Crate to have a capacity of " + MIRROR_CRATE_LIMIT + ", but it was " + container.getCapacity(),
        container.getCapacity() == MIRROR_CRATE_LIMIT);
        assertTrue("Expected the first Art in Crate to be mirrorArt, it wasn't", 
        container.getArts().get(0) == mirrorArt);
        assertTrue("Expected there to only be one Mirror, there wasn't", 
        container.getCurrentSize() == 1);
        assertTrue("Expected the height to be " + CRATE_HEIGHT_OVERHEAD + " more than the Mirror; Container Height: " + container.getHeight()
        + "Mirror height: " + mirrorArt.getHeight(), container.getHeight() == mirrorArt.getHeight() + CRATE_HEIGHT_OVERHEAD);
        assertTrue("Expected a standard crate to be " + CRATE_WIDTH + " wide, it wasn't", 
        container.getWidth() == CRATE_WIDTH);
        assertTrue("Expected a standard crate to be " + CRATE_LENGTH+ " in length, it wasn't", 
        container.getLength() == CRATE_LENGTH);
    }

    @Test
    public void testConstructContainerForArtWithMirrorOnOversizeContainer(){
        Container container = oversizeContainer.constructContainerForArt(mirrorArt);
        assertTrue("Wrong expected Type. Expected Crate, was given " + container.getType().toString(), 
        container.getType() == ArchDesign.entities.Container.Type.Crate);
        assertTrue("Wrong expected is Mirror Crate. Expected Crate to be a Mirror Crate, but it wasn't",
        container.isMirrorCrate());
        assertFalse("Expected the Crate to not be empty", container.isEmpty());
        assertTrue("Expected the Crate to have a capacity of " + MIRROR_CRATE_LIMIT + ", but it was " + container.getCapacity(),
        container.getCapacity() == MIRROR_CRATE_LIMIT);
        assertTrue("Expected the first Art in Crate to be mirrorArt, it wasn't", 
        container.getArts().get(0) == mirrorArt);
        assertTrue("Expected there to only be one Mirror, there wasn't", 
        container.getCurrentSize() == 1);
        assertTrue("Expected the height to be " + CRATE_HEIGHT_OVERHEAD + " more than the Mirror; Container Height: " + container.getHeight()
        + "Mirror height: " + mirrorArt.getHeight(), container.getHeight() == mirrorArt.getHeight() + CRATE_HEIGHT_OVERHEAD);
        assertTrue("Expected a standard crate to be " + CRATE_WIDTH + " wide, it wasn't", 
        container.getWidth() == CRATE_WIDTH);
        assertTrue("Expected a standard crate to be " + CRATE_LENGTH+ " in length, it wasn't", 
        container.getLength() == CRATE_LENGTH);
    }

    @Test
    public void testConstructContainerForArtWithMirrorOnCustomContainer(){
        Container container = customContainer.constructContainerForArt(mirrorArt);
        assertTrue("Wrong expected Type. Expected Crate, was given " + container.getType().toString(), 
        container.getType() == ArchDesign.entities.Container.Type.Crate);
        assertTrue("Wrong expected is Mirror Crate. Expected Crate to be a Mirror Crate, but it wasn't",
        container.isMirrorCrate());
        assertFalse("Expected the Crate to not be empty", container.isEmpty());
        assertTrue("Expected the Crate to have a capacity of " + MIRROR_CRATE_LIMIT + ", but it was " + container.getCapacity(),
        container.getCapacity() == MIRROR_CRATE_LIMIT);
        assertTrue("Expected the first Art in Crate to be mirrorArt, it wasn't", 
        container.getArts().get(0) == mirrorArt);
        assertTrue("Expected there to only be one Mirror, there wasn't", 
        container.getCurrentSize() == 1);
        assertTrue("Expected the height to be " + CRATE_HEIGHT_OVERHEAD + " more than the Mirror; Container Height: " + container.getHeight()
        + "Mirror height: " + mirrorArt.getHeight(), container.getHeight() == mirrorArt.getHeight() + CRATE_HEIGHT_OVERHEAD);
        assertTrue("Expected a standard crate to be " + CRATE_WIDTH + " wide, it wasn't", 
        container.getWidth() == CRATE_WIDTH);
        assertTrue("Expected a standard crate to be " + CRATE_LENGTH+ " in length, it wasn't", 
        container.getLength() == CRATE_LENGTH);
    }

    @Test
    public void testConstructContainerForArtWithNonMirrorOnNullContainer(){
        assertThrows("Expected an IllegalArgumentException, but it did not get thrown", 
        IllegalArgumentException.class, () -> nullContainer.constructContainerForArt(nonMirrorArt));
    }

    @Test
    public void testConstructContainerForArtWithNonMirrorOnPalletContainer(){
        assertThrows("Expected an IllegalArgumentException, but it did not get thrown", 
        IllegalArgumentException.class, () -> palletContainer.constructContainerForArt(nonMirrorArt));
    }

    @Test
    public void testConstructContainerForArtWithNonMirrorOnCrateContainer(){
        assertThrows("Expected an IllegalArgumentException, but it did not get thrown", 
        IllegalArgumentException.class, () -> crateContainer.constructContainerForArt(nonMirrorArt));
    }

    @Test
    public void testConstructContainerForArtWithNonMirrorOnGlassContainer(){
        assertThrows("Expected an IllegalArgumentException, but it did not get thrown", 
        IllegalArgumentException.class, () -> glassContainer.constructContainerForArt(nonMirrorArt));
    }

    @Test
    public void testConstructContainerForArtWithNonMirrorOnOversizeContainer(){
        assertThrows("Expected an IllegalArgumentException, but it did not get thrown", 
        IllegalArgumentException.class, () -> oversizeContainer.constructContainerForArt(nonMirrorArt));
    }

    @Test
    public void testConstructContainerForArtWithNonMirrorOnCustomContainer(){
        assertThrows("Expected an IllegalArgumentException, but it did not get thrown", 
        IllegalArgumentException.class, () -> customContainer.constructContainerForArt(nonMirrorArt));
    }

    @Test
    public void testConstructContainerForArtWithMirrorOnNullContainerNoCrate(){
        assertThrows("Expected an IllegalArgumentException, but it did not get thrown", 
        IllegalArgumentException.class, () -> nullContainerNoCrate.constructContainerForArt(nonMirrorArt));
    }

    @Test
    public void testConstructContainerForArtWithMirrorOnPalletContainerNoCrate(){
        assertThrows("Expected an IllegalArgumentException, but it did not get thrown", 
        IllegalArgumentException.class, () -> palletContainerNoCrate.constructContainerForArt(nonMirrorArt));
    }

    @Test
    public void testConstructContainerForArtWithMirrorOnGlassContainerNoCrate(){
        assertThrows("Expected an IllegalArgumentException, but it did not get thrown", 
        IllegalArgumentException.class, () -> glassContainerNoCrate.constructContainerForArt(nonMirrorArt));
    }

    @Test
    public void testConstructContainerForArtWithMirrorOnOversizeContainerNoCrate(){
        assertThrows("Expected an IllegalArgumentException, but it did not get thrown", 
        IllegalArgumentException.class, () -> oversizeContainerNoCrate.constructContainerForArt(nonMirrorArt));
    }

    @Test
    public void testConstructContainerForArtWithMirrorOnCustomContainerNoCrate(){
        assertThrows("Expected an IllegalArgumentException, but it did not get thrown", 
        IllegalArgumentException.class, () -> customContainerNoCrate.constructContainerForArt(nonMirrorArt));
    }

    @Test
    public void testConstructContainerForArtWithNonMirrorOnNullContainerNoCrate(){
        assertThrows("Expected an IllegalArgumentException, but it did not get thrown", 
        IllegalArgumentException.class, () -> nullContainer.constructContainerForArt(nonMirrorArt));
    }

    @Test
    public void testConstructContainerForArtWithNonMirrorOnPalletContainerNoCrate(){
        assertThrows("Expected an IllegalArgumentException, but it did not get thrown", 
        IllegalArgumentException.class, () -> palletContainer.constructContainerForArt(nonMirrorArt));
    }

    @Test
    public void testConstructContainerForArtWithNonMirrorOnGlassContainerNoCrate(){
        assertThrows("Expected an IllegalArgumentException, but it did not get thrown", 
        IllegalArgumentException.class, () -> glassContainer.constructContainerForArt(nonMirrorArt));
    }

    @Test
    public void testConstructContainerForArtWithNonMirrorOnOversizeContainerNoCrate(){
        assertThrows("Expected an IllegalArgumentException, but it did not get thrown", 
        IllegalArgumentException.class, () -> oversizeContainer.constructContainerForArt(nonMirrorArt));
    }

    @Test
    public void testConstructContainerForArtWithNonMirrorOnCustomContainerNoCrate(){
        assertThrows("Expected an IllegalArgumentException, but it did not get thrown", 
        IllegalArgumentException.class, () -> customContainerNoCrate.constructContainerForArt(nonMirrorArt));
    }
    
    // ---------------- testing constructContainerForBox ----------------
    @Test
    public void testconstructContainerForBoxWithStandardBox(){

    }

    @Test
    public void testconstructContainerForBoxWithOversizedBox(){

    }

    @Test
    public void testconstructContainerForBoxWithCustomSmallBox(){

    }

    @Test
    public void testconstructContainerForBoxWithCustomLargeBox(){

    }
    
    // ---------------- testing addBox ----------------
    @Test
    public void testaddBox(){

    }
    
    // ---------------- testing addArt ----------------
    @Test
    public void testaddArt(){

    }
    
    // ---------------- testing canBoxFit ----------------
    @Test
    public void testcanBoxFit(){

    }
    
    // ---------------- testing canArtFit ----------------
    @Test
    public void testcanArtFit(){

    }
    
    // ---------------- testing getWeight() ----------------
    @Test
    public void testgetWeight(){

    }
    
    // ---------------- testing getCapacity ----------------
    @Test
    public void testgetCapacity(){

    }
    
}
