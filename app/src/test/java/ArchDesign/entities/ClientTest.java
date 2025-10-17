package ArchDesign.entities;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ClientTest {
    private Client standardClient;
    private Client.DeliveryCapabilities standardDC;

    @Before
    public void setUp() {
        standardClient = new Client("St Louis, MO", "standard Test", true, true, false, false, false, Client.ServiceType.DELIVERY);
        standardDC = standardClient.getDeliveryCapabilities();
    }

    // 1) creation
    @Test
    public void testClient_successfulCreation() {
        assertNotNull("client object shoud not be null", standardClient);
        assertNotNull("delivery capabilities object of client should not be null", standardClient.getDeliveryCapabilities());
    }

    // 2) assignments
    @Test
    public void testClient_successfulAssignment() {
        assertEquals("St Louis, MO", standardClient.getLocation());
        assertEquals("standard Test", standardClient.getName());
        assertEquals(Client.ServiceType.DELIVERY, standardClient.getServiceType());
    }

    @Test
    public void testClient_successfulAssignment_deliveryCapabilities() {
        assertEquals(true, standardDC.doesAcceptPallets());
        assertEquals(true, standardDC.doesAcceptCrates());
        assertEquals(false, standardDC.hasLoadingDockAccess());
        assertEquals(false, standardDC.isLiftgateRequired());
        assertEquals(false, standardDC.isInsideDeliveryNeeded());
    }

    @Test
    public void testClient_ServiceTypeAssignment() {
        // delivery and installation
        assertEquals(Client.ServiceType.DELIVERY_AND_INSTALLATION, Client.assignServiceType("delivery and installation"));
        assertEquals(Client.ServiceType.DELIVERY_AND_INSTALLATION, Client.assignServiceType("deliveryandinstallation"));
        assertEquals(Client.ServiceType.DELIVERY_AND_INSTALLATION, Client.assignServiceType("delivery installation"));
        assertEquals(Client.ServiceType.DELIVERY_AND_INSTALLATION, Client.assignServiceType("deliv & install"));
        assertEquals(Client.ServiceType.DELIVERY_AND_INSTALLATION, Client.assignServiceType("installation and delivery"));
        assertEquals(Client.ServiceType.DELIVERY_AND_INSTALLATION, Client.assignServiceType("INSTALLATION DELIVERY"));
        assertEquals(Client.ServiceType.DELIVERY_AND_INSTALLATION, Client.assignServiceType("DELIV INSTALL"));

        // delivery
        assertEquals(Client.ServiceType.DELIVERY, Client.assignServiceType("delivery"));
        assertEquals(Client.ServiceType.DELIVERY, Client.assignServiceType("DELIVERY"));
        assertEquals(Client.ServiceType.DELIVERY, Client.assignServiceType("deliv"));
        assertEquals(Client.ServiceType.DELIVERY, Client.assignServiceType("DELIV"));

        // installation
        assertEquals(Client.ServiceType.INSTALLATION, Client.assignServiceType("installation"));
        assertEquals(Client.ServiceType.INSTALLATION, Client.assignServiceType("INSTALLATION"));
        assertEquals(Client.ServiceType.INSTALLATION, Client.assignServiceType("install"));
        assertEquals(Client.ServiceType.INSTALLATION, Client.assignServiceType("INSTALL"));

        // default
        assertEquals(Client.ServiceType.UNKNOWN, Client.assignServiceType(null));
        assertEquals(Client.ServiceType.UNKNOWN, Client.assignServiceType(""));
        assertEquals(Client.ServiceType.UNKNOWN, Client.assignServiceType(" "));
        assertEquals(Client.ServiceType.UNKNOWN, Client.assignServiceType("n/a"));
        assertEquals(Client.ServiceType.UNKNOWN, Client.assignServiceType("none"));
    }
}
