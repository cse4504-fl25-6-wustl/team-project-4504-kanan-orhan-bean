import java.util.List;

import entities.Art;
import entities.Box;
import entities.Container;
import interactors.Packing;
import entities.Client;
import parser.CSVParser;
import requests.Request;
import responses.Response;

public class Main {
    public static void main(String[] args) {

        String inputFileName = ""; //read in from args

        // read ClientParser

        Client client = new Client(); 

        CSVParser parser = new CSVParser();

        List<Art> arts = parser.parseCSV(inputFileName);

        Request request = new Request(arts, client);

        Packing packing = new Packing();

        Response response = packing.packEverything(request);

        System.out.println(response);

    }
}