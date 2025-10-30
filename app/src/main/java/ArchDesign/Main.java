package ArchDesign;

import java.util.List;

import ArchDesign.entities.Art;
import ArchDesign.interactors.Packing;
import ArchDesign.entities.Client;
import ArchDesign.parser.Parser;
import ArchDesign.requests.Request;
import ArchDesign.responses.JSONSerializer;
import ArchDesign.responses.CommandLineSerializer;
import ArchDesign.responses.Response;

public class Main {

    private static final String USAGE = String.join(System.lineSeparator(),
            "Usage:",
            "  java -jar app.jar <art_input.csv> <client_input.csv> [output.json]",
            "",
            "Examples:",
            "  java -jar app.jar Input3.csv Client.csv",
            "  java -jar app.jar Input3.csv Client.csv Output3.json");

    public static void main(String[] args) {

        if (args.length < 2 || args.length > 3) {
            System.err.println(USAGE);
            return;
        }

        String inputFileName = args[0];
        String inputClientFileName = args[1];

        Response response = generateResponseForMain(inputFileName, inputClientFileName);

        if (args.length == 3) {
            String outputFileName = args[2];
            new JSONSerializer(response, outputFileName);
        }

        // CLI summary is produced by the serializer (not the interactor)
        CommandLineSerializer cli = new CommandLineSerializer(response);
        System.out.println(cli.getSummary());
    }

    public static Response generateResponseForMain(String inputfile, String clientFile) {
        List<Art> arts = Parser.parseArt(inputfile);
        Client client = Parser.parseClient(clientFile);
        Request request = new Request(arts, client);
        Packing packing = new Packing();
        return packing.packEverything(request);
    }
}
