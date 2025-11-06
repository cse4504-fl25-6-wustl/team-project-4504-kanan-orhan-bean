// package ArchDesign.entities;

// import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertFalse;
// import static org.junit.Assert.assertNull;
// import static org.junit.Assert.assertThrows;
// import static org.junit.Assert.assertTrue;

// import org.junit.Before;
// import org.junit.Test;

// import ArchDesign.entities.Art.Glazing;
// import ArchDesign.entities.Art.Type;

// public class ContainerCustomTest {
//     protected Container customContainer;
//     protected Container customContainerNoCrate;
//     protected Container fullCustomContainer;
//     protected Art mirrorArt;
//     protected Art nonMirrorArt;
//     protected Art standardArt;
//     protected Art oversizeArt;
//     protected Art smallCustomArt;
//     protected Art largeCustomArt;
//     protected Box standardBox;
//     protected Box oversizeBox;
//     protected Box customSmallBox;
//     protected Box customLargeBox;

//     protected static final double tooSmallWidthContainer = 34.5;
//     protected static final double standardWidthContainer = 36.5;
//     protected static final double oversizeWidthContainer = 44;
//     protected static final double tooLargeWidthContainer = 45;

//     protected static final double tooSmallWidthArt = 34;
//     protected static final double standardWidthArt = 35.5;
//     protected static final double oversizeWidthArt = 43.5;
//     protected static final double tooLargeWidthArt = 46;

//     // From Box.java
//     protected static final double STANDARD_LENGTH = 37;
//     protected static final double STANDARD_WIDTH = 11;
//     protected static final double STANDARD_HEIGHT = 31;
//     protected static final double OVERSIZE_LENGTH = 44;
//     protected static final double OVERSIZE_WIDTH = 13;
//     protected static final double OVERSIZE_HEIGHT = 48;
//     protected static final double OVERSIZE_BOX_LIMIT = 36;

//     // From Container.java
//     protected final double STANDARD_PALLET_LENGTH = 48;
//     protected final double STANDARD_PALLET_WIDTH = 40;
//     protected final double GLASS_PALLET_LENGTH = 43;
//     protected final double GLASS_PALLET_WIDTH = 35;
//     protected final double OVERSIZE_PALLET_LENGTH = 60;
//     protected final double OVERSIZE_PALLET_WIDTH = 40;
//     protected final double CRATE_LENGTH = 50;
//     protected final double CRATE_WIDTH = 38;
//     protected final double CRATE_HEIGHT_OVERHEAD = 8;
//     protected final double OVERHEAD_PALLET_WEIGHT = 60;
//     protected final double OVERHEAD_OVERSIZE_PALLET_WEIGHT = 75;
//     protected final double OVERHEAD_CRATE_WEIGHT = 125;
//     protected final int MIRROR_CRATE_LIMIT = 24;
//     protected final int NORMAL_GLASS_ACRYLIC_CRATE_LIMIT = 25;
//     protected final int OVERSIZE_GLASS_ACRYLIC_CRATE_LIMIT = 18;
//     protected final int NORMAL_CANVAS_CRATE_LIMIT = 18;
//     protected final int OVERSIZE_CANVAS_CRATE_LIMIT = 12;
//     protected final int CUSTOM_BOX_DIMENSION_LIMIT = 33;

//     // TODO: Figure out where to use these
//     protected final double OVERSIZE_CRATE_LIMIT = 46;
//     protected final double CRATE_RECOMMENDED_HEIGHT_LIMIT = 84;
//     protected final double CRATE_ABSOLUTE_HEIGHT_LIMIT = 102;

//     @Before
//     public void setUp(){
//         customContainer = new Container(ArchDesign.entities.Container.Type.Custom, true);
//         customContainerNoCrate = new Container(ArchDesign.entities.Container.Type.Custom, false);
//         mirrorArt = new Art(Type.Mirror, Glazing.NoGlaze, 1, standardWidthArt, standardWidthArt, 3);
//         nonMirrorArt = new Art(Type.PaperPrintFramed, Glazing.Glass, 2, standardWidthArt, standardWidthArt, 3);
//         standardArt = new Art(Type.PaperPrintFramed, Glazing.Glass, 3, standardWidthArt, standardWidthArt, 3);
//         oversizeArt = new Art(Type.PaperPrintFramed, Glazing.Glass, 4, oversizeWidthArt, oversizeWidthArt, 3);
//         smallCustomArt = new Art(Type.PaperPrintFramed, Glazing.Glass, 5, tooSmallWidthArt, tooSmallWidthArt, 3);
//         largeCustomArt = new Art(Type.PaperPrintFramed, Glazing.Glass, 6, tooLargeWidthArt, tooLargeWidthArt, 3);
//         standardBox = Box.createBoxForArt(standardArt);
//         oversizeBox = Box.createBoxForArt(oversizeArt);
//         customSmallBox = Box.createBoxForArt(smallCustomArt);
//         customLargeBox = Box.createBoxForArt(largeCustomArt);
//         fullCustomContainer = new Container(ArchDesign.entities.Container.Type.Custom, true);
//         fullCustomContainer.addBox(customLargeBox);
//         assertEquals("Container Type: " + fullCustomContainer.getType()
//         + " | Box isCustom?: " + customLargeBox.isCustom()
//         + " | Container Capacity: " + fullCustomContainer.getCapacity()
//         + " | Current Size " + fullCustomContainer.getCurrentSize()
//         + " | Is Empty? " + fullCustomContainer.isEmpty()
//         + " | Is Full? " + fullCustomContainer.isFull()
//         , 1, 1);
//     }

//     // ---------------- testing constructContainerForArt ----------------
//     // Since the method returns a new Container rather than modifying a 
//     // current Container The different types of containers shouldn't matter, 
//     // but I wanted to test them just in case. And since it takes in Art, but 
//     // it only makes decisions based on whether it is Mirror or not I only 
//     // checked those two Art Types. If it can't accept crates we shouldn't 
//     // be trying to create Container's with Mirrors, but I tested it anyway 
//     // for all containers except CrateNoCrate, because we can't physically 
//     // create a crate with acceptsCrates as false
//     // Total Tests: 22; 6 Containers (2 canAcceptCrate Options on 5), 2 Arts,
//     @Test
//     public void testConstructContainerForArtWithMirrorOnCustomContainer(){
//         Container container = customContainer.constructContainerForArt(mirrorArt);
//         assertTrue("Wrong expected Type. Expected Crate, was given " + container.getType().toString(), 
//         container.getType() == ArchDesign.entities.Container.Type.Crate);
//         assertTrue("Wrong expected is Mirror Crate. Expected Crate to be a Mirror Crate, but it wasn't",
//         container.isMirrorCrate());
//         assertFalse("Expected the Crate to not be empty", container.isEmpty());
//         assertTrue("Expected the Crate to have a capacity of " + MIRROR_CRATE_LIMIT + ", but it was " + container.getCapacity(),
//         container.getCapacity() == MIRROR_CRATE_LIMIT);
//         assertTrue("Expected the first Art in Crate to be mirrorArt, it wasn't", 
//         container.getArts().get(0) == mirrorArt);
//         assertTrue("Expected there to only be one Mirror, there wasn't", 
//         container.getCurrentSize() == 1);
//         assertTrue("Expected the height to be " + CRATE_HEIGHT_OVERHEAD + " more than the Mirror; Container Height: " + container.getHeight()
//         + "Mirror height: " + mirrorArt.getHeight(), container.getHeight() == mirrorArt.getHeight() + CRATE_HEIGHT_OVERHEAD);
//         assertTrue("Expected a standard crate to be " + CRATE_WIDTH + " wide, it wasn't", 
//         container.getWidth() == CRATE_WIDTH);
//         assertTrue("Expected a standard crate to be " + CRATE_LENGTH+ " in length, it wasn't", 
//         container.getLength() == CRATE_LENGTH);
//     }

//     @Test
//     public void testConstructContainerForArtWithNonMirrorOnCustomContainer(){
//         assertThrows("Expected an IllegalArgumentException, but it did not get thrown", 
//         IllegalArgumentException.class, () -> customContainer.constructContainerForArt(nonMirrorArt));
//     }

//     @Test
//     public void testConstructContainerForArtWithMirrorOnCustomContainerNoCrate(){
//         assertThrows("Expected an IllegalArgumentException, but it did not get thrown", 
//         IllegalArgumentException.class, () -> customContainerNoCrate.constructContainerForArt(nonMirrorArt));
//     }

//     @Test
//     public void testConstructContainerForArtWithNonMirrorOnCustomContainerNoCrate(){
//         assertThrows("Expected an IllegalArgumentException, but it did not get thrown", 
//         IllegalArgumentException.class, () -> customContainerNoCrate.constructContainerForArt(nonMirrorArt));
//     }
    
//     // ---------------- testing constructContainerForBox ----------------
//     // Since the method returns a new Container rather than modifying a 
//     // current Container The different types of containers shouldn't matter, 
//     // but I wanted to test them just in case. And since it takes in Box, and 
//     // there are 4 different types of Boxes (Standard, Oversize, Custom, Small 
//     // Enough to fit into a Glass Pallet).
//     // Currently All tests (Except Glass) are failing, because Box.java has a 
//     // broken getWidth(), getLength(), getWeight() function that returns 0.0 
//     // so it creates everything as a Glass Pallet.
//     // Total Tests: 29; 6 Containers (2 canAcceptCrate Options on 5), 4 Boxes,
//     @Test
//     public void testconstructContainerForBoxWithStandardBoxOnCustomContainer(){
//         Box box = standardBox;
//         Container container = customContainer.constructContainerForBox(box);
//         // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
//         assertTrue("Wrong expected Type. Expected Crate, was given " + container.getType().toString() + 
//         " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
//         container.getType() == ArchDesign.entities.Container.Type.Crate);
//         assertTrue("Wrong expected is Mirror Crate. Expected Crate to NOT be a Mirror Crate, but it was",
//         !container.isMirrorCrate());
//         assertFalse("Expected the Crate to not be empty", container.isEmpty());
//         assertTrue("Expected the Crate to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
//         container.getCapacity() == 4);
//         assertTrue("Expected the first Box in Crate to be standardBox, it wasn't", 
//         container.getBoxes().get(0) == box);
//         assertTrue("Expected there to only be one Box, there wasn't", 
//         container.getCurrentSize() == 1);
//         // Because it is returning the wrong Height the crate is being set to Glass rather than Pallet so the following tests are failing
//         assertTrue("Expected the height to be " + CRATE_HEIGHT_OVERHEAD + " more than the Box; Container Height: " + container.getHeight()
//         + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight() + CRATE_HEIGHT_OVERHEAD);
//         assertTrue("Expected a standard crate to be " + CRATE_WIDTH + " wide, it wasn't", 
//         container.getWidth() == CRATE_WIDTH);
//         assertTrue("Expected a standard crate to be " + CRATE_LENGTH+ " in length, it wasn't", 
//         container.getLength() == CRATE_LENGTH);
//     }

//     @Test
//     public void testconstructContainerForBoxWithOversizedBoxOnCustomContainer(){
//         Box box = oversizeBox;
//         Container container = customContainer.constructContainerForBox(box);
//         // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
//         assertTrue("Wrong expected Type. Expected Overisze, was given " + container.getType().toString() + 
//         " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
//         container.getType() == ArchDesign.entities.Container.Type.Crate);
//         assertTrue("Wrong expected is Mirror Crate. Expected Oversize to NOT be a Mirror Crate, but it was",
//         !container.isMirrorCrate());
//         assertFalse("Expected the Oversize to not be empty", container.isEmpty());
//         assertTrue("Expected the Oversize to have a capacity of " + 3 + ", but it was " + container.getCapacity(),
//         container.getCapacity() == 3);
//         assertTrue("Expected the first Box in Oversize to be oversizeBox, it wasn't", 
//         container.getBoxes().get(0) == box);
//         assertTrue("Expected there to only be one Box, there wasn't", 
//         container.getCurrentSize() == 1);
//         // Because it is returning the wrong Height the crate is being set to Glass rather than Pallet so the following tests are failing
//         assertTrue("Expected the height to be " + CRATE_HEIGHT_OVERHEAD + " more than the Box; Container Height: " + container.getHeight()
//         + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight() + CRATE_HEIGHT_OVERHEAD);
//         assertTrue("Expected a standard crate to be " + CRATE_WIDTH + " wide, it wasn't", 
//         container.getWidth() == CRATE_WIDTH);
//         assertTrue("Expected a standard crate to be " + CRATE_LENGTH+ " in length, it wasn't", 
//         container.getLength() == CRATE_LENGTH);
//     }

//     @Test
//     public void testconstructContainerForBoxWithCustomSmallBoxOnCustomContainer(){
//         Box box = customSmallBox;
//         Container container = customContainer.constructContainerForBox(box);
//         //Currently We didn't discuss CustomBoxCreation.
//         // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
//         assertTrue("Wrong expected Type. Expected Glass, was given " + container.getType().toString() + 
//         " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
//         container.getType() == ArchDesign.entities.Container.Type.Crate);
//         assertTrue("Wrong expected is Mirror Crate. Expected Glass to NOT be a Mirror Crate, but it was",
//         !container.isMirrorCrate());
//         assertFalse("Expected the Glass to not be empty", container.isEmpty());
//         assertTrue("Expected the Glass to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
//         container.getCapacity() == 4);
//         assertTrue("Expected the first Box in Glass to be customSmallBox, it wasn't", 
//         container.getBoxes().get(0) == box);
//         assertTrue("Expected there to only be one Box, there wasn't", 
//         container.getCurrentSize() == 1);
//         // Because it is returning the wrong Height the crate is being set to Glass rather than Pallet so the following tests are failing
//         assertTrue("Expected the height to be " + CRATE_HEIGHT_OVERHEAD + " more than the Mirror; Container Height: " + container.getHeight()
//         + "Mirror height: " + box.getHeight(), container.getHeight() == box.getHeight() + CRATE_HEIGHT_OVERHEAD);
//         assertTrue("Expected a standard crate to be " + CRATE_WIDTH + " wide, it wasn't", 
//         container.getWidth() == CRATE_WIDTH);
//         assertTrue("Expected a standard crate to be " + CRATE_LENGTH+ " in length, it wasn't", 
//         container.getLength() == CRATE_LENGTH);
//     }

//     @Test
//     public void testconstructContainerForBoxWithCustomLargeBoxOnCustomContainer(){
//         Box box = customLargeBox;
//         Container container = customContainer.constructContainerForBox(box);
//         assertNull("Should return null. Bri handles Custom Boxes", container);
//         // assertTrue("Wrong expected Type. Expected Custom, was given " + container.getType().toString() + 
//         // " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
//         // container.getType() == ArchDesign.entities.Container.Type.Custom);
//         // assertTrue("Wrong expected is Mirror Crate. Expected Custom to NOT be a Mirror Crate, but it was",
//         // !container.isMirrorCrate());
//         // assertFalse("Expected the Custom to not be empty", container.isEmpty());
//         // assertTrue("Expected the Custom to have a capacity of " + 1 + ", but it was " + container.getCapacity(),
//         // container.getCapacity() == 1);
//         // assertTrue("Expected the first Box in Custom to be customLargeBox, it wasn't", 
//         // container.getBoxes().get(0) == box);
//         // assertTrue("Expected there to only be one Box, there wasn't", 
//         // container.getCurrentSize() == 1);
//         // assertTrue("Expected the height to be same as the Box; Container Height: " + container.getHeight()
//         // + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
//         // assertTrue("Expected a Custom to be at least " + CUSTOM_BOX_DIMENSION_LIMIT + " wide in one direction, it wasn't"
//         //  + " Width: " + container.getWidth() + " Length: " + container.getLength(), 
//         //  container.getWidth() >= CUSTOM_BOX_DIMENSION_LIMIT || container.getLength() >= CUSTOM_BOX_DIMENSION_LIMIT);
//     }
    
//     @Test
//     public void testconstructContainerForBoxWithStandardBoxOnCustomContainerWithNoCrate(){
//         Box box = standardBox;
//         Container container = customContainerNoCrate.constructContainerForBox(box);
//         // Mistake with the Box.java getWidth and getHeight functions. It returns 0.0
//         assertTrue("Wrong expected Type. Expected Pallet, was given " + container.getType().toString() + 
//         " Box Length: " + box.getWidth() + " Box Height: " + box.getHeight(), 
//         container.getType() == ArchDesign.entities.Container.Type.Pallet);
//         assertTrue("Wrong expected is Mirror Pallet. Expected Pallet to NOT be a Mirror Pallet, but it was",
//         !container.isMirrorCrate());
//         assertFalse("Expected the Pallet to not be empty", container.isEmpty());
//         assertTrue("Expected the Pallet to have a capacity of " + 4 + ", but it was " + container.getCapacity(),
//         container.getCapacity() == 4);
//         assertTrue("Expected the first Box in Pallet to be standardBox, it wasn't", 
//         container.getBoxes().get(0) == box);
//         assertTrue("Expected there to only be one Box, there wasn't", 
//         container.getCurrentSize() == 1);
//         // Because it is returning the wrong Height the Pallet is being set to Glass rather than Pallet so the following tests are failing
//         assertTrue("Expected the height to be the same as the Box; Container Height: " + container.getHeight()
//         + "Box height: " + box.getHeight(), container.getHeight() == box.getHeight());
//         assertTrue("Expected a standard Pallet to be " + STANDARD_PALLET_WIDTH + " wide, it wasn't", 
//         container.getWidth() == STANDARD_PALLET_WIDTH);
//         assertTrue("Expected a standard Pallet to be " + STANDARD_PALLET_LENGTH+ " in length, it wasn't", 
//         container.getLength() == STANDARD_PALLET_LENGTH);
//     }

//     // ---------------- testing addBox ----------------
//     // Since the method returns a boolean and modifies the boxes instance variable
//     // We tested this with 4 different types of Boxes, once on Null Container, and
//     // Thrice (Empty, Full, NonEmptyNonFull) on the other 5 containers.
//     // Total Tests: 64; 6 Containers (3 Fullness Options on 5), 4 Boxes,
//     @Test
//     public void testaddBoxWithStandardBoxOnEmptyCustomContainer(){
//         Box box = standardBox;
//         boolean added = customContainer.addBox(box);
//         assertFalse("Added Box to Custom Container", added);
//         assertTrue("Expected 0 Box", customContainer.getCurrentSize() == 0);
//     }

//     @Test
//     public void testaddBoxWithStandardBoxOnFullCustomContainer(){
//         Box box = standardBox;
//         boolean added = fullCustomContainer.addBox(box);
//         assertFalse("Added Box to a Full Custom Container", added);
//         assertTrue("Expected 4 Box", fullCustomContainer.getCurrentSize() == 1);
//     }

//     @Test
//     public void testaddBoxWithOversizeBoxOnEmptyCustomContainer(){
//         Box box = oversizeBox;
//         boolean added = customContainer.addBox(box);
//         assertFalse("Added Box to Custom Container", added);
//         assertTrue("Expected 0 Box", customContainer.getCurrentSize() == 0);
//     }

//     @Test
//     public void testaddBoxWithOversizeBoxOnFullCustomContainer(){
//         Box box = oversizeBox;
//         boolean added = fullCustomContainer.addBox(box);
//         assertFalse("Added Box to a Full Custom Container", added);
//         assertTrue("Expected 4 Box", fullCustomContainer.getCurrentSize() == 1);
//     }

//     @Test
//     public void testaddBoxWithCustomSmallBoxOnEmptyCustomContainer(){
//         Box box = customSmallBox;
//         boolean added = customContainer.addBox(box);
//         assertFalse("Added Box to Custom Container", added);
//         assertTrue("Expected 0 Box", customContainer.getCurrentSize() == 0);
//     }

//     @Test
//     public void testaddBoxWithCustomSmallBoxOnFullCustomContainer(){
//         Box box = customSmallBox;
//         boolean added = fullCustomContainer.addBox(box);
//         assertFalse("Added Box to a Full Custom Container", added);
//         assertTrue("Expected 4 Box", fullCustomContainer.getCurrentSize() == 1);
//     }

//     @Test
//     public void testaddBoxWithCustomLargeBoxOnEmptyCustomContainer(){
//         Box box = customLargeBox;
//         boolean added = customContainer.addBox(box);
//         assertTrue("Couldn't add Box to Custom Container", added);
//         assertTrue("Expected 1 Box", customContainer.getCurrentSize() == 1);
//     }

//     @Test
//     public void testaddBoxWithCustomLargeBoxOnFullCustomContainer(){
//         Box box = customLargeBox;
//         boolean added = fullCustomContainer.addBox(box);
//         assertFalse("Added Box to a Full Custom Container", added);
//         assertTrue("Expected 1 Box", fullCustomContainer.getCurrentSize() == 1);
//     }
    
//     // ---------------- testing addArt ----------------
//     // Since the method returns whether it added or not we tested that boolean.
//     // Test adding Mirror Art and Non MirrorArt on 6 Containers. For Crates we 
//     // also checked to add Mirrors to Empty, Full, NonEmptyNonFull Crates.
//     // Total Tests: 16; 6 Containers (3 fullness Options on 1), 2 Arts,
//     @Test
//     public void testaddArtWithMirrorArtOnCustomContainer(){
//         boolean added = customContainer.addArt(mirrorArt);
//         assertFalse("Can not add Mirror on anything but Crates",added);
//     }

//     @Test
//     public void testaddArtWithNonMirrorArtOnCustomContainer(){
//         boolean added = customContainer.addArt(nonMirrorArt);
//         assertFalse("Can not add anything Non Mirror on Containers",added);
//     }
    
//     // ---------------- testing canBoxFit ----------------
//     // Total Tests: 7;
//     @Test
//     public void testCustomContainerRejectsAll() {
//         assertFalse(customContainer.canBoxFit(standardBox));
//         assertFalse(customContainer.canBoxFit(oversizeBox));
//     }
    
//     // ---------------- testing canArtFit ----------------
//     // Total Tests: 4
//     @Test
//     public void testThrowsExceptionIfNotMirrorCrate() {
//         customContainer.addBox(standardBox);
//         assertThrows("Expected an Error", IllegalStateException.class, 
//         ()->customContainer.canArtFit(mirrorArt));
//     }

//     @Test
//     public void testThrowsExceptionIfArtIsNotMirror() {
//         customContainer.addArt(mirrorArt);
//         assertThrows("Expected an Error", IllegalStateException.class, 
//         ()->customContainer.canArtFit(nonMirrorArt));
//     }
    
//     // ---------------- testing getWeight() ----------------
//     // Total Tests: 6
    
//     // ---------------- testing getCapacity ----------------
//     // Total Tests: 8
//     @Test
//     public void testCustomContainerCapacityIsOne() {
//         customContainer.addBox(customLargeBox);
//         assertEquals(1, customContainer.getCapacity());
//     }
// }
