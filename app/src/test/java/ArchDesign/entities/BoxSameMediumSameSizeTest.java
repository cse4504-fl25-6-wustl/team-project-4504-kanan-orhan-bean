package ArchDesign.entities;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ArchDesign.interactors.Packing;
import ArchDesign.parser.Parser;
import ArchDesign.requests.Request;
import ArchDesign.responses.Response;

public class BoxSameMediumSameSizeTest {

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
  public void acousticFramed_4_at_43x43_largeBoxes_1() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 4, 1, Acoustic panel - Framed, 43, 43, Z, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(4, r.getTotalPieces());
    assertEquals(0, r.getStandardBoxCount());
    assertEquals(1, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void acousticFramed_5_at_43x43_largeBoxes_2() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 5, 1, Acoustic panel - Framed, 43, 43, Z, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(5, r.getTotalPieces());
    assertEquals(0, r.getStandardBoxCount());
    assertEquals(2, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void acousticFramed_6_at_43x43_largeBoxes_2() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 6, 1, Acoustic panel - Framed, 43, 43, Z, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(6, r.getTotalPieces());
    assertEquals(0, r.getStandardBoxCount());
    assertEquals(2, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void paperFramedGlass_6_at_43x43_largeBoxes_1() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 6, 1, Paper Print - Framed, 43, 43, Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(6, r.getTotalPieces());
    assertEquals(0, r.getStandardBoxCount());
    assertEquals(1, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void paperFramedGlass_8_at_43x43_largeBoxes_2() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 8, 1, Paper Print - Framed, 43, 43, Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(8, r.getTotalPieces());
    assertEquals(0, r.getStandardBoxCount());
    assertEquals(2, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void mirror_10_at_43x43_largeBoxes_2() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 10, 1, Mirror, 43, 43, N/A, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(10, r.getTotalPieces());
    assertEquals(0, r.getStandardBoxCount());
    assertEquals(2, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void mirror_8_at_43x43_largeBoxes_1() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 8, 1, Mirror, 43, 43, N/A, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(8, r.getTotalPieces());
    assertEquals(0, r.getStandardBoxCount());
    assertEquals(1, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  // =========================================================
  // ============== STANDARD BOXES (33 x 43) ===============
  // =========================================================

  @Test
  public void acousticFramed_4_at_33x43_standardBoxes_1() {
    List<String> rows = new ArrayList<>();
    // note: input uses plural "Acoustic panels - Framed" in spec; assignType
    // handles "acoustic"+"framed"
    rows.add("1, 4, 1, Acoustic panels - Framed, 33, 43, Z, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(4, r.getTotalPieces());
    assertEquals(1, r.getStandardBoxCount());
    assertEquals(0, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void canvasFramed_5_at_33x43_standardBoxes_2() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 5, 1, Canvas - Framed, 33, 43, N/A, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(5, r.getTotalPieces());
    assertEquals(2, r.getStandardBoxCount());
    assertEquals(0, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void paperFramedAcrylic_6_at_33x43_standardBoxes_1() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 6, 1, Paper Print - Framed, 33, 43, Acrylic, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(6, r.getTotalPieces());
    assertEquals(1, r.getStandardBoxCount());
    assertEquals(0, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void paperFramedGlass_7_at_33x43_standardBoxes_2() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 7, 1, Paper Print - Framed, 33, 43, Glass, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(7, r.getTotalPieces());
    assertEquals(2, r.getStandardBoxCount());
    assertEquals(0, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void mirror_8_at_33x43_standardBoxes_1() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 8, 1, Mirror, 33, 43, N/A, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(8, r.getTotalPieces());
    assertEquals(1, r.getStandardBoxCount());
    assertEquals(0, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }

  @Test
  public void mirror_9_at_33x43_standardBoxes_2() {
    List<String> rows = new ArrayList<>();
    rows.add("1, 9, 1, Mirror, 33, 43, N/A, N/A, N/A");

    Response r = buildResponse(rows);
    assertEquals(9, r.getTotalPieces());
    assertEquals(2, r.getStandardBoxCount());
    assertEquals(0, r.getLargeBoxCount());
    assertEquals(0, r.getCustomPieceCount());
  }
}
