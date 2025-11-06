package ArchDesign.test_cases.box_packing;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ArchDesign.entities.Art;
import ArchDesign.entities.Client;
import ArchDesign.interactors.Packing;
import ArchDesign.parser.Parser;
import ArchDesign.requests.Request;
import ArchDesign.responses.Response;

public class BoxMixedMediumSameSizeTest {

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

  // =========================================================
  // =============== LARGE BOXES (43 x 43) ===============
  // =========================================================

  @Test
  public void largeBox_1_4PerBox_5_6PerBox() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 1, 1, Canvas - Gallery, 43, 43, n/a, N/A, N/A");
    rows.add("1, 5, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(6, r.getTotalPieces());
    assertEquals(0, r.getStandardBoxCount());
    assertEquals(2, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void largeBox_1_4PerBox_6_6PerBox() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 1, 1, Canvas - Gallery, 43, 43, n/a, N/A, N/A");
    rows.add("1, 6, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(7, r.getTotalPieces());
    assertEquals(0, r.getStandardBoxCount());
    assertEquals(2, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void largeBox_2_4PerBox_4_6PerBox() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 2, 1, Canvas - Gallery, 43, 43, n/a, N/A, N/A");
    rows.add("1, 4, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(6, r.getTotalPieces());
    assertEquals(0, r.getStandardBoxCount());
    assertEquals(2, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void largeBox_2_4PerBox_5_6PerBox() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 2, 1, Canvas - Gallery, 43, 43, n/a, N/A, N/A");
    rows.add("1, 5, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(7, r.getTotalPieces());
    assertEquals(0, r.getStandardBoxCount());
    assertEquals(2, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void largeBox_3_4PerBox_2_6PerBox() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 3, 1, Canvas - Gallery, 43, 43, n/a, N/A, N/A");
    rows.add("1, 2, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(5, r.getTotalPieces());
    assertEquals(0, r.getStandardBoxCount());
    assertEquals(2, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void largeBox_3_4PerBox_3_6PerBox() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 3, 1, Canvas - Gallery, 43, 43, n/a, N/A, N/A");
    rows.add("1, 3, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(6, r.getTotalPieces());
    assertEquals(0, r.getStandardBoxCount());
    assertEquals(2, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void largeBox_3_4PerBox_4_6PerBox() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 3, 1, Canvas - Gallery, 43, 43, n/a, N/A, N/A");
    rows.add("1, 4, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(7, r.getTotalPieces());
    assertEquals(0, r.getStandardBoxCount());
    assertEquals(2, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void largeBox_4_4PerBox_1_6PerBox() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 4, 1, Canvas - Gallery, 43, 43, n/a, N/A, N/A");
    rows.add("1, 1, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(5, r.getTotalPieces());
    assertEquals(0, r.getStandardBoxCount());
    assertEquals(2, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void largeBox_4_4PerBox_2_6PerBox() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 4, 1, Canvas - Gallery, 43, 43, n/a, N/A, N/A");
    rows.add("1, 2, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(6, r.getTotalPieces());
    assertEquals(0, r.getStandardBoxCount());
    assertEquals(2, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  // =========================================================
  // ============== STANDARD BOXES (33 x 43) ===============
  // =========================================================

  @Test
  public void standardbox_1_4PerBox_4_6PerBox() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 1, 1, Canvas - Gallery, 33, 43, n/a, N/A, N/A");
    rows.add("1, 4, 1, Paper Print - Framed, 33, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(5, r.getTotalPieces());
    assertEquals(1, r.getStandardBoxCount());
    assertEquals(0, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void standardbox_1_4PerBox_5_6PerBox() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 1, 1, Canvas - Gallery, 33, 43, n/a, N/A, N/A");
    rows.add("1, 5, 1, Paper Print - Framed, 33, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(6, r.getTotalPieces());
    assertEquals(2, r.getStandardBoxCount());
    assertEquals(0, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void standardbox_1_4PerBox_6_6PerBox() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 1, 1, Canvas - Gallery, 33, 43, n/a, N/A, N/A");
    rows.add("1, 6, 1, Paper Print - Framed, 33, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(7, r.getTotalPieces());
    assertEquals(2, r.getStandardBoxCount());
    assertEquals(0, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void standardbox_2_4PerBox_3_6PerBox() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 2, 1, Canvas - Gallery, 33, 43, n/a, N/A, N/A");
    rows.add("1, 3, 1, Paper Print - Framed, 33, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(5, r.getTotalPieces());
    assertEquals(1, r.getStandardBoxCount());
    assertEquals(0, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void standardbox_2_4PerBox_4_6PerBox() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 2, 1, Canvas - Gallery, 33, 43, n/a, N/A, N/A");
    rows.add("1, 4, 1, Paper Print - Framed, 33, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(6, r.getTotalPieces());
    assertEquals(2, r.getStandardBoxCount());
    assertEquals(0, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void standardbox_3_4PerBox_1_6PerBox() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 3, 1, Canvas - Gallery, 33, 43, n/a, N/A, N/A");
    rows.add("1, 1, 1, Paper Print - Framed, 33, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(4, r.getTotalPieces());
    assertEquals(1, r.getStandardBoxCount());
    assertEquals(0, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void standardbox_3_4PerBox_2_6PerBox() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 3, 1, Canvas - Gallery, 33, 43, n/a, N/A, N/A");
    rows.add("1, 2, 1, Paper Print - Framed, 33, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(5, r.getTotalPieces());
    assertEquals(2, r.getStandardBoxCount());
    assertEquals(0, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void standardbox_3_4PerBox_3_6PerBox() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 3, 1, Canvas - Gallery, 33, 43, n/a, N/A, N/A");
    rows.add("1, 3, 1, Paper Print - Framed, 33, 43, Regular Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(6, r.getTotalPieces());
    assertEquals(2, r.getStandardBoxCount());
    assertEquals(0, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }
    
}
