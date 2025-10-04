import java.util.List;

import entities.Art;
import entities.Box;
import entities.Container;
import interactors.Packing;
import entities.Client;
import parser.ClientFileParser;
import parser.Parser;
import requests.Request;
import responses.Response;

public class Main {
    public static void main(String[] args) {

        String inputFileName = args[0]; //read in from args
        String inputClientFileName = args[1]; //read in from args

        System.out.println("Input File Name: " + inputFileName);
        System.out.println("Client File Name: " + inputClientFileName);

        List<Art> arts = Parser.parseArt(inputFileName);
        
        Client client = Parser.parseClient(inputClientFileName);

        Request request = new Request(arts, client);

        Packing packing = new Packing();

        Response response = packing.packEverything(request);

        System.out.println(response.toString());

    }
}