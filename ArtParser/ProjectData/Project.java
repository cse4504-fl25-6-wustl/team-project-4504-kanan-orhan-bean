/**
 * Project.java
 *
 * Stores all input data for a particular projecting, by storing client and artCollection objects
 */

package ProjectData;

// do we need to make 

public class Project {
    private Client client;
    private ArtCollection collection;

    public Project() {
        this.client = null;
        this.collection = null;
    }

    public Project(Client client, ArtCollection collection) {
        this.client = client;
        this.collection = collection;
    }

    // getters
    public Client getClient() { return this.client; }
    public ArtCollection getArtCollection() { return this.collection; }

    // setters
    public void setClient(Client client) { this.client = client; }
    public void setClient(String jobSiteLocation, String clientName,
                  boolean acceptsPallets, boolean acceptsCrates,
                  boolean loadingDockAccess, boolean liftgateRequired,
                  boolean insideDeliveryNeeded, Client.ServiceType serviceType) {
        this.client = new Client(jobSiteLocation, clientName, acceptsPallets, acceptsCrates,
                  loadingDockAccess, liftgateRequired, insideDeliveryNeeded, serviceType);
    }
    public void setArtCollection(ArtCollection collection) { this.collection = collection; }
}
