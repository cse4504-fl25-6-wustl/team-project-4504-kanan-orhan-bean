// TODO: can we structure in a way so Client and ArtCollection can be private objects??

public class Project {
    private Client client;
    private ArtCollection collection;

    public Project(Client client, ArtCollection collection) {
        this.client = client;
        this.collection = collection;
    }

    // getters
    public Client getClient() { return this.client; }
    public ArtCollection getArtCollection() { return this.collection; }

    // setters
    public void setClient(Client client) { this.client = client; }
    public void setArtCollection(ArtCollection collection) { this.collection = collection; }
}
