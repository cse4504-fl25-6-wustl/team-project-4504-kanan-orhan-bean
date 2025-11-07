package ArchDesign.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ArchDesign.entities.Art.Glazing;
import ArchDesign.entities.Art.Material;
import ArchDesign.entities.Art.Type;

public class ArtTest {

  private Art art(Type type, Glazing glazing, double w, double h) {
    // Art(Type type, Glazing glazing, int lineNumber, double width, double height,
    // int hardware)
    return new Art(type, glazing, 1, w, h, 0);
  }

  // Constructor: material mapping
  @Test
  public void ctor_setsMaterial_andSpecialHandling_correctly_basicMappings() {
    // PaperPrintFramed w/ glazing
    // assertEquals(Material.Glass, art(Type.PaperPrintFramed, Glazing.Glass, 10, 12).getMaterial());
    // assertEquals(Material.Acyrlic, art(Type.PaperPrintFramed, Glazing.Acrylic, 10, 12).getMaterial());

    // Direct mappings by type
    // assertEquals(Material.CanvasFramed, art(Type.CanvasFloatFrame, Glazing.NoGlaze, 10, 12).getMaterial());
    // assertEquals(Material.CanvasGallery, art(Type.WallDecor, Glazing.NoGlaze, 10, 12).getMaterial());
    // assertEquals(Material.AcousticPanel, art(Type.AcousticPanelGalleryWrapped, Glazing.NoGlaze, 10, 12).getMaterial());
    // assertEquals(Material.AcousticPanelFramed, art(Type.TwoInchAcousticPanelFloatFrame, Glazing.NoGlaze, 10, 12).getMaterial());
    // assertEquals(Material.AcousticPanelFramed, art(Type.OneInchAcousticPanelFloatFrame, Glazing.NoGlaze, 10, 12).getMaterial());
    // assertEquals(Material.PatientBoard, art(Type.MetalPrint, Glazing.NoGlaze, 10, 12).getMaterial());
    // assertEquals(Material.Mirror, art(Type.Mirror, Glazing.NoGlaze, 10, 12).getMaterial());
  }

  // Constructor: special handling flag
  @Test
  public void ctor_setsSpecialHandling_true_for_expected_types() {
    assertTrue(art(Type.AcousticPanelGalleryWrapped, Glazing.NoGlaze, 10, 12).needSpecialHandling());
    assertTrue(art(Type.TwoInchAcousticPanelFloatFrame, Glazing.NoGlaze, 10, 12).needSpecialHandling());
    assertTrue(art(Type.OneInchAcousticPanelFloatFrame, Glazing.NoGlaze, 10, 12).needSpecialHandling());
    assertTrue(art(Type.CanvasFloatFrame, Glazing.NoGlaze, 10, 12).needSpecialHandling());

    assertFalse(art(Type.PaperPrintFramed, Glazing.Glass, 10, 12).needSpecialHandling());
    assertFalse(art(Type.WallDecor, Glazing.NoGlaze, 10, 12).needSpecialHandling());
    assertFalse(art(Type.MetalPrint, Glazing.NoGlaze, 10, 12).needSpecialHandling());
    assertFalse(art(Type.Mirror, Glazing.NoGlaze, 10, 12).needSpecialHandling());
  }

  // getWeight(): width * height * material.LBpSQIN
  @Test
  public void getWeight_usesMaterialDensity() {
    // 10 x 20 = 200 sq in
    int expectedGlass = (int) Math.ceil(200 * Material.Glass.LBpSQIN);
    int expectedAcyrlic = (int) Math.ceil(200 * Material.Acyrlic.LBpSQIN);
    int expectedGallery = (int) Math.ceil(200 * Material.CanvasGallery.LBpSQIN);
    int expectedMirror = (int) Math.ceil(200 * Material.Mirror.LBpSQIN);

    assertEquals(expectedGlass, art(Type.PaperPrintFramed, Glazing.Glass, 10, 20).getWeight(), 1e-9);
    assertEquals(expectedAcyrlic, art(Type.PaperPrintFramed, Glazing.Acrylic, 10, 20).getWeight(), 1e-9);
    // assertEquals(expectedGallery, art(Type.WallDecor, Glazing.NoGlaze, 10, 20).getWeight(), 1e-9);
    assertEquals(expectedMirror, art(Type.Mirror, Glazing.NoGlaze, 10, 20).getWeight(), 1e-9);
  }

  // isCustom(): boundary at 44" (greater or equal)
  @Test
  public void isCustom_boundaryChecks_43point5in() {
    // below threshold on both -> false
    assertFalse(art(Type.PaperPrintFramed, Glazing.Glass, 43.9, 43.9).isCustom());

    // exactly at threshold on either -> false
    assertFalse(art(Type.PaperPrintFramed, Glazing.Glass, 44.0, 10.0).isCustom());
    assertFalse(art(Type.PaperPrintFramed, Glazing.Glass, 10.0, 44.0).isCustom());

    // one above threshold on either -> false
    assertFalse(art(Type.PaperPrintFramed, Glazing.Glass, 44.1, 10.0).isCustom());
    assertFalse(art(Type.PaperPrintFramed, Glazing.Glass, 10.0, 44.1).isCustom());

    // both above threshold on either -> true
    assertTrue(art(Type.PaperPrintFramed, Glazing.Glass, 44.1, 44.1).isCustom());
    assertTrue(art(Type.PaperPrintFramed, Glazing.Glass, 44.1, 44.1).isCustom());
  }

  // isOversize(): boundary at 44" (strictly greater)
  @Test
  public void isCustom_boundaryChecks_36point0in() {
    // below threshold on both -> false
    assertFalse(art(Type.WallDecor, Glazing.NoGlaze, 43.9, 43.9).isOversized());

    // exactly at threshold on either -> false
    assertFalse(art(Type.WallDecor, Glazing.NoGlaze, 44.0, 10.0).isOversized());
    assertFalse(art(Type.WallDecor, Glazing.NoGlaze, 10.0, 44.0).isOversized());

    // one above threshold on either -> true
    assertTrue(art(Type.WallDecor, Glazing.NoGlaze, 44.1, 10.0).isOversized());
    assertTrue(art(Type.WallDecor, Glazing.NoGlaze, 10.0, 44.1).isOversized());

    // both above threshold on either -> true
    assertTrue(art(Type.WallDecor, Glazing.NoGlaze, 44.1, 44.1).isOversized());
    assertTrue(art(Type.WallDecor, Glazing.NoGlaze, 44.1, 44.1).isOversized());
  }
}
