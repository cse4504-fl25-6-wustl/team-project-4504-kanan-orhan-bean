/**
 * ClientSpecific.java
 *
 * finds and returns a client object, which holds client specific business rules
 */

// TODO: add other ways of searching for a client and/or project

import ProjectData.Project;
import ProjectData.Client;

public class ClientSpecific {

    public Client getRules(Project project) {
        return project.getClient();
    }

    // TODO: search database for a client object that has this name
    public Client getRules(String name) {
        System.err.println("ERROR: method not yet implemented");
        return null;
    }
}
