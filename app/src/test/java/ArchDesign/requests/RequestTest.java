package ArchDesign.requests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ArchDesign.entities.Art;
import ArchDesign.entities.Art.Glazing;
import ArchDesign.entities.Art.Type;
import ArchDesign.entities.Client;
import ArchDesign.entities.Client.ServiceType;

public class RequestTest {

  private Art art(int line, double w, double h) {
    // Art(Type type, Glazing glazing, int lineNumber, double width, double height,
    // int hardware)
    return new Art(Type.PaperPrintFramed, Glazing.Glass, line, w, h, 0);
  }

  private Client client() {
    // Client(String jobSiteLocation, String clientName,
    // boolean acceptsPallets, boolean acceptsCrates,
    // boolean loadingDockAccess, boolean liftgateRequired,
    // boolean insideDeliveryNeeded, ServiceType serviceType)
    return new Client(
        "St. Louis, MO",
        "Test Client",
        true, // acceptsPallets
        true, // acceptsCrates
        true, // loadingDockAccess
        false, // liftgateRequired
        false, // insideDeliveryNeeded
        ServiceType.DELIVERY);
  }

  @Test
  public void givenArtsAndClient_createsRequestObject() {
    // Arrange
    Art a1 = art(1, 10.0, 12.0);
    Art a2 = art(2, 20.0, 24.0);
    List<Art> arts = new ArrayList<>();
    arts.add(a1);
    arts.add(a2);

    Client c = client();

    // Act
    Request req = new Request(arts, c);

    assertNotNull(req);
    assertSame(c, req.getClient());
    assertEquals(2, req.getArts().size());
    assertSame(a1, req.getArts().get(0));
    assertSame(a2, req.getArts().get(1));
  }
}
