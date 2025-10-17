package ArchDesign;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ArchDesign.entities.Art;
import ArchDesign.entities.Container;
import ArchDesign.responses.Response;

public class MainTest {

  private PrintStream originalOut;
  private ByteArrayOutputStream out;

  @Before
  public void setUp() {
    originalOut = System.out;
    out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
  }

  @After
  public void tearDown() {
    System.setOut(originalOut);
  }

  private String runMain(String artCsv, String clientCsv) {
    String[] args = new String[] { artCsv, clientCsv };
    Main.main(args);
    return new String(out.toByteArray(), StandardCharsets.UTF_8);
  }

  @Test
  public void main_withInput1_printsExpectedSummary() {
    String output = runMain("input/Input1.csv", "input/Site_requirements.csv");

    // basic echoes
    assertTrue(output.contains("Input File Name: input/Input1.csv"));
    assertTrue(output.contains("Client File Name: input/Site_requirements.csv"));

    // summary markers
    assertTrue(output.contains("=== PACKING SUMMARY ==="));
    assertTrue(output.contains("Items:"));
    assertTrue(output.contains("Boxes:"));
    assertTrue(output.contains("Containers:"));
    assertTrue(output.contains("TOTAL SHIPMENT WEIGHT:"));

    assertTrue(output.trim().length() > 50);
  }

  // @Test
  public void main_withInput2_printsExpectedSummary() {
    String output = runMain("input/Input2.csv", "input/Site_requirements.csv");

    assertTrue(output.contains("Input File Name: input/Input2.csv"));
    assertTrue(output.contains("Client File Name: input/Site_requirements.csv"));
    assertTrue(output.contains("=== PACKING SUMMARY ==="));
    assertTrue(output.contains("TOTAL SHIPMENT WEIGHT:"));
    assertTrue(output.trim().length() > 50);
  }

  // @Test
  public void main_withMissingArgs_doesNotCrash() {
    assertThrows(ArrayIndexOutOfBoundsException.class, () -> Main.main(new String[] {}));
  }

  @Test
  public void testMainWithExpectedOutput1(){
    Response response = Main.generateResponseForMain("input/Input1.csv", "input/Site_requirements.csv");

    int totalArtworkWeight = 0;
    int totalArtworkPieces = 0;
    int totalStandardArtworkPieces = 0;
    int totalOversizedArtworkPieces = 0;
    List<Art> oversizedArts = new ArrayList<>();
    for (Art art : response.getArts()){
      totalArtworkWeight += art.getWeight();
      totalArtworkPieces++;
      totalStandardArtworkPieces++;
      if (art.isOversized()){
        totalOversizedArtworkPieces++;
        oversizedArts.add(art);
        totalStandardArtworkPieces--;
      }
    }

    int totalContainerWeight = 0;
    int totalContainers = 0;
    for (Container container : response.getContainers()){
      totalContainerWeight += container.getJustContainerWeight();
      totalContainers++;
    }
    
    assertEquals(55, totalArtworkPieces);
    assertEquals(49, totalStandardArtworkPieces);
    assertEquals(6, totalOversizedArtworkPieces);
    int oversize46dash34 = 0;
    int oversize56dash32 = 0;
    int oversize48dash32 = 0;
    for (Art art: oversizedArts){
      if (art.getWidth() == 34 && art.getHeight() == 46){
        assertEquals(16, art.getWeight());
        oversize46dash34++;
      }
      else if (art.getWidth() == 32 && art.getHeight() == 56){
        assertEquals(18, art.getWeight());
        oversize56dash32++;
      }
      else if (art.getWidth() == 32 && art.getHeight() == 48){
        assertEquals(16, art.getWeight());
        oversize48dash32++;
      }
    }
    assertEquals(2, oversize46dash34);
    assertEquals(1, oversize56dash32);
    assertEquals(3, oversize48dash32);
    assertEquals(784, totalArtworkWeight);
    assertEquals(2, totalContainers);
    assertEquals(150, totalContainerWeight);
    assertEquals(934, response.getTotalWeight());
  }
}
