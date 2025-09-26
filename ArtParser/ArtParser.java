/**
 * ArtParser.java
 *
 * takes input files of required types, extracts all relevant input data
 * returns a project object
 */

// TODO: add parseThenUpdate method later
import ProjectData.Project;

public class ArtParser {

    // general method for parsing input data
    public static Project parse(String requirementsFilePath, String artFilePath) {
        Project project = new Project();
        // Create the two specialized parsers
        ArtFileParser artParser = new ArtFileParser();
        RequirementsFileParser reqParser = new RequirementsFileParser();

        // add project as inputs
        if (artParser.parse(artFilePath, project) != Parser.ParseReturnVals.SUCCESS) {
            return null;
        }
        else if (reqParser.parse(requirementsFilePath, project) != Parser.ParseReturnVals.SUCCESS) {
            return null;
        }
        else return project;
    }
}

