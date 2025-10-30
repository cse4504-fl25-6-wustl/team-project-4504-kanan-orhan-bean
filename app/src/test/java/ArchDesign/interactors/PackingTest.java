package ArchDesign.interactors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ArchDesign.entities.Art;
import ArchDesign.entities.Art.Glazing;
import ArchDesign.entities.Art.Type;
import ArchDesign.entities.Client;
import ArchDesign.entities.Client.ServiceType;
import ArchDesign.entities.Container;
import ArchDesign.requests.Request;
import ArchDesign.responses.Response;

public class PackingTest {

  private Art art(Type type, Glazing glazing, int line, double w, double h) {
    // Art(Type type, Glazing glazing, int lineNumber, double width, double height,
    // int hardware)
    return new Art(type, glazing, line, w, h, 0);
  }

  private Client client(boolean acceptsPallets, boolean acceptsCrates) {
    // Client(String jobSiteLocation, String clientName,
    // boolean acceptsPallets, boolean acceptsCrates,
    // boolean loadingDockAccess, boolean liftgateRequired,
    // boolean insideDeliveryNeeded, ServiceType serviceType)
    return new Client(
        "St. Louis, MO",
        "QA Client",
        acceptsPallets,
        acceptsCrates,
        true, // loadingDockAccess
        false, // liftgateRequired
        false, // insideDeliveryNeeded
        ServiceType.DELIVERY);
  }

  @Test
  public void packEverything_givenRequest_returnsCoherentResponse() {
    // Arrange: mix of non-mirror + mirror (mirror influences containerization)
    List<Art> arts = new ArrayList<>();
    arts.add(art(Type.WallDecor, Glazing.NoGlaze, 1, 10.0, 20.0));
    arts.add(art(Type.Mirror, Glazing.NoGlaze, 2, 12.0, 12.0));

    // allow pallets; disable crates so every new container must be a Pallet
    Client c = client(true, false);
    Request req = new Request(arts, c);

    // Act
    Packing packing = new Packing();
    Response resp = packing.packEverything(req);

    // Assert: Response object present
    assertNotNull(resp);

    // Arts are echoed back in the response with same elements
    assertEquals(arts.size(), resp.getArts().size());
    assertEquals(arts.get(0), resp.getArts().get(0));
    assertEquals(arts.get(1), resp.getArts().get(1));

    // With pallets allowed (and non-empty arts), we expect at least one container
    assertTrue("Should create at least one container when pallets are allowed",
        resp.getContainers().size() >= 1);

    // Because crates are disabled, all containers must be Pallets
    for (Container cont : resp.getContainers()) {
      assertEquals(Container.Type.Pallet, cont.getType());
    }

    // Total weight rule in Packing: prefer containers' weight sum when > 0
    double sumContainers = 0.0;
    for (Container cont : resp.getContainers()) {
      sumContainers += cont.getWeight();
    }
    assertTrue("Expected positive container weight", sumContainers > 0.0);
    assertEquals(sumContainers, resp.getTotalWeight(), 1e-9);

    // Summary contains expected user-facing markers
    // String summary = resp.getSummary();
    // assertNotNull(summary);
    // assertTrue(summary.contains("=== PACKING SUMMARY ==="));
    // assertTrue(summary.contains("Items:"));
    // assertTrue(summary.contains("Boxes:"));
    // assertTrue(summary.contains("Containers:"));
    // assertTrue(summary.contains("TOTAL SHIPMENT WEIGHT:"));
  }

  @Test
  public void packEverything_nullRequest_throwsNPE() {
    Packing packing = new Packing();
    assertThrows(NullPointerException.class, () -> packing.packEverything(null));
  }
}
