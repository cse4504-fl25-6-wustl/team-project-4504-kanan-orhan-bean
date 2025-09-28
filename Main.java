import java.util.List;

import entities.Art;
import entities.Box;
import entities.Container;
import interactors.Packing;
import entities.Client;
import parser.CSVParser;
import parser.ClientParser;
import requests.Request;
import responses.Response;

public class Main {
    public static void main(String[] args) {

        String inputFileName = args[0]; //read in from args
        String inputClientFileName = args[1]; //read in from args

        CSVParser CSVparser = new CSVParser();

        ClientParser ClientParser = new ClientParser();

        List<Art> arts = CSVparser.parseCSV(inputFileName);

        Client client = ClientParser.parseClient(inputClientFileName);

        Request request = new Request(arts, client);

        Packing packing = new Packing();

        Response response = packing.packEverything(request);

        System.out.println(response.toString());

    }
}