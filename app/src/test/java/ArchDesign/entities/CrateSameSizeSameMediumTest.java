package ArchDesign.entities;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ArchDesign.interactors.Packing;
import ArchDesign.parser.Parser;
import ArchDesign.requests.Request;
import ArchDesign.responses.Response;

public class CrateSameSizeSameMediumTest {

    private List<Art> addArts(List<Art> arts, String row) {
        String[] v = row.split(",");

        int lineNumber = Integer.parseInt(v[0].trim());
        int quantity = Integer.parseInt(v[1].trim());
        int tagNumber = Integer.parseInt(v[2].trim());

        String typeStr = v[3].trim();
        Art.Type type = Art.assignType(typeStr);

        double width = Double.parseDouble(v[4].trim());
        double height = Double.parseDouble(v[5].trim());

        String glazingStr = v[6].trim();
        Art.Glazing glazing = Art.assignGlazingType(glazingStr);

        String frameMoulding = v[7].trim();
        String hardwareStr = v[8].trim();
        int hardware = Art.assignHardware(hardwareStr);

        for (int i = 0; i < quantity; i++) {
            arts.add(new Art(type, glazing, lineNumber, width, height, hardware));
        }
        return arts;
    }

    private Response buildResponse(List<String> rows) {
        List<Art> arts = new ArrayList<>();
        for (String r : rows)
            addArts(arts, r);
        Client client = Parser.parseClient("input/Site_requirements_crate.csv");
        Request req = new Request(arts, client);
        Packing pk = new Packing();
        return pk.packEverything(req);
    }

    private static class ContainerCounts {
        int standardPallets = 0;
        int oversizedPallets = 0;
        int crates = 0;
    }

    private ContainerCounts countContainers(Response r) {
        ContainerCounts cc = new ContainerCounts();
        for (Container c : r.getContainers()) {
            switch (c.getType()) {
                case Pallet:
                    cc.standardPallets++;
                    break;
                case Oversize:
                    cc.oversizedPallets++;
                    break;
                case Crate:
                    cc.crates++;
                    break;
                default:
                    /* Glass/Custom not expected here */ break;
            }
        }
        return cc;
    }

    private void assertCrateScenario(Response r, int expectedPieces, int expectedStdPallets,
            int expectedOverPallets, int expectedCrates) {
        // total pieces = number of arts
        assertEquals(expectedPieces, r.getArts().size());
        ContainerCounts cc = countContainers(r);
        assertEquals(expectedStdPallets, cc.standardPallets);
        assertEquals(expectedOverPallets, cc.oversizedPallets);
        assertEquals(expectedCrates, cc.crates);
    }

    // =========================================================
    // ============ PACK LONG DIRECTION (46 x 58) ==============
    // =========================================================

    @Test
    public void canvas_14_at_46x58__1_crate() {
        List<String> rows = new ArrayList<>();
        rows.add("1, 14, 1, Canvas, 46, 58, N/A, N/A, N/A");

        Response r = buildResponse(rows);
        assertCrateScenario(r, 14, /* std */0, /* over */0, /* crate */1);
    }

    @Test
    public void canvas_15_at_46x58__2_crates() {
        List<String> rows = new ArrayList<>();
        rows.add("1, 15, 1, Canvas, 46, 58, N/A, N/A, N/A");

        Response r = buildResponse(rows);
        assertCrateScenario(r, 15, 0, 0, 2);
    }

    @Test
    public void paperFramedGlass_19_at_46x58__1_crate() {
        List<String> rows = new ArrayList<>();
        rows.add("1, 19, 1, Paper Print - Framed, 46, 58, Regular Glass, N/A, N/A");

        Response r = buildResponse(rows);
        assertCrateScenario(r, 19, 0, 0, 1);
    }

    @Test
    public void paperFramedGlass_20_at_46x58__2_crates() {
        List<String> rows = new ArrayList<>();
        rows.add("1, 20, 1, Paper Print - Framed, 46, 58, Regular Glass, N/A, N/A");

        Response r = buildResponse(rows);
        assertCrateScenario(r, 20, 0, 0, 2);
    }

    // =========================================================
    // ============ PACK SHORT DIRECTION (36 x 48) =============
    // =========================================================

    @Test
    public void canvas_18_at_36x48__1_crate() {
        List<String> rows = new ArrayList<>();
        rows.add("1, 18, 1, Canvas, 36, 48, N/A, N/A, N/A");

        Response r = buildResponse(rows);
        assertCrateScenario(r, 18, 0, 0, 1);
    }

    @Test
    public void canvas_19_at_36x48__2_crates() {
        List<String> rows = new ArrayList<>();
        rows.add("1, 19, 1, Canvas, 36, 48, N/A, N/A, N/A");

        Response r = buildResponse(rows);
        assertCrateScenario(r, 19, 0, 0, 2);
    }

    @Test
    public void paperFramedGlass_25_at_36x48__1_crate() {
        List<String> rows = new ArrayList<>();
        rows.add("1, 25, 1, Paper Print - Framed, 36, 48, Regular Glass, N/A, N/A");

        Response r = buildResponse(rows);
        assertCrateScenario(r, 25, 0, 0, 1);
    }

    @Test
    public void paperFramedGlass_26_at_36x48__2_crates() {
        List<String> rows = new ArrayList<>();
        rows.add("1, 26, 1, Paper Print - Framed, 36, 48, Regular Glass, N/A, N/A");

        Response r = buildResponse(rows);
        assertCrateScenario(r, 26, 0, 0, 2);
    }
}
