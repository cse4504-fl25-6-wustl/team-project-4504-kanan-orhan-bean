import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import entities.Art;
import entities.Art.Glazing;
import entities.Art.Type;
import entities.Client.ServiceType;
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

        System.out.println("Input File Name: " + inputFileName);
        System.out.println("Client File Name: " + inputClientFileName);

        CSVParser CSVparser = new CSVParser();

        ClientParser ClientParser = new ClientParser();

        // List<Art> arts = CSVparser.parseCSV(inputFileName);

        // Client client = ClientParser.parseClient(inputClientFileName);

        Type[] types = new Type[]{
            Type.PaperPrintFramed,
            Type.CanvasFloatFrame,
            Type.PaperPrintFramed,
            Type.WallDecor,
            Type.MetalPrint,
            Type.PaperPrintFramed,
            Type.Mirror
        };

        Glazing[] glazings = new Glazing[]{
            Glazing.Glass,
            Glazing.NoGlaze,
            Glazing.Glass,
            Glazing.NoGlaze,
            Glazing.Glass,
            Glazing.Glass,
            Glazing.NoGlaze
        };

        double[] widths = new double[]{
            31.3750,
            27.0000,
            25.0000,
            32.0000,
            24.0000,
            42.5000,
            36.1250
        };

        double[] heights = new double[]{
            45.3750,
            27.0000,
            49.0000,
            32.0000,
            36.0000,
            30.5000,
            48.1250
        };

        int[] hardwares = new int[]{
            4,
            3,
            4,
            3,
            3,
            3,
            4
        };

        List<Art> arts = new ArrayList<>();

        for (int i = 0; i < 7; i++){
            Art art = new Art(types[i], glazings[i], i, widths[i], heights[i], hardwares[i]);
            arts.add(art);
        }

        ServiceType serviceType = ServiceType.DELIVERY_AND_INSTALLATION;

        Client client = new Client("Chevy Chase, MD", "MedStar", true, 
        true, true, true, true, serviceType);

        Request request = new Request(arts, client);

        Packing packing = new Packing();

        Response response = packing.packEverything(request);

        System.out.println(response.toString());

    }
}