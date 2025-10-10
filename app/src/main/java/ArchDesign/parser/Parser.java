/**
 * ArtParser.java
 * Author: Bean Royal
 *
 * takes input files of required types, extracts all relevant input data
 * returns appropriate object / data structure for input
 */

// TODO: add parseThenUpdate method later
package ArchDesign.parser;

import java.util.List;
import java.util.ArrayList;
import entities.Art;
import entities.Client;

public class Parser {
    public static List<Art> parseArt(String artFileName) {
        List<Art> arts = new ArrayList<Art>();
        ArtFileParser artParser = new ArtFileParser(arts);
        if (artParser.parse(artFileName) == FileParser.ParseReturnVals.SUCCESS) {
            return artParser.getArt();
        }
        return null;
    }

    public static Client parseClient(String clientFileName) {
        ClientFileParser clientParser = new ClientFileParser();
        if (clientParser.parse(clientFileName) == FileParser.ParseReturnVals.SUCCESS) {
            return clientParser.getClient();
        }
        return null;
    }
}
