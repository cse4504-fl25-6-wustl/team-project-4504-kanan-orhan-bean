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
    int totalPallets = 0;
    int totalOversizePallets = 0;
    int totalCrates = 0;
    for (Container container : response.getContainers()){
      totalContainerWeight += container.getJustContainerWeight();
      totalContainers++;
      if (container.getType() == ArchDesign.entities.Container.Type.Pallet){
        totalPallets++;
      }
      else if (container.getType() == ArchDesign.entities.Container.Type.Oversize){
        totalOversizePallets++;
      }
      else if (container.getType() == ArchDesign.entities.Container.Type.Crate){
        totalCrates++;
      }
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
    assertEquals(0, totalPallets);
    assertEquals(2, totalOversizePallets);
    assertEquals(0, totalCrates);
    assertEquals(150, totalContainerWeight);
    assertEquals(934, response.getTotalWeight());
  }

  @Test
  public void testMainWithExpectedOutput2(){
    Response response = Main.generateResponseForMain("input/Input2.csv", "input/Site_requirements.csv");

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
    int totalPallets = 0;
    int totalOversizePallets = 0;
    int totalCrates = 0;
    for (Container container : response.getContainers()){
      totalContainerWeight += container.getJustContainerWeight();
      totalContainers++;
      if (container.getType() == ArchDesign.entities.Container.Type.Pallet){
        totalPallets++;
      }
      else if (container.getType() == ArchDesign.entities.Container.Type.Oversize){
        totalOversizePallets++;
      }
      else if (container.getType() == ArchDesign.entities.Container.Type.Crate){
        totalCrates++;
      }
    }
    assertEquals(70, totalArtworkPieces);
    assertEquals(70, totalStandardArtworkPieces);
    assertEquals(0, totalOversizedArtworkPieces);
    assertEquals(1120, totalArtworkWeight);
    assertEquals(3, totalContainers);
    assertEquals(3, totalPallets);
    assertEquals(0, totalOversizePallets);
    assertEquals(0, totalCrates);
    assertEquals(180, totalContainerWeight);
    assertEquals(1300, response.getTotalWeight());
  }

  @Test
  public void testMainWithExpectedOutput3(){
    Response response = Main.generateResponseForMain("input/Input3.csv", "input/Site_requirements.csv");

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
    int totalPallets = 0;
    int totalOversizePallets = 0;
    int totalCrates = 0;
    for (Container container : response.getContainers()){
      totalContainerWeight += container.getJustContainerWeight();
      totalContainers++;
      if (container.getType() == ArchDesign.entities.Container.Type.Pallet){
        totalPallets++;
      }
      else if (container.getType() == ArchDesign.entities.Container.Type.Oversize){
        totalOversizePallets++;
      }
      else if (container.getType() == ArchDesign.entities.Container.Type.Crate){
        totalCrates++;
      }
    }
    assertEquals(13, totalArtworkPieces);
    assertEquals(1, totalStandardArtworkPieces);
    assertEquals(2, totalOversizedArtworkPieces);
    int oversize31dash55 = 0;
    int oversize34dash47 = 0;
    for (Art art: oversizedArts){
      if (art.getWidth() == 31 && art.getHeight() == 55){
        assertEquals(17, art.getWeight());
        oversize31dash55++;
      }
      else if (art.getWidth() == 34 && art.getHeight() == 47){
        assertEquals(16, art.getWeight());
        oversize34dash47++;
      }
    }
    assertEquals(1, oversize31dash55);
    assertEquals(1, oversize34dash47);
    assertEquals(187, totalArtworkWeight);
    assertEquals(1, totalContainers);
    assertEquals(1, totalPallets);
    assertEquals(0, totalOversizePallets);
    assertEquals(0, totalCrates);
    assertEquals(60, totalContainerWeight);
    assertEquals(247, response.getTotalWeight());
  }

  @Test
  public void testMainWithExpectedOutput4(){
    Response response = Main.generateResponseForMain("input/Input4.csv", "input/Site_requirements.csv");

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
    int totalPallets = 0;
    int totalOversizePallets = 0;
    int totalCrates = 0;
    for (Container container : response.getContainers()){
      totalContainerWeight += container.getJustContainerWeight();
      totalContainers++;
      if (container.getType() == ArchDesign.entities.Container.Type.Pallet){
        totalPallets++;
      }
      else if (container.getType() == ArchDesign.entities.Container.Type.Oversize){
        totalOversizePallets++;
      }
      else if (container.getType() == ArchDesign.entities.Container.Type.Crate){
        totalCrates++;
      }
    }
    assertEquals(18, totalArtworkPieces);
    assertEquals(18, totalStandardArtworkPieces);
    assertEquals(0, totalOversizedArtworkPieces);
    assertEquals(234, totalArtworkWeight);
    assertEquals(1, totalContainers);
    assertEquals(1, totalPallets);
    assertEquals(0, totalOversizePallets);
    assertEquals(0, totalCrates);
    assertEquals(60, totalContainerWeight);
    assertEquals(294, response.getTotalWeight());
  }
}
