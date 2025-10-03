package requests;

import java.util.List;
import java.util.Objects;
import java.util.Collections;

import entities.Art;
import entities.Client;

public class Request {

    private final List<Art> arts;
    private final Client client;

    /**
     * @param arts   list of Art objects to be packaged (must be non-null; may be
     *               empty)
     * @param client client/site info (delivery capabilities, service type)
     */
    public Request(List<Art> arts, Client client) {
        // a. Art objects to be packaged up
        // b. Client site information: does the client accept crates or not, etc.
        this.arts = Collections.unmodifiableList(Objects.requireNonNull(arts, "arts must not be null"));
        this.client = Objects.requireNonNull(client, "client must not be null");
    }

    /** getters */
    public List<Art> getArts() {
        return arts;
    }

    public Client getClient() {
        return client;
    }
}
