package ArchDesign.entities;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArtStaticHelpersTest {
    // 1) assigning type
    @Test
    public void testArt_staticHelpers_assignType() {
        //paper print (print)
        assertEquals(Art.Type.PaperPrintFramed, Art.assignType("print"));
        assertEquals(Art.Type.PaperPrintFramed, Art.assignType("PRINT"));
        assertEquals(Art.Type.PaperPrintFramed, Art.assignType("paper print"));
        assertEquals(Art.Type.PaperPrintFramed, Art.assignType("paperprint"));
        assertEquals(Art.Type.PaperPrintFramed, Art.assignType("printframed"));
        assertEquals(Art.Type.PaperPrintFramed, Art.assignType("print framed"));
        assertEquals(Art.Type.PaperPrintFramed, Art.assignType("paperprintframed"));

        // paper print framed with title (print -> title)
        assertEquals(Art.Type.PaperPrintFramedWithTitlePlate, Art.assignType("print title"));
        assertEquals(Art.Type.PaperPrintFramedWithTitlePlate, Art.assignType("TITLE print"));
        assertEquals(Art.Type.PaperPrintFramedWithTitlePlate, Art.assignType("printtitle"));
        assertEquals(Art.Type.PaperPrintFramedWithTitlePlate, Art.assignType("paper print framed with title plate"));
        assertEquals(Art.Type.PaperPrintFramedWithTitlePlate, Art.assignType("paperprintframedwithtitleplate"));

        // metal print (print -> metal OR metal)
        assertEquals(Art.Type.MetalPrint, Art.assignType("metal print"));
        assertEquals(Art.Type.MetalPrint, Art.assignType("print METAL"));
        assertEquals(Art.Type.MetalPrint, Art.assignType("metalprint"));
        assertEquals(Art.Type.MetalPrint, Art.assignType("metal"));
        assertEquals(Art.Type.MetalPrint, Art.assignType("METAL"));

        // canvas float frame (canvas)
        assertEquals(Art.Type.CanvasFloatFrame, Art.assignType("canvas"));
        assertEquals(Art.Type.CanvasFloatFrame, Art.assignType("CANVAS"));
        assertEquals(Art.Type.CanvasFloatFrame, Art.assignType("canvas float FRAME"));
        assertEquals(Art.Type.CanvasFloatFrame, Art.assignType("canvasfloatframe"));

        // wall decor (wall)
        assertEquals(Art.Type.WallDecor, Art.assignType("wall"));
        assertEquals(Art.Type.WallDecor, Art.assignType("WALL"));
        assertEquals(Art.Type.WallDecor, Art.assignType("wall DECOR"));
        assertEquals(Art.Type.WallDecor, Art.assignType("decor wall"));
        assertEquals(Art.Type.WallDecor, Art.assignType("walldecor"));

        // acoustic panel (acoustic)
        assertEquals(Art.Type.AcousticPanel, Art.assignType("acoustic"));
        assertEquals(Art.Type.AcousticPanel, Art.assignType("ACOUSTIC"));
        assertEquals(Art.Type.AcousticPanel, Art.assignType("acoustic PANEL"));
        assertEquals(Art.Type.AcousticPanel, Art.assignType("panel acoustic"));
        assertEquals(Art.Type.AcousticPanel, Art.assignType("acousticpanel"));

        // acoustic panel framed (acoustic -> framed)
        assertEquals(Art.Type.AcousticPanelFramed, Art.assignType("acoustic framed"));
        assertEquals(Art.Type.AcousticPanelFramed, Art.assignType("FRAMED acoustic"));
        assertEquals(Art.Type.AcousticPanelFramed, Art.assignType("acoustic framed PANEL"));
        assertEquals(Art.Type.AcousticPanelFramed, Art.assignType("acousticframedPANEL"));

        // mirror (mirror)
        assertEquals(Art.Type.Mirror, Art.assignType("mirror"));
        assertEquals(Art.Type.Mirror, Art.assignType("MIRROR"));

        // default
        assertEquals(Art.Type.UNKNOWN, Art.assignType(null));
        assertEquals(Art.Type.UNKNOWN, Art.assignType(""));
        assertEquals(Art.Type.UNKNOWN, Art.assignType(" "));
        assertEquals(Art.Type.UNKNOWN, Art.assignType("n/a"));
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
