package ArchDesign.responses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ArchDesign.entities.Art;
import ArchDesign.entities.Art.Glazing;
import ArchDesign.entities.Art.Type;
import ArchDesign.entities.Box;
import ArchDesign.entities.Container;

public class ResponseTest {

  private Art art(int line, double w, double h) {
    // Art(Type type, Glazing glazing, int lineNumber, double width, double height,
    // int hardware)
    return new Art(Type.PaperPrintFramed, Glazing.Glass, line, w, h, 0);
  }

  @Test
  public void givenInputs_createsResponseObject() {
    List<Art> arts = new ArrayList<>();
    arts.add(art(1, 10.0, 12.0));

    List<Box> boxes = new ArrayList<>();
    List<Container> containers = new ArrayList<>();

    double totalWeight = 123.45;
    String summary = "Test summary";

    // Act
    Response resp = new Response(arts, boxes, containers, totalWeight, summary);

    // Assert
    assertNotNull(resp);
    assertEquals(1, resp.getArts().size());
    assertEquals(0, resp.getBoxes().size());
    assertEquals(0, resp.getContainers().size());
    assertEquals(totalWeight, resp.getTotalWeight(), 1e-9);
    assertEquals(summary, resp.getSummary());
    assertEquals(summary, resp.toString());
  }
}
