package ArchDesign.entities;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ArchDesign.interactors.Packing;
import ArchDesign.parser.Parser;
import ArchDesign.requests.Request;
import ArchDesign.responses.Response;

public class BoxSameMediumMixedSizeTest {

    // ---------- helpers ----------

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
    Client client = Parser.parseClient("input/Site_requirements.csv");
    Request req = new Request(arts, client);
    Packing pk = new Packing();
    return pk.packEverything(req);
  }

  @Test
  public void large1_standard1_custom1() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 1, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");
    rows.add("1, 1, 1, Paper Print - Framed, 33, 43, Regular Glass, N/A, N/A");
    rows.add("1, 1, 1, Paper Print - Framed, 48, 50, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(3, r.getTotalPieces());
    assertEquals(0, r.getStandardBoxCount());
    assertEquals(1, r.getLargeBoxCount());
    assertEquals(1, r.getCustomPieceCount());
  }

  @Test
  public void large1_standard2() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 1, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");
    rows.add("1, 2, 1, Paper Print - Framed, 33, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(3, r.getTotalPieces());
    assertEquals(0, r.getStandardBoxCount());
    assertEquals(1, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }
  @Test
  public void large2_standard1() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 2, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");
    rows.add("1, 1, 1, Paper Print - Framed, 33, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(3, r.getTotalPieces());
    assertEquals(0, r.getStandardBoxCount());
    assertEquals(1, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void standard1_large2() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 1, 1, Paper Print - Framed, 33, 43, Regular Glass, N/A, N/A");
    rows.add("1, 2, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(3, r.getTotalPieces());
    assertEquals(0, r.getStandardBoxCount());
    assertEquals(1, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }
  
  @Test
  public void standard2_large1() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 2, 1, Paper Print - Framed, 33, 43, Regular Glass, N/A, N/A");
    rows.add("1, 1, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(3, r.getTotalPieces());
    assertEquals(0, r.getStandardBoxCount());
    assertEquals(1, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }
  @Test
  public void large6_standard6() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 3, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");
    rows.add("1, 3, 1, Paper Print - Framed, 33, 43, Regular Glass, N/A, N/A");
    rows.add("1, 3, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");
    rows.add("1, 3, 1, Paper Print - Framed, 33, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(12, r.getTotalPieces());
    assertEquals(1, r.getStandardBoxCount());
    assertEquals(1, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }
    
}
