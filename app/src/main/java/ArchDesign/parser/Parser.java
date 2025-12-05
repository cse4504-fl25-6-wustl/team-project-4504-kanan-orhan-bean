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
import ArchDesign.entities.Art;
import ArchDesign.entities.Client;

public class Parser {
    public static List<Art> parseArt(String artFileName) {
        if (artFileName == null || artFileName.trim().isEmpty()) {
            throw new IllegalArgumentException("Error: No art file was selected.");
        }

        List<Art> arts = new ArrayList<Art>();
        ArtFileParser artParser = new ArtFileParser(arts);
        FileParser.ParseReturnVals result = artParser.parse(artFileName);

        switch (result) {
            case SUCCESS:
                return artParser.getArt();
            case FILE_NOT_FOUND:
                throw new IllegalArgumentException("Error: Could not find the art file: " + artFileName);
            case INVALID_FILE_TYPE:
                throw new IllegalArgumentException(
                        "Error: Unsupported art file type for line items. Please upload a .csv file.");
            case INVALID_FORMAT:
                throw new IllegalArgumentException(
                        "Error: The art file format is invalid. Please check the header and columns.");
            case PARSE_ERROR:
                throw new IllegalArgumentException(
                        "Error: Could not parse the art CSV file. Please check that all values are valid.");
            default:
                throw new IllegalArgumentException("Error: An unknown error occurred while parsing the art file.");
        }
    }

    public static Client parseClient(String clientFileName) {
        if (clientFileName == null || clientFileName.trim().isEmpty()) {
            throw new IllegalArgumentException("Error: No client file was selected.");
        }

        ClientFileParser clientParser = new ClientFileParser();
        FileParser.ParseReturnVals result = clientParser.parse(clientFileName);

        switch (result) {
            case SUCCESS:
                return clientParser.getClient();
            case FILE_NOT_FOUND:
                throw new IllegalArgumentException("Error: Could not find the client file: " + clientFileName);
            case INVALID_FILE_TYPE:
                throw new IllegalArgumentException("Error: Unsupported client file type. Please upload a .csv file.");
            case INVALID_FORMAT:
                throw new IllegalArgumentException(
                        "Error: The client file format is invalid. Please make sure it has exactly one data row.");
            case PARSE_ERROR:
                throw new IllegalArgumentException(
                        "Error: Could not parse the client CSV file. Please check that all values are valid.");
            default:
                throw new IllegalArgumentException("Error: An unknown error occurred while parsing the client file.");
        }
    }
}
