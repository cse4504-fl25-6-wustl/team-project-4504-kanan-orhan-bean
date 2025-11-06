package ArchDesign.entities;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArtStaticHelpersTest {
    // 1) assigning type
    // paper prints, acoustic, canvas, mirrors
    @Test
    public void testArt_staticHelpers_assignType_unknown() {
        // default
        assertEquals(Art.Type.UNKNOWN, Art.assignType(null));
        assertEquals(Art.Type.UNKNOWN, Art.assignType(""));
        assertEquals(Art.Type.UNKNOWN, Art.assignType(" "));
        assertEquals(Art.Type.UNKNOWN, Art.assignType("n/a"));
        assertEquals(Art.Type.UNKNOWN, Art.assignType("unknown"));
        assertEquals(Art.Type.UNKNOWN, Art.assignType("any"));
    }
    @Test
    public void testArt_staticHelpers_assignType_acoustic() {
        // AcousticPanelGalleryWrapped, TwoInchAcousticPanelFloatFrame, OneInchAcousticPanelFloatFrame
        // (acoustic -> float -> two/2)
        assertEquals(Art.Type.AcousticPanelGalleryWrapped, Art.assignType("acoustic"));
        assertEquals(Art.Type.AcousticPanelGalleryWrapped, Art.assignType("ACOUSTIC"));
        assertEquals(Art.Type.AcousticPanelGalleryWrapped, Art.assignType("panel acoustic"));
        assertEquals(Art.Type.AcousticPanelGalleryWrapped, Art.assignType("acousticpanel"));

        assertEquals(Art.Type.OneInchAcousticPanelFloatFrame, Art.assignType("acoustic float"));
        assertEquals(Art.Type.OneInchAcousticPanelFloatFrame, Art.assignType("ACOUSTIC FLOAT"));
        assertEquals(Art.Type.OneInchAcousticPanelFloatFrame, Art.assignType("float panel acoustic"));
        assertEquals(Art.Type.OneInchAcousticPanelFloatFrame, Art.assignType("acousticfloatpanel"));

        assertEquals(Art.Type.TwoInchAcousticPanelFloatFrame, Art.assignType("acoustic float two"));
        assertEquals(Art.Type.TwoInchAcousticPanelFloatFrame, Art.assignType("acoustic float 2"));
        assertEquals(Art.Type.TwoInchAcousticPanelFloatFrame, Art.assignType("ACOUSTIC float TWO"));
        assertEquals(Art.Type.TwoInchAcousticPanelFloatFrame, Art.assignType("two float panel acoustic"));
        assertEquals(Art.Type.TwoInchAcousticPanelFloatFrame, Art.assignType("2acousticpanelfloattwo"));
    }
    @Test
    public void testArt_staticHelpers_assignType_canvas() {
        // CanvasGalleryWrapped, CanvasFramed, CanvasFloatFrame
        // (canvas -> gallery), (print -> float)
        assertEquals(Art.Type.CanvasFramed, Art.assignType("canvas"));
        assertEquals(Art.Type.CanvasFramed, Art.assignType("CANVAS"));
        assertEquals(Art.Type.CanvasFramed, Art.assignType("framedCANVAS"));

        assertEquals(Art.Type.CanvasFloatFrame, Art.assignType("canvas float"));
        assertEquals(Art.Type.CanvasFloatFrame, Art.assignType("CANVAS FLOAT"));
        assertEquals(Art.Type.CanvasFloatFrame, Art.assignType("canvas float FRAME"));
        assertEquals(Art.Type.CanvasFloatFrame, Art.assignType("floatcanvasframe"));

        assertEquals(Art.Type.CanvasGalleryWrapped, Art.assignType("canvas gallery"));
        assertEquals(Art.Type.CanvasGalleryWrapped, Art.assignType("CANVAS GALLERY"));
        assertEquals(Art.Type.CanvasGalleryWrapped, Art.assignType("canvas gallery FRAME"));
        assertEquals(Art.Type.CanvasGalleryWrapped, Art.assignType("gallerycanvasframe"));
    }
    @Test
    public void testArt_staticHelpers_assignType_print() {
    // PrintFloatMount, PaperPrintFramed, printFramedWithTitlePlate
    // (print -> framed -> title), (print -> float)
        assertEquals(Art.Type.PaperPrintFramed, Art.assignType("print framed"));
        assertEquals(Art.Type.PaperPrintFramed, Art.assignType("PRINT FRAMED"));
        assertEquals(Art.Type.PaperPrintFramed, Art.assignType("FRAMED paper print"));
        assertEquals(Art.Type.PaperPrintFramed, Art.assignType("printframed"));

        assertEquals(Art.Type.PrintFramedwithTitlePlate, Art.assignType("print framed title"));
        assertEquals(Art.Type.PrintFramedwithTitlePlate, Art.assignType("PRINT FRAMED TITLE"));
        assertEquals(Art.Type.PrintFramedwithTitlePlate, Art.assignType("FRAMED paper titleplate print"));
        assertEquals(Art.Type.PrintFramedwithTitlePlate, Art.assignType("printframedtitleplate"));

        assertEquals(Art.Type.PrintFloatMount, Art.assignType("print float"));
        assertEquals(Art.Type.PrintFloatMount, Art.assignType("PRINT FLOAT"));
        assertEquals(Art.Type.PrintFloatMount, Art.assignType("FLOAT paper print"));
        assertEquals(Art.Type.PrintFloatMount, Art.assignType("PAPERprintfloat"));
    }
    @Test
    public void testArt_staticHelpers_assignType_mirror() {
        // Mirror, MirrorBeveled
        // (mirror -> beveled)
        assertEquals(Art.Type.Mirror, Art.assignType("mirror"));
        assertEquals(Art.Type.Mirror, Art.assignType("MIRROR"));

        assertEquals(Art.Type.MirrorBeveled, Art.assignType("mirror beveled"));
        assertEquals(Art.Type.MirrorBeveled, Art.assignType("MIRROR BEVELED"));
        assertEquals(Art.Type.MirrorBeveled, Art.assignType("beveledmirror"));
    }
    

    // 2) assigning glazing
    @Test
    public void testArt_staticHelpers_assignGlazing() {
        // glass
        assertEquals(Art.Glazing.Glass, Art.assignGlazingType("glass"));
        assertEquals(Art.Glazing.Glass, Art.assignGlazingType("GLASS"));

        // acrylic
        assertEquals(Art.Glazing.Acrylic, Art.assignGlazingType("acrylic"));
        assertEquals(Art.Glazing.Acrylic, Art.assignGlazingType("ACRYLIC"));

        // no glaze
        assertEquals(Art.Glazing.NoGlaze, Art.assignGlazingType("glaze"));
        assertEquals(Art.Glazing.NoGlaze, Art.assignGlazingType("GLAZE"));
        assertEquals(Art.Glazing.NoGlaze, Art.assignGlazingType("no glaze"));
        assertEquals(Art.Glazing.NoGlaze, Art.assignGlazingType("noglaze"));
        assertEquals(Art.Glazing.NoGlaze, Art.assignGlazingType("no"));
        assertEquals(Art.Glazing.NoGlaze, Art.assignGlazingType("none"));

        // default
        assertEquals(Art.Glazing.UNKNOWN, Art.assignGlazingType(null));
        assertEquals(Art.Glazing.UNKNOWN, Art.assignGlazingType(""));
        assertEquals(Art.Glazing.UNKNOWN, Art.assignGlazingType(" "));
        assertEquals(Art.Glazing.UNKNOWN, Art.assignGlazingType("n/a"));
    }

    // 2) assigning glazing
    @Test
    public void testArt_staticHelpers_assignHardware() {
        // standard
        assertEquals(4, Art.assignHardware("4 pt sec"));
        assertEquals(4, Art.assignHardware("4ptsec"));
        assertEquals(4, Art.assignHardware("4 PT SEC"));
        assertEquals(4, Art.assignHardware("    df4 jfj 4sadf a"));
        assertEquals(4, Art.assignHardware("4"));

        // default
        assertEquals(-1, Art.assignHardware(null));
        assertEquals(-1, Art.assignHardware(""));
        assertEquals(-1, Art.assignHardware(" "));
        assertEquals(-1, Art.assignHardware("n/a"));
        assertEquals(-1, Art.assignHardware("pt sec"));
        assertEquals(-1, Art.assignHardware("pt sec 4"));
        assertEquals(-1, Art.assignHardware("four"));
    }
}
