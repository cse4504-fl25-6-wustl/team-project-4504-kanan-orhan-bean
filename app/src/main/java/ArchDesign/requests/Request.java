package ArchDesign.requests;

import java.util.List;
import java.util.Objects;
import java.util.Collections;
import java.util.ArrayList;

import ArchDesign.entities.Art;
import ArchDesign.entities.Client;

public class Request {

    private final List<Art> arts;
    private final Client client;

    /**
     * @param arts   list of Art objects to be packaged (non-null; may be empty)
     * @param client client/site info (delivery capabilities, service type)
     */
    public Request(List<Art> arts, Client client) {
        // a. Art objects to be packaged up
        // b. Client site information: does the client accept crates or not, etc.
        Objects.requireNonNull(arts, "arts must not be null");
        Objects.requireNonNull(client, "client must not be null");
        List<Art> copy = new ArrayList<>(arts);

        this.arts = Collections.unmodifiableList(copy);
        this.client = client;
    }

    /** getters */
    public List<Art> getArts() {
        return this.arts;
    }

    public Client getClient() {
        return this.client;
    }
}
