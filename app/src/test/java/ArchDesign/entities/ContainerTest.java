package ArchDesign.entities;

import static org.junit.Assert.assertEquals;
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
    protected Container fullPalletContainer;
    protected Container fullCrateContainer;
    protected Container fullGlassContainer;
    protected Container fullOversizeContainer;
    protected Art mirrorArt;
    protected Art nonMirrorArt;
    protected Art standardArt;
    protected Art oversizeArt;
    protected Art smallCustomArt;
    protected Art largeCustomArt;
    protected Box standardBox;
    protected Box oversizeBox;
    protected Box customSmallBox;
    protected Box customLargeBox;

    protected static final double tooSmallWidthContainer = 34.5;
    protected static final double standardWidthContainer = 36.5;
    protected static final double oversizeWidthContainer = 44;
    protected static final double tooLargeWidthContainer = 45;

    protected static final double tooSmallWidthArt = 34;
    protected static final double standardWidthArt = 35.5;
    protected static final double oversizeWidthArt = 43.5;
    protected static final double tooLargeWidthArt = 46;

    // From Box.java
    protected static final double STANDARD_LENGTH = 37;
    protected static final double STANDARD_WIDTH = 11;
    protected static final double STANDARD_HEIGHT = 31;
    protected static final double OVERSIZE_LENGTH = 44;
    protected static final double OVERSIZE_WIDTH = 13;
    protected static final double OVERSIZE_HEIGHT = 48;
    protected static final double OVERSIZE_BOX_LIMIT = 36;

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
        // Currently Setup failing due to the fullContainers. addBox isn't working properly because canBoxFit isn't working
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
        mirrorArt = new Art(Type.Mirror, Glazing.NoGlaze, 1, standardWidthArt, standardWidthArt, 3);
        nonMirrorArt = new Art(Type.PaperPrintFramed, Glazing.Glass, 2, standardWidthArt, standardWidthArt, 3);
        standardArt = new Art(Type.PaperPrintFramed, Glazing.Glass, 3, standardWidthArt, standardWidthArt, 3);
        oversizeArt = new Art(Type.PaperPrintFramed, Glazing.Glass, 4, oversizeWidthArt, oversizeWidthArt, 3);
        smallCustomArt = new Art(Type.PaperPrintFramed, Glazing.Glass, 5, tooSmallWidthArt, tooSmallWidthArt, 3);
        largeCustomArt = new Art(Type.PaperPrintFramed, Glazing.Glass, 6, tooLargeWidthArt, tooLargeWidthArt, 3);
        standardBox = new Box();
        standardBox.addArt(standardArt);
        oversizeBox = new Box();
        oversizeBox.addArt(oversizeArt);
        customSmallBox = new Box();
        customSmallBox.addArt(smallCustomArt);
        customLargeBox = new Box();
        customLargeBox.addArt(largeCustomArt);
        fullPalletContainer = new Container(ArchDesign.entities.Container.Type.Pallet, true);
        fullPalletContainer.addBox(standardBox);
        fullPalletContainer.addBox(standardBox);
        fullPalletContainer.addBox(standardBox);
        fullPalletContainer.addBox(standardBox);
        fullCrateContainer = new Container(ArchDesign.entities.Container.Type.Crate, true);
        fullCrateContainer.addBox(standardBox);
        fullCrateContainer.addBox(standardBox);
        fullCrateContainer.addBox(standardBox);
        fullCrateContainer.addBox(standardBox);
        fullGlassContainer = new Container(ArchDesign.entities.Container.Type.Glass, true);
        fullGlassContainer.addBox(customSmallBox);
        fullGlassContainer.addBox(customSmallBox);
        fullGlassContainer.addBox(customSmallBox);
        fullGlassContainer.addBox(customSmallBox);
        fullOversizeContainer = new Container(ArchDesign.entities.Container.Type.Oversize, true);
        fullOversizeContainer.addBox(oversizeBox);
        fullOversizeContainer.addBox(oversizeBox);
        fullOversizeContainer.addBox(oversizeBox);
        fullOversizeContainer.addBox(oversizeBox);
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
    // Since the method returns a new Container rather than modifying a 
    // current Container The different types of containers shouldn't matter, 
    // but I wanted to test them just in case. And since it takes in Box, and 
    // there are 4 different types of Boxes (Standard, Oversize, Custom, Small 
    // Enough to fit into a Glass Pallet).
    // Currently All tests (Except Glass) are failing, because Box.java has a 
    // broken getWidth(), getLength(), getWeight() function that returns 0.0 
    // so it creates everything as a Glass Pallet.
    // Total Tests: 29; 6 Containers (2 canAcceptCrate Options on 5), 4 Boxes,
    @Test
    public void testconstructContainerForBoxWithStandardBoxOnNullContainer(){
        Box box = standardBox;
        Container container = nullContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Crate, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Crate);
        assertTrue("Wrong expected is Mirror Crate. Expected Crate to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Crate to not be empty", container.isEmpty());
        assertTrue("Expected the Crate to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Crate to be standardBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be " + CRATE_HEIGHT_OVERHEAD + " more than the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight() + CRATE_HEIGHT_OVERHEAD);
        assertTrue("Expected a standard crate to be " + CRATE_WIDTH + " wide, it wasn't", 
        container.getWidth() == CRATE_WIDTH);
        assertTrue("Expected a standard crate to be " + CRATE_LENGTH+ " in length, it wasn't", 
        container.getLength() == CRATE_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithStandardBoxOnPalletContainer(){
        Box box = standardBox;
        Container container = palletContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Crate, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Crate);
        assertTrue("Wrong expected is Mirror Crate. Expected Crate to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Crate to not be empty", container.isEmpty());
        assertTrue("Expected the Crate to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Crate to be standardBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be " + CRATE_HEIGHT_OVERHEAD + " more than the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight() + CRATE_HEIGHT_OVERHEAD);
        assertTrue("Expected a standard crate to be " + CRATE_WIDTH + " wide, it wasn't", 
        container.getWidth() == CRATE_WIDTH);
        assertTrue("Expected a standard crate to be " + CRATE_LENGTH+ " in length, it wasn't", 
        container.getLength() == CRATE_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithStandardBoxOnCrateContainer(){
        Box box = standardBox;
        Container container = crateContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Crate, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Crate);
        assertTrue("Wrong expected is Mirror Crate. Expected Crate to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Crate to not be empty", container.isEmpty());
        assertTrue("Expected the Crate to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Crate to be standardBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be " + CRATE_HEIGHT_OVERHEAD + " more than the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight() + CRATE_HEIGHT_OVERHEAD);
        assertTrue("Expected a standard crate to be " + CRATE_WIDTH + " wide, it wasn't", 
        container.getWidth() == CRATE_WIDTH);
        assertTrue("Expected a standard crate to be " + CRATE_LENGTH+ " in length, it wasn't", 
        container.getLength() == CRATE_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithStandardBoxOnGlassContainer(){
        Box box = standardBox;
        Container container = glassContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Crate, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Crate);
        assertTrue("Wrong expected is Mirror Crate. Expected Crate to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Crate to not be empty", container.isEmpty());
        assertTrue("Expected the Crate to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Crate to be standardBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be " + CRATE_HEIGHT_OVERHEAD + " more than the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight() + CRATE_HEIGHT_OVERHEAD);
        assertTrue("Expected a standard crate to be " + CRATE_WIDTH + " wide, it wasn't", 
        container.getWidth() == CRATE_WIDTH);
        assertTrue("Expected a standard crate to be " + CRATE_LENGTH+ " in length, it wasn't", 
        container.getLength() == CRATE_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithStandardBoxOnOversizedContainer(){
        Box box = standardBox;
        Container container = oversizeContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Crate, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Crate);
        assertTrue("Wrong expected is Mirror Crate. Expected Crate to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Crate to not be empty", container.isEmpty());
        assertTrue("Expected the Crate to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Crate to be standardBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be " + CRATE_HEIGHT_OVERHEAD + " more than the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight() + CRATE_HEIGHT_OVERHEAD);
        assertTrue("Expected a standard crate to be " + CRATE_WIDTH + " wide, it wasn't", 
        container.getWidth() == CRATE_WIDTH);
        assertTrue("Expected a standard crate to be " + CRATE_LENGTH+ " in length, it wasn't", 
        container.getLength() == CRATE_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithStandardBoxOnCustomContainer(){
        Box box = standardBox;
        Container container = customContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Crate, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Crate);
        assertTrue("Wrong expected is Mirror Crate. Expected Crate to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Crate to not be empty", container.isEmpty());
        assertTrue("Expected the Crate to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Crate to be standardBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be " + CRATE_HEIGHT_OVERHEAD + " more than the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight() + CRATE_HEIGHT_OVERHEAD);
        assertTrue("Expected a standard crate to be " + CRATE_WIDTH + " wide, it wasn't", 
        container.getWidth() == CRATE_WIDTH);
        assertTrue("Expected a standard crate to be " + CRATE_LENGTH+ " in length, it wasn't", 
        container.getLength() == CRATE_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithOversizedBoxOnNullContainer(){
        Box box = oversizeBox;
        Container container = nullContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Overisze, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Oversize);
        assertTrue("Wrong expected is Mirror Crate. Expected Oversize to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Oversize to not be empty", container.isEmpty());
        assertTrue("Expected the Oversize to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Oversize to be oversizeBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a Glass to be " + OVERSIZE_PALLET_WIDTH + " wide, it wasn't", 
        container.getWidth() == OVERSIZE_PALLET_WIDTH);
        assertTrue("Expected a standard crate to be " + OVERSIZE_PALLET_LENGTH+ " in length, it wasn't", 
        container.getLength() == OVERSIZE_PALLET_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithOversizedBoxOnPalletContainer(){
        Box box = oversizeBox;
        Container container = palletContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Overisze, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Oversize);
        assertTrue("Wrong expected is Mirror Crate. Expected Oversize to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Oversize to not be empty", container.isEmpty());
        assertTrue("Expected the Oversize to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Oversize to be oversizeBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a Glass to be " + OVERSIZE_PALLET_WIDTH + " wide, it wasn't", 
        container.getWidth() == OVERSIZE_PALLET_WIDTH);
        assertTrue("Expected a standard crate to be " + OVERSIZE_PALLET_LENGTH+ " in length, it wasn't", 
        container.getLength() == OVERSIZE_PALLET_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithOversizedBoxOnCrateContainer(){
        Box box = oversizeBox;
        Container container = crateContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Overisze, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Oversize);
        assertTrue("Wrong expected is Mirror Crate. Expected Oversize to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Oversize to not be empty", container.isEmpty());
        assertTrue("Expected the Oversize to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Oversize to be oversizeBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a Glass to be " + OVERSIZE_PALLET_WIDTH + " wide, it wasn't", 
        container.getWidth() == OVERSIZE_PALLET_WIDTH);
        assertTrue("Expected a standard crate to be " + OVERSIZE_PALLET_LENGTH+ " in length, it wasn't", 
        container.getLength() == OVERSIZE_PALLET_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithOversizedBoxOnGlassContainer(){
        Box box = oversizeBox;
        Container container = glassContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Overisze, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Oversize);
        assertTrue("Wrong expected is Mirror Crate. Expected Oversize to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Oversize to not be empty", container.isEmpty());
        assertTrue("Expected the Oversize to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Oversize to be oversizeBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a Glass to be " + OVERSIZE_PALLET_WIDTH + " wide, it wasn't", 
        container.getWidth() == OVERSIZE_PALLET_WIDTH);
        assertTrue("Expected a standard crate to be " + OVERSIZE_PALLET_LENGTH+ " in length, it wasn't", 
        container.getLength() == OVERSIZE_PALLET_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithOversizedBoxOnOversizeContainer(){
        Box box = oversizeBox;
        Container container = oversizeContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Overisze, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Oversize);
        assertTrue("Wrong expected is Mirror Crate. Expected Oversize to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Oversize to not be empty", container.isEmpty());
        assertTrue("Expected the Oversize to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Oversize to be oversizeBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a Glass to be " + OVERSIZE_PALLET_WIDTH + " wide, it wasn't", 
        container.getWidth() == OVERSIZE_PALLET_WIDTH);
        assertTrue("Expected a standard crate to be " + OVERSIZE_PALLET_LENGTH+ " in length, it wasn't", 
        container.getLength() == OVERSIZE_PALLET_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithOversizedBoxOnCustomContainer(){
        Box box = oversizeBox;
        Container container = customContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Overisze, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Oversize);
        assertTrue("Wrong expected is Mirror Crate. Expected Oversize to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Oversize to not be empty", container.isEmpty());
        assertTrue("Expected the Oversize to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Oversize to be oversizeBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a Glass to be " + OVERSIZE_PALLET_WIDTH + " wide, it wasn't", 
        container.getWidth() == OVERSIZE_PALLET_WIDTH);
        assertTrue("Expected a standard crate to be " + OVERSIZE_PALLET_LENGTH+ " in length, it wasn't", 
        container.getLength() == OVERSIZE_PALLET_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithCustomSmallBoxOnNullContainer(){
        Box box = customSmallBox;
        Container container = nullContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Glass, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Glass);
        assertTrue("Wrong expected is Mirror Crate. Expected Glass to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Glass to not be empty", container.isEmpty());
        assertTrue("Expected the Glass to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Glass to be customSmallBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a Glass to be " + GLASS_PALLET_WIDTH + " wide, it wasn't", 
        container.getWidth() == GLASS_PALLET_WIDTH);
        assertTrue("Expected a standard crate to be " + GLASS_PALLET_LENGTH+ " in length, it wasn't", 
        container.getLength() == GLASS_PALLET_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithCustomSmallBoxOnPalletContainer(){
        Box box = customSmallBox;
        Container container = palletContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Glass, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Glass);
        assertTrue("Wrong expected is Mirror Crate. Expected Glass to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Glass to not be empty", container.isEmpty());
        assertTrue("Expected the Glass to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Glass to be customSmallBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a Glass to be " + GLASS_PALLET_WIDTH + " wide, it wasn't", 
        container.getWidth() == GLASS_PALLET_WIDTH);
        assertTrue("Expected a standard crate to be " + GLASS_PALLET_LENGTH+ " in length, it wasn't", 
        container.getLength() == GLASS_PALLET_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithCustomSmallBoxOnCrateContainer(){
        Box box = customSmallBox;
        Container container = crateContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Glass, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Glass);
        assertTrue("Wrong expected is Mirror Crate. Expected Glass to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Glass to not be empty", container.isEmpty());
        assertTrue("Expected the Glass to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Glass to be customSmallBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a Glass to be " + GLASS_PALLET_WIDTH + " wide, it wasn't", 
        container.getWidth() == GLASS_PALLET_WIDTH);
        assertTrue("Expected a standard crate to be " + GLASS_PALLET_LENGTH+ " in length, it wasn't", 
        container.getLength() == GLASS_PALLET_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithCustomSmallBoxOnGlassContainer(){
        Box box = customSmallBox;
        Container container = glassContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Glass, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Glass);
        assertTrue("Wrong expected is Mirror Crate. Expected Glass to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Glass to not be empty", container.isEmpty());
        assertTrue("Expected the Glass to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Glass to be customSmallBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a Glass to be " + GLASS_PALLET_WIDTH + " wide, it wasn't", 
        container.getWidth() == GLASS_PALLET_WIDTH);
        assertTrue("Expected a standard crate to be " + GLASS_PALLET_LENGTH+ " in length, it wasn't", 
        container.getLength() == GLASS_PALLET_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithCustomSmallBoxOnOversizeContainer(){
        Box box = customSmallBox;
        Container container = oversizeContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Glass, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Glass);
        assertTrue("Wrong expected is Mirror Crate. Expected Glass to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Glass to not be empty", container.isEmpty());
        assertTrue("Expected the Glass to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Glass to be customSmallBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a Glass to be " + GLASS_PALLET_WIDTH + " wide, it wasn't", 
        container.getWidth() == GLASS_PALLET_WIDTH);
        assertTrue("Expected a standard crate to be " + GLASS_PALLET_LENGTH+ " in length, it wasn't", 
        container.getLength() == GLASS_PALLET_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithCustomSmallBoxOnCustomContainer(){
        Box box = customSmallBox;
        Container container = customContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Glass, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Glass);
        assertTrue("Wrong expected is Mirror Crate. Expected Glass to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Glass to not be empty", container.isEmpty());
        assertTrue("Expected the Glass to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Glass to be customSmallBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a Glass to be " + GLASS_PALLET_WIDTH + " wide, it wasn't", 
        container.getWidth() == GLASS_PALLET_WIDTH);
        assertTrue("Expected a standard crate to be " + GLASS_PALLET_LENGTH+ " in length, it wasn't", 
        container.getLength() == GLASS_PALLET_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithCustomLargeBoxOnNullContainer(){
        Box box = customLargeBox;
        Container container = nullContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Custom, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Custom);
        assertTrue("Wrong expected is Mirror Crate. Expected Custom to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Custom to not be empty", container.isEmpty());
        assertTrue("Expected the Custom to have a capacity of " + 1 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 1);
        assertTrue("Expected the first Box in Custom to be customLargeBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Custom rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a Custom to be at least " + CUSTOM_BOX_DIMENSION_LIMIT + " wide in one direction, it wasn't"
         + " Width: " + container.getWidth() + " Length: " + container.getLength(), 
         container.getWidth() >= CUSTOM_BOX_DIMENSION_LIMIT || container.getLength() >= CUSTOM_BOX_DIMENSION_LIMIT);
    }

    @Test
    public void testconstructContainerForBoxWithCustomLargeBoxOnPalletContainer(){
        Box box = customLargeBox;
        Container container = palletContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Custom, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Custom);
        assertTrue("Wrong expected is Mirror Crate. Expected Custom to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Custom to not be empty", container.isEmpty());
        assertTrue("Expected the Custom to have a capacity of " + 1 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 1);
        assertTrue("Expected the first Box in Custom to be customLargeBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Custom rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a Custom to be at least " + CUSTOM_BOX_DIMENSION_LIMIT + " wide in one direction, it wasn't"
         + " Width: " + container.getWidth() + " Length: " + container.getLength(), 
         container.getWidth() >= CUSTOM_BOX_DIMENSION_LIMIT || container.getLength() >= CUSTOM_BOX_DIMENSION_LIMIT);
    }

    @Test
    public void testconstructContainerForBoxWithCustomLargeBoxOnCrateContainer(){
        Box box = customLargeBox;
        Container container = crateContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Custom, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Custom);
        assertTrue("Wrong expected is Mirror Crate. Expected Custom to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Custom to not be empty", container.isEmpty());
        assertTrue("Expected the Custom to have a capacity of " + 1 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 1);
        assertTrue("Expected the first Box in Custom to be customLargeBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Custom rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a Custom to be at least " + CUSTOM_BOX_DIMENSION_LIMIT + " wide in one direction, it wasn't"
         + " Width: " + container.getWidth() + " Length: " + container.getLength(), 
         container.getWidth() >= CUSTOM_BOX_DIMENSION_LIMIT || container.getLength() >= CUSTOM_BOX_DIMENSION_LIMIT);
    }

    @Test
    public void testconstructContainerForBoxWithCustomLargeBoxOnGlassContainer(){
        Box box = customLargeBox;
        Container container = glassContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Custom, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Custom);
        assertTrue("Wrong expected is Mirror Crate. Expected Custom to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Custom to not be empty", container.isEmpty());
        assertTrue("Expected the Custom to have a capacity of " + 1 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 1);
        assertTrue("Expected the first Box in Custom to be customLargeBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Custom rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a Custom to be at least " + CUSTOM_BOX_DIMENSION_LIMIT + " wide in one direction, it wasn't"
         + " Width: " + container.getWidth() + " Length: " + container.getLength(), 
         container.getWidth() >= CUSTOM_BOX_DIMENSION_LIMIT || container.getLength() >= CUSTOM_BOX_DIMENSION_LIMIT);
    }

    @Test
    public void testconstructContainerForBoxWithCustomLargeBoxOnOversizeContainer(){
        Box box = customLargeBox;
        Container container = oversizeContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Custom, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Custom);
        assertTrue("Wrong expected is Mirror Crate. Expected Custom to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Custom to not be empty", container.isEmpty());
        assertTrue("Expected the Custom to have a capacity of " + 1 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 1);
        assertTrue("Expected the first Box in Custom to be customLargeBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Custom rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a Custom to be at least " + CUSTOM_BOX_DIMENSION_LIMIT + " wide in one direction, it wasn't"
         + " Width: " + container.getWidth() + " Length: " + container.getLength(), 
         container.getWidth() >= CUSTOM_BOX_DIMENSION_LIMIT || container.getLength() >= CUSTOM_BOX_DIMENSION_LIMIT);
    }

    @Test
    public void testconstructContainerForBoxWithCustomLargeBoxOnCustomContainer(){
        Box box = customLargeBox;
        Container container = customContainer.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Custom, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Custom);
        assertTrue("Wrong expected is Mirror Crate. Expected Custom to NOT be a Mirror Crate, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Custom to not be empty", container.isEmpty());
        assertTrue("Expected the Custom to have a capacity of " + 1 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 1);
        assertTrue("Expected the first Box in Custom to be customLargeBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the crate is being set to Custom rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a Custom to be at least " + CUSTOM_BOX_DIMENSION_LIMIT + " wide in one direction, it wasn't"
         + " Width: " + container.getWidth() + " Length: " + container.getLength(), 
         container.getWidth() >= CUSTOM_BOX_DIMENSION_LIMIT || container.getLength() >= CUSTOM_BOX_DIMENSION_LIMIT);
    }
    
    @Test
    public void testconstructContainerForBoxWithStandardBoxOnNullContainerWithNoCrate(){
        Box box = standardBox;
        Container container = nullContainerNoCrate.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Pallet, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Pallet);
        assertTrue("Wrong expected is Mirror Pallet. Expected Pallet to NOT be a Mirror Pallet, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Pallet to not be empty", container.isEmpty());
        assertTrue("Expected the Pallet to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Pallet to be standardBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the Pallet is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be the same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a standard Pallet to be " + STANDARD_PALLET_WIDTH + " wide, it wasn't", 
        container.getWidth() == STANDARD_PALLET_WIDTH);
        assertTrue("Expected a standard Pallet to be " + STANDARD_PALLET_LENGTH+ " in length, it wasn't", 
        container.getLength() == STANDARD_PALLET_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithStandardBoxOnPalletContainerWithNoCrate(){
        Box box = standardBox;
        Container container = palletContainerNoCrate.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Pallet, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Pallet);
        assertTrue("Wrong expected is Mirror Pallet. Expected Pallet to NOT be a Mirror Pallet, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Pallet to not be empty", container.isEmpty());
        assertTrue("Expected the Pallet to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Pallet to be standardBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the Pallet is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be the same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a standard Pallet to be " + STANDARD_PALLET_WIDTH + " wide, it wasn't", 
        container.getWidth() == STANDARD_PALLET_WIDTH);
        assertTrue("Expected a standard Pallet to be " + STANDARD_PALLET_LENGTH+ " in length, it wasn't", 
        container.getLength() == STANDARD_PALLET_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithStandardBoxOnGlassContainerWithNoCrate(){
        Box box = standardBox;
        Container container = glassContainerNoCrate.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Pallet, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Pallet);
        assertTrue("Wrong expected is Mirror Pallet. Expected Pallet to NOT be a Mirror Pallet, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Pallet to not be empty", container.isEmpty());
        assertTrue("Expected the Pallet to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Pallet to be standardBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the Pallet is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be the same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a standard Pallet to be " + STANDARD_PALLET_WIDTH + " wide, it wasn't", 
        container.getWidth() == STANDARD_PALLET_WIDTH);
        assertTrue("Expected a standard Pallet to be " + STANDARD_PALLET_LENGTH+ " in length, it wasn't", 
        container.getLength() == STANDARD_PALLET_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithStandardBoxOnOversizedContainerWithNoCrate(){
        Box box = standardBox;
        Container container = oversizeContainerNoCrate.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Pallet, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Pallet);
        assertTrue("Wrong expected is Mirror Pallet. Expected Pallet to NOT be a Mirror Pallet, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Pallet to not be empty", container.isEmpty());
        assertTrue("Expected the Pallet to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Pallet to be standardBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the Pallet is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be the same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a standard Pallet to be " + STANDARD_PALLET_WIDTH + " wide, it wasn't", 
        container.getWidth() == STANDARD_PALLET_WIDTH);
        assertTrue("Expected a standard Pallet to be " + STANDARD_PALLET_LENGTH+ " in length, it wasn't", 
        container.getLength() == STANDARD_PALLET_LENGTH);
    }

    @Test
    public void testconstructContainerForBoxWithStandardBoxOnCustomContainerWithNoCrate(){
        Box box = standardBox;
        Container container = customContainerNoCrate.constructContainerForBox(box);
        // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
        assertTrue("Wrong expected Type. Expected Pallet, was given " + container.getType().toString() + 
        " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
        container.getType() == ArchDesign.entities.Container.Type.Pallet);
        assertTrue("Wrong expected is Mirror Pallet. Expected Pallet to NOT be a Mirror Pallet, but it was",
        !container.isMirrorCrate());
        assertFalse("Expected the Pallet to not be empty", container.isEmpty());
        assertTrue("Expected the Pallet to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
        container.getCapacity() == 4);
        assertTrue("Expected the first Box in Pallet to be standardBox, it wasn't", 
        container.getBoxes().get(0) == box);
        assertTrue("Expected there to only be one Box, there wasn't", 
        container.getCurrentSize() == 1);
        // Because it is returning the wrong Height the Pallet is being set to Glass rather than Pallet so the following tests are failing
        assertTrue("Expected the height to be the same as the Box; Container Height: " + container.getHeight()
        + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
        assertTrue("Expected a standard Pallet to be " + STANDARD_PALLET_WIDTH + " wide, it wasn't", 
        container.getWidth() == STANDARD_PALLET_WIDTH);
        assertTrue("Expected a standard Pallet to be " + STANDARD_PALLET_LENGTH+ " in length, it wasn't", 
        container.getLength() == STANDARD_PALLET_LENGTH);
    }

    // ---------------- testing addBox ----------------
    // Since the method returns a boolean and modifies the boxes instance variable
    // We tested this with 4 different types of Boxes, once on Null Container, and
    // Thrice (Empty, Full, NonEmptyNonFull) on the other 5 containers.
    // Total Tests: 64; 6 Containers (3 Fullness Options on 5), 4 Boxes,
    @Test
    public void testaddBoxWithStandardBoxOnNullContainer(){
        Box box = standardBox;
        boolean added = nullContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 1 Box", nullContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithOversizeBoxOnNullContainer(){
        Box box = oversizeBox;
        boolean added = nullContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 1 Box", nullContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithCustomSmallBoxOnNullContainer(){
        Box box = customSmallBox;
        boolean added = nullContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 1 Box", nullContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithCustomLargeBoxOnNullContainer(){
        Box box = customLargeBox;
        boolean added = nullContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 1 Box", nullContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithStandardBoxOnEmptyPalletContainer(){
        Box box = standardBox;
        boolean added = palletContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 1 Box", palletContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithStandardBoxOnFullPalletContainer(){
        Box box = standardBox;
        boolean added = fullPalletContainer.addBox(box);
        assertFalse("Added Box to a Full Container", added);
        assertTrue("Expected 4 Box", fullPalletContainer.getCurrentSize() == 4);
    }

    @Test
    public void testaddBoxWithStandardBoxOnNonEmptyNonFullPalletContainer(){
        Box box = standardBox;
        palletContainer.addBox(box);
        boolean added = palletContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 2 Boxes", palletContainer.getCurrentSize() == 2);
    }

    @Test
    public void testaddBoxWithOversizeBoxOnEmptyPalletContainer(){
        Box box = oversizeBox;
        boolean added = palletContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 1 Box", palletContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithOversizeBoxOnFullPalletContainer(){
        Box box = oversizeBox;
        boolean added = fullPalletContainer.addBox(box);
        assertFalse("Added Box to a Full Container", added);
        assertTrue("Expected 4 Box", fullPalletContainer.getCurrentSize() == 4);
    }

    @Test
    public void testaddBoxWithOversizeBoxOnNonEmptyNonFullPalletContainer(){
        Box box = oversizeBox;
        palletContainer.addBox(box);
        boolean added = palletContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 2 Boxes", palletContainer.getCurrentSize() == 2);
    }

    @Test
    public void testaddBoxWithCustomSmallBoxOnEmptyPalletContainer(){
        Box box = customSmallBox;
        boolean added = palletContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 1 Box", palletContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithCustomSmallBoxOnFullPalletContainer(){
        Box box = customSmallBox;
        boolean added = fullPalletContainer.addBox(box);
        assertFalse("Added Box to a Full Container", added);
        assertTrue("Expected 4 Box", fullPalletContainer.getCurrentSize() == 4);
    }

    @Test
    public void testaddBoxWithCustomSmallBoxOnNonEmptyNonFullPalletContainer(){
        Box box = customSmallBox;
        palletContainer.addBox(box);
        boolean added = palletContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 2 Boxes", palletContainer.getCurrentSize() == 2);
    }

    @Test
    public void testaddBoxWithCustomLargeBoxOnEmptyPalletContainer(){
        Box box = customLargeBox;
        boolean added = palletContainer.addBox(box);
        assertFalse("Added Custom Box to Container", added);
        assertTrue("Expected 1 Box", palletContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithCustomLargeBoxOnFullPalletContainer(){
        Box box = customLargeBox;
        boolean added = fullPalletContainer.addBox(box);
        assertFalse("Added Box to a Full Container", added);
        assertTrue("Expected 4 Box", fullPalletContainer.getCurrentSize() == 4);
    }

    @Test
    public void testaddBoxWithCustomLargeBoxOnNonEmptyNonFullPalletContainer(){
        Box box = customLargeBox;
        palletContainer.addBox(box);
        boolean added = palletContainer.addBox(box);
        assertFalse("Added Custom Box to Container", added);
        assertTrue("Expected 2 Boxes", palletContainer.getCurrentSize() == 2);
    }

    @Test
    public void testaddBoxWithStandardBoxOnEmptyCrateContainer(){
        Box box = standardBox;
        boolean added = crateContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 1 Box", crateContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithStandardBoxOnFullCrateContainer(){
        Box box = standardBox;
        boolean added = fullCrateContainer.addBox(box);
        assertFalse("Added Box to a Full Container", added);
        assertTrue("Expected 4 Box", fullCrateContainer.getCurrentSize() == 4);
    }

    @Test
    public void testaddBoxWithStandardBoxOnNonEmptyNonFullCrateContainer(){
        Box box = standardBox;
        crateContainer.addBox(box);
        boolean added = crateContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 2 Boxes", crateContainer.getCurrentSize() == 2);
    }

    @Test
    public void testaddBoxWithOversizeBoxOnEmptyCrateContainer(){
        Box box = oversizeBox;
        boolean added = crateContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 1 Box", crateContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithOversizeBoxOnFullCrateContainer(){
        Box box = oversizeBox;
        boolean added = fullCrateContainer.addBox(box);
        assertFalse("Added Box to a Full Container", added);
        assertTrue("Expected 4 Box", crateContainer.getCurrentSize() == 4);
    }

    @Test
    public void testaddBoxWithOversizeBoxOnNonEmptyNonFullCrateContainer(){
        Box box = oversizeBox;
        crateContainer.addBox(box);
        boolean added = crateContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 2 Boxes", crateContainer.getCurrentSize() == 2);
    }

    @Test
    public void testaddBoxWithCustomSmallBoxOnEmptyCrateContainer(){
        Box box = customSmallBox;
        boolean added = crateContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 1 Box", crateContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithCustomSmallBoxOnFullCrateContainer(){
        Box box = customSmallBox;
        boolean added = fullCrateContainer.addBox(box);
        assertFalse("Added Box to a Full Container", added);
        assertTrue("Expected 4 Box", fullCrateContainer.getCurrentSize() == 4);
    }

    @Test
    public void testaddBoxWithCustomSmallBoxOnNonEmptyNonFullCrateContainer(){
        Box box = customSmallBox;
        crateContainer.addBox(box);
        boolean added = crateContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 2 Boxes", crateContainer.getCurrentSize() == 2);
    }

    @Test
    public void testaddBoxWithCustomLargeBoxOnEmptyCrateContainer(){
        Box box = customLargeBox;
        boolean added = crateContainer.addBox(box);
        assertFalse("Added Custom Box to Container", added);
        assertTrue("Expected 1 Box", crateContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithCustomLargeBoxOnFullCrateContainer(){
        Box box = customLargeBox;
        boolean added = fullCrateContainer.addBox(box);
        assertFalse("Added Box to a Full Container", added);
        assertTrue("Expected 4 Box", fullCrateContainer.getCurrentSize() == 4);
    }

    @Test
    public void testaddBoxWithCustomLargeBoxOnNonEmptyNonFullCrateContainer(){
        Box box = customLargeBox;
        crateContainer.addBox(box);
        boolean added = crateContainer.addBox(box);
        assertFalse("Added Custom Box to Container", added);
        assertTrue("Expected 2 Boxes", crateContainer.getCurrentSize() == 2);
    }

    @Test
    public void testaddBoxWithStandardBoxOnEmptyGlassContainer(){
        Box box = standardBox;
        boolean added = glassContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 1 Box", glassContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithStandardBoxOnFullGlassContainer(){
        Box box = standardBox;
        boolean added = fullGlassContainer.addBox(box);
        assertFalse("Added Box to a Full Container", added);
        assertTrue("Expected 4 Box", fullGlassContainer.getCurrentSize() == 4);
    }

    @Test
    public void testaddBoxWithStandardBoxOnNonEmptyNonFullGlassContainer(){
        Box box = standardBox;
        glassContainer.addBox(customSmallBox);
        boolean added = glassContainer.addBox(box);
        assertFalse("Added Oversize Box to Container", added);
        assertTrue("Expected 2 Boxes", glassContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithOversizeBoxOnEmptyGlassContainer(){
        Box box = oversizeBox;
        boolean added = glassContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 1 Box", glassContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithOversizeBoxOnFullGlassContainer(){
        Box box = oversizeBox;
        boolean added = fullGlassContainer.addBox(box);
        assertFalse("Added Box to a Full Container", added);
        assertTrue("Expected 4 Box", fullGlassContainer.getCurrentSize() == 4);
    }

    @Test
    public void testaddBoxWithOversizeBoxOnNonEmptyNonFullGlassContainer(){
        Box box = oversizeBox;
        glassContainer.addBox(customSmallBox);
        boolean added = glassContainer.addBox(box);
        assertFalse("Added Oversize Box to Container", added);
        assertTrue("Expected 2 Boxes", glassContainer.getCurrentSize() == 2);
    }

    @Test
    public void testaddBoxWithCustomSmallBoxOnEmptyGlassContainer(){
        Box box = customSmallBox;
        boolean added = glassContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 1 Box", glassContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithCustomSmallBoxOnFullGlassContainer(){
        Box box = customSmallBox;
        boolean added = fullGlassContainer.addBox(box);
        assertFalse("Added Box to a Full Container", added);
        assertTrue("Expected 4 Box", fullGlassContainer.getCurrentSize() == 4);
    }

    @Test
    public void testaddBoxWithCustomSmallBoxOnNonEmptyNonFullGlassContainer(){
        Box box = customSmallBox;
        glassContainer.addBox(box);
        boolean added = glassContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 2 Boxes", glassContainer.getCurrentSize() == 2);
    }

    @Test
    public void testaddBoxWithCustomLargeBoxOnEmptyGlassContainer(){
        Box box = customLargeBox;
        boolean added = glassContainer.addBox(box);
        assertFalse("Added Box to Container", added);
        assertTrue("Expected No Box", glassContainer.getCurrentSize() == 0);
    }

    @Test
    public void testaddBoxWithCustomLargeBoxOnFullGlassContainer(){
        Box box = customLargeBox;
        boolean added = fullGlassContainer.addBox(box);
        assertFalse("Added Box to a Full Container", added);
        assertTrue("Expected 4 Box", fullGlassContainer.getCurrentSize() == 4);
    }

    @Test
    public void testaddBoxWithCustomLargeBoxOnNonEmptyNonFullGlassContainer(){
        Box box = customLargeBox;
        glassContainer.addBox(customSmallBox);
        boolean added = glassContainer.addBox(box);
        assertFalse("Added Box to Container", added);
        assertTrue("Expected 1 Boxes", glassContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithStandardBoxOnEmptyOversizeContainer(){
        Box box = standardBox;
        boolean added = oversizeContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 1 Box", oversizeContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithStandardBoxOnFullOversizeContainer(){
        Box box = standardBox;
        boolean added = fullOversizeContainer.addBox(box);
        assertFalse("Added Box to a Full Container", added);
        assertTrue("Expected 4 Box", fullOversizeContainer.getCurrentSize() == 4);
    }

    @Test
    public void testaddBoxWithStandardBoxOnNonEmptyNonFullOversizeContainer(){
        Box box = standardBox;
        oversizeContainer.addBox(oversizeBox);
        boolean added = oversizeContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 2 Boxes", oversizeContainer.getCurrentSize() == 2);
    }

    @Test
    public void testaddBoxWithOversizeBoxOnEmptyOversizeContainer(){
        Box box = standardBox;
        boolean added = oversizeContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 1 Box", oversizeContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithOversizeBoxOnFullOversizeContainer(){
        Box box = oversizeBox;
        boolean added = fullOversizeContainer.addBox(box);
        assertFalse("Added Box to a Full Container", added);
        assertTrue("Expected 4 Box", fullOversizeContainer.getCurrentSize() == 4);
    }

    @Test
    public void testaddBoxWithOversizeBoxOnNonEmptyNonFullOversizeContainer(){
        Box box = oversizeBox;
        oversizeContainer.addBox(oversizeBox);
        boolean added = oversizeContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 2 Boxes", oversizeContainer.getCurrentSize() == 2);
    }

    @Test
    public void testaddBoxWithCustomSmallBoxOnEmptyOversizeContainer(){
        Box box = customSmallBox;
        boolean added = oversizeContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 1 Box", oversizeContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithCustomSmallBoxOnFullOversizeContainer(){
        Box box = customSmallBox;
        boolean added = fullOversizeContainer.addBox(box);
        assertFalse("Added Box to a Full Container", added);
        assertTrue("Expected 4 Box", fullOversizeContainer.getCurrentSize() == 4);
    }

    @Test
    public void testaddBoxWithCustomSmallBoxOnNonEmptyNonFullOversizeContainer(){
        Box box = customSmallBox;
        oversizeContainer.addBox(oversizeBox);
        boolean added = oversizeContainer.addBox(box);
        assertTrue("Couldn't add Box to Container", added);
        assertTrue("Expected 2 Boxes", oversizeContainer.getCurrentSize() == 2);
    }

    @Test
    public void testaddBoxWithCustomLargeBoxOnEmptyOversizeContainer(){
        Box box = customLargeBox;
        boolean added = oversizeContainer.addBox(box);
        assertFalse("Couldn't add Box to Container", added);
        assertTrue("Expected No Box", oversizeContainer.getCurrentSize() == 0);
    }

    @Test
    public void testaddBoxWithCustomLargeBoxOnFullOversizeContainer(){
        Box box = customLargeBox;
        boolean added = fullOversizeContainer.addBox(box);
        assertFalse("Added Box to a Full Container", added);
        assertTrue("Expected 4 Box", fullOversizeContainer.getCurrentSize() == 4);
    }

    @Test
    public void testaddBoxWithCustomLargeBoxOnNonEmptyNonFullOversizeContainer(){
        Box box = customLargeBox;
        oversizeContainer.addBox(oversizeBox);
        boolean added = oversizeContainer.addBox(box);
        assertFalse("Couldn't add Custom Box to Container", added);
        assertTrue("Expected 1 Boxes", oversizeContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithStandardBoxOnEmptyCustomContainer(){
        Box box = standardBox;
        boolean added = customContainer.addBox(box);
        assertFalse("Added Box to Custom Container", added);
        assertTrue("Expected 1 Box", customContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithStandardBoxOnFullCustomContainer(){
        Box box = standardBox;
        boolean added = customContainer.addBox(box);
        assertFalse("Added Box to a Full Custom Container", added);
        assertTrue("Expected 4 Box", customContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithStandardBoxOnNonEmptyNonFullCustomContainer(){
        // Doesn't Exist? Custom Container's are made for a Custom Box.
    }

    @Test
    public void testaddBoxWithOversizeBoxOnEmptyCustomContainer(){
        Box box = oversizeBox;
        boolean added = customContainer.addBox(box);
        assertFalse("Added Box to Custom Container", added);
        assertTrue("Expected 1 Box", customContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithOversizeBoxOnFullCustomContainer(){
        Box box = oversizeBox;
        boolean added = customContainer.addBox(box);
        assertFalse("Added Box to a Full Custom Container", added);
        assertTrue("Expected 4 Box", customContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithOversizeBoxOnNonEmptyNonFullCustomContainer(){
        // Doesn't Exist? Custom Container's are made for a Custom Box.
    }

    @Test
    public void testaddBoxWithCustomSmallBoxOnEmptyCustomContainer(){
        Box box = customSmallBox;
        boolean added = customContainer.addBox(box);
        assertFalse("Added Box to Custom Container", added);
        assertTrue("Expected 1 Box", customContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithCustomSmallBoxOnFullCustomContainer(){
        Box box = customSmallBox;
        boolean added = customContainer.addBox(box);
        assertFalse("Added Box to a Full Custom Container", added);
        assertTrue("Expected 4 Box", customContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithCustomSmallBoxOnNonEmptyNonFullCustomContainer(){
        // Doesn't Exist? Custom Container's are made for a Custom Box.
    }

    @Test
    public void testaddBoxWithCustomLargeBoxOnEmptyCustomContainer(){
        Box box = customLargeBox;
        boolean added = customContainer.addBox(box);
        assertTrue("Couldn't add Box to Custom Container", added);
        assertTrue("Expected 1 Box", customContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithCustomLargeBoxOnFullCustomContainer(){
        Box box = customLargeBox;
        boolean added = customContainer.addBox(box);
        assertFalse("Added Box to a Full Custom Container", added);
        assertTrue("Expected 4 Box", customContainer.getCurrentSize() == 1);
    }

    @Test
    public void testaddBoxWithCustomLargeBoxOnNonEmptyNonFullCustomContainer(){
        // Doesn't Exist? Custom Container's are made for a Custom Box.
    }
    
    // ---------------- testing addArt ----------------
    // Since the method returns whether it added or not we tested that boolean.
    // Test adding Mirror Art and Non MirrorArt on 6 Containers. For Crates we 
    // also checked to add Mirrors to Empty, Full, NonEmptyNonFull Crates.
    // Total Tests: 16; 6 Containers (3 fullness Options on 1), 2 Arts,
    @Test
    public void testaddArtWithMirrorArtOnNullContainer(){
        boolean added = nullContainer.addArt(mirrorArt);
        assertFalse("Can not add Mirror on anything but Crates",added);
    }

    @Test
    public void testaddArtWithNonMirrorArtOnNullContainer(){
        boolean added = nullContainer.addArt(nonMirrorArt);
        assertFalse("Can not add anything Non Mirror on Containers",added);
    }

    @Test
    public void testaddArtWithMirrorArtOnPalletContainer(){
        boolean added = palletContainer.addArt(mirrorArt);
        assertFalse("Can not add Mirror on anything but Crates",added);
    }

    @Test
    public void testaddArtWithNonMirrorArtOnPalletContainer(){
        boolean added = palletContainer.addArt(nonMirrorArt);
        assertFalse("Can not add anything Non Mirror on Containers",added);
    }

    @Test
    public void testaddArtWithMirrorArtOnEmptyCrateContainer(){
        boolean added = crateContainer.addArt(mirrorArt);
        assertTrue("Can not add anything Non Mirror on Containers",added);
    }

    @Test
    public void testaddArtWithMirrorArtOnFullCrateContainer(){
        for (int i=0; i < MIRROR_CRATE_LIMIT; i++){
            crateContainer.addArt(mirrorArt);
        }
        boolean added = crateContainer.addArt(mirrorArt);
        assertFalse("Can not add Mirror on a full Container",added);
    }

    @Test
    public void testaddArtWithMirrorArtOnNonEmptyNonFullCrateContainer(){
        for (int i=0; i < MIRROR_CRATE_LIMIT - 5; i++){
            crateContainer.addArt(mirrorArt);
        }
        boolean added = crateContainer.addArt(mirrorArt);
        assertTrue("Can not add anything Non Mirror on Containers",added);
    }

    @Test
    public void testaddArtWithNonMirrorArtOnEmptyCrateContainer(){
        boolean added = crateContainer.addArt(nonMirrorArt);
        assertFalse("Can not add anything Non Mirror on Containers",added);
    }

    @Test
    public void testaddArtWithNonMirrorArtOnFullCrateContainer(){
        for (int i=0; i < MIRROR_CRATE_LIMIT; i++){
            crateContainer.addArt(mirrorArt);
        }
        boolean added = crateContainer.addArt(nonMirrorArt);
        assertFalse("Can not add anything Non Mirror on Containers",added);
    }

    @Test
    public void testaddArtWithNonMirrorArtOnNonEmptyNonFullCrateContainer(){
        for (int i=0; i < MIRROR_CRATE_LIMIT - 5; i++){
            crateContainer.addArt(mirrorArt);
        }
        boolean added = crateContainer.addArt(nonMirrorArt);
        assertFalse("Can not add anything Non Mirror on Containers",added);
    }

    @Test
    public void testaddArtWithMirrorArtOnGlassContainer(){
        boolean added = glassContainer.addArt(mirrorArt);
        assertFalse("Can not add Mirror on anything but Crates",added);
    }

    @Test
    public void testaddArtWithNonMirrorArtOnGlassContainer(){
        boolean added = glassContainer.addArt(nonMirrorArt);
        assertFalse("Can not add anything Non Mirror on Containers",added);
    }

    @Test
    public void testaddArtWithMirrorArtOnOversizeContainer(){
        boolean added = oversizeContainer.addArt(mirrorArt);
        assertFalse("Can not add Mirror on anything but Crates",added);
    }

    @Test
    public void testaddArtWithNonMirrorArtOnOversizeContainer(){
        boolean added = oversizeContainer.addArt(nonMirrorArt);
        assertFalse("Can not add anything Non Mirror on Containers",added);
    }

    @Test
    public void testaddArtWithMirrorArtOnCustomContainer(){
        boolean added = customContainer.addArt(mirrorArt);
        assertFalse("Can not add Mirror on anything but Crates",added);
    }

    @Test
    public void testaddArtWithNonMirrorArtOnCustomContainer(){
        boolean added = customContainer.addArt(nonMirrorArt);
        assertFalse("Can not add anything Non Mirror on Containers",added);
    }
    
    // ---------------- testing canBoxFit ----------------
    // Total Tests: 7;
    @Test
    public void testMirrorCrateThrowsException() {
        Container mirrorCrate = new Container(ArchDesign.entities.Container.Type.Crate, true);
        mirrorCrate.addArt(mirrorArt);
        assertThrows("Expected an Throwable Error", IllegalStateException.class, () -> mirrorCrate.canBoxFit(standardBox));
    }

    @Test
    public void testFullContainerCannotFit() {
        assertFalse(fullPalletContainer.canBoxFit(standardBox));
        assertFalse(fullCrateContainer.canBoxFit(standardBox));
        assertFalse(fullGlassContainer.canBoxFit(customSmallBox));
        assertFalse(fullOversizeContainer.canBoxFit(oversizeBox));
    }

    @Test
    public void testCustomContainerRejectsAll() {
        assertFalse(customContainer.canBoxFit(standardBox));
        assertFalse(customContainer.canBoxFit(oversizeBox));
    }

    @Test
    public void testEmptyContainerStandardAndOversize() {
        // Empty container can take standard box
        assertTrue(palletContainer.canBoxFit(standardBox));
        // Empty container can take oversize box and mark it as such
        assertTrue(oversizeContainer.canBoxFit(oversizeBox));
        assertTrue(oversizeContainer.isCarryingOversizeBox());
    }

    @Test
    public void testGlassContainerAcceptsSmallBox() {
        // Should accept small box (glass type)
        assertTrue(glassContainer.canBoxFit(customSmallBox));
    }

    @Test
    public void testPalletRejectsOversizeIfAlmostFull() {
        Container almostFullPallet = new Container(ArchDesign.entities.Container.Type.Pallet, true);
        almostFullPallet.addBox(standardBox);
        almostFullPallet.addBox(standardBox);
        almostFullPallet.addBox(standardBox);
        // now capacity is low — oversize shouldn't fit
        assertFalse(almostFullPallet.canBoxFit(oversizeBox));
    }

    @Test
    public void testStandardFitsInNormalConditions() {
        Container midLoadCrate = new Container(ArchDesign.entities.Container.Type.Crate, true);
        midLoadCrate.addBox(standardBox);
        assertTrue(midLoadCrate.canBoxFit(standardBox));
        assertTrue(midLoadCrate.canBoxFit(oversizeBox));
    }
    
    // ---------------- testing canArtFit ----------------
    @Test(expected = IllegalStateException.class)
    public void testThrowsExceptionIfNotMirrorCrate() {
        crateContainer.addBox(standardBox);
        assertThrows("Expected an Error", IllegalStateException.class, 
        ()->crateContainer.canArtFit(mirrorArt));
    }

    @Test(expected = IllegalStateException.class)
    public void testThrowsExceptionIfArtIsNotMirror() {
        crateContainer.addArt(mirrorArt);
        assertThrows("Expected an Error", IllegalStateException.class, 
        ()->crateContainer.canArtFit(nonMirrorArt));
    }

    @Test
    public void testFullMirrorCrateCannotFit() {
        // Fill crate to capacity
        for (int i=0; i< MIRROR_CRATE_LIMIT; i++){
            crateContainer.addArt(mirrorArt);
        }

        assertTrue(crateContainer.isFull());
        assertFalse(crateContainer.canArtFit(mirrorArt));
    }

    @Test
    public void testMirrorCrateCanFitWhenNotFull() {
        for (int i=0; i< MIRROR_CRATE_LIMIT - 5; i++){
            crateContainer.addArt(mirrorArt);
        }
        assertTrue(crateContainer.canArtFit(mirrorArt));
    }
    
    // ---------------- testing getWeight() ----------------
    // Total Tests: 6
    @Test
    public void testMirrorCrateWeightIncludesArtAndTare() {
        crateContainer.addArt(mirrorArt);
        crateContainer.addArt(mirrorArt);

        double expected = mirrorArt.getWeight() + mirrorArt.getWeight() + OVERHEAD_CRATE_WEIGHT;
        double actual = crateContainer.getWeight();

        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testNonMirrorCrateWeightIncludesBoxesAndTare() {
        crateContainer.addBox(standardBox);
        crateContainer.addBox(standardBox);

        double expected = standardBox.getWeight() + standardBox.getWeight() + OVERHEAD_CRATE_WEIGHT;
        double actual = crateContainer.getWeight();

        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testPalletContainerWeightIncludesBoxesAndTare() {
        palletContainer.addBox(standardBox);
        double expected = standardBox.getWeight() + OVERHEAD_PALLET_WEIGHT;
        double actual = palletContainer.getWeight();

        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testOversizeContainerWeightIncludesBoxesAndOversizeTare() {
        oversizeContainer.addBox(standardBox);
        double expected = standardBox.getWeight() + OVERHEAD_OVERSIZE_PALLET_WEIGHT;
        double actual = oversizeContainer.getWeight();

        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testEmptyCrateWeightIsJustTare() {
        double expected = OVERHEAD_CRATE_WEIGHT;
        double actual = crateContainer.getWeight();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testEmptyPalletWeightIsJustTare() {
        double expected = OVERHEAD_PALLET_WEIGHT;
        double actual = palletContainer.getWeight();
        assertEquals(expected, actual, 0.001);
    }
    
    // ---------------- testing getCapacity ----------------
    // Total Tests: 8
    @Test
    public void testEmptyContainerThrowsException() {
        assertThrows(IllegalStateException.class, ()->palletContainer.getCapacity());
        assertThrows(IllegalStateException.class, ()->crateContainer.getCapacity());
        assertThrows(IllegalStateException.class, ()->glassContainer.getCapacity());
        assertThrows(IllegalStateException.class, ()->oversizeContainer.getCapacity());

    }

    @Test
    public void testStandardPalletCapacity() {
        palletContainer.addBox(standardBox);
        assertEquals(4, palletContainer.getCapacity());
    }

    @Test
    public void testStandardCrateCapacity() {
        crateContainer.addBox(standardBox);
        assertEquals(4, crateContainer.getCapacity());
    }

    @Test
    public void testGlassContainerCapacity() {
        glassContainer.addBox(standardBox);
        assertEquals(4, glassContainer.getCapacity());
    }

    @Test
    public void testOversizeContainerCapacity() {
        oversizeContainer.addBox(standardBox);
        assertEquals(5, oversizeContainer.getCapacity());
    }

    @Test
    public void testOversizeBoxReducesCapacityByOne() {
        oversizeContainer.addBox(oversizeBox);
        assertEquals(4, oversizeContainer.getCapacity());
    }

    @Test
    public void testMirrorCrateHasMirrorLimit() {
        crateContainer.addArt(mirrorArt);
        int capacity = crateContainer.getCapacity();
        assertEquals(MIRROR_CRATE_LIMIT, capacity);
    }

    @Test
    public void testCustomContainerCapacityIsOne() {
        customContainer.addBox(standardBox);
        assertEquals(1, customContainer.getCapacity());
    }
}
