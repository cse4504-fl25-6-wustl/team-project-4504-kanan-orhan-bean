package ArchDesign;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
}
