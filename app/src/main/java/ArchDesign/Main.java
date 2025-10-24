package ArchDesign;
import java.util.List;

import ArchDesign.entities.Art;
import ArchDesign.interactors.Packing;
import ArchDesign.entities.Client;
import ArchDesign.parser.Parser;
import ArchDesign.requests.Request;
import ArchDesign.responses.JSONSerializer;
import ArchDesign.responses.Response;

public class Main {
    public static void main(String[] args) {
        
        // TODO: If CLI Cases:
        // IF CLI < 2: Give Error, too little Args
        // If CLI = 2: Run Normal with Output in Terminal
        // If CLI = 3: Run Normal with Output in Terminal as well as Output to a JSON file
        // If CLI > 3: Give Error, too many Args

        if (args.length < 2 || args.length > 3){
            // TODO: Good Usage Message
            System.out.println("Give Usage Message:");
        }
        else{
            String inputFileName = args[0]; // read in from args
            String inputClientFileName = args[1]; // read in from args

            System.out.println("Input File Name: " + inputFileName);
            System.out.println("Client File Name: " + inputClientFileName);

            Response response = generateResponseForMain(inputFileName, inputClientFileName);
            if (args.length == 3){
                String outputFileName = args[2];
                new JSONSerializer(response, outputFileName);
            }
            System.out.println(response.toString());
        }

    }

    public static Response generateResponseForMain(String inputfile, String clientFile){
        List<Art> arts = Parser.parseArt(inputfile);

        Client client = Parser.parseClient(clientFile);

        Request request = new Request(arts, client);

        Packing packing = new Packing();

        Response response = packing.packEverything(request);

        return response;
    }
}