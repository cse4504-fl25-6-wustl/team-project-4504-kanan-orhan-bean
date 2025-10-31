package ArchDesign.entities;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ArchDesign.Main;
import ArchDesign.entities.Art.Glazing;
import ArchDesign.entities.Art.Type;
import ArchDesign.interactors.Packing;
import ArchDesign.parser.Parser;
import ArchDesign.requests.Request;
import ArchDesign.responses.Response;

public class PalletPackingTest {

    public List<Art> addArts(List<Art> arts, String string){
        String[] values = string.split(",");

        // Process each column, extract values
        int lineNumber = Integer.parseInt(values[0].trim());
        int quantity = Integer.parseInt(values[1].trim());
        int tagNumber = Integer.parseInt(values[2].trim());
        
        // Type enum mapping
        String typeStr = values[3].trim();
        Art.Type type = Art.assignType(typeStr);
        
        double width = Double.parseDouble(values[4].trim());
        double height = Double.parseDouble(values[5].trim());

        // Glazing enum mapping
        String glazingStr = values[6].trim();
        Art.Glazing glazing = Art.assignGlazingType(glazingStr);

        String frameMoulding = values[7].trim();

        // hardware int extracting
        String hardwareStr = values[8].trim();
        int hardware = Art.assignHardware(hardwareStr);

        // Build ArtPiece and add to collection
        for (int i=1; i<=quantity; ++i) {
            Art artPiece = new Art(type, glazing, lineNumber, width, height, hardware);
            arts.add(artPiece);
        }
        return arts;
    }

    public Response pallet_art_creator(List<String> strings){
        List<Art> arts = new ArrayList<Art>();
        for (String string : strings){
            addArts(arts, string);
        }
        Client client = Parser.parseClient("input/Site_requirements.csv");
        Request request = new Request(arts, client);
        Packing packing = new Packing();
        Response response = packing.packEverything(request);

        return response;
    }

    @Test
    public void standard_box_15(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 90, 1, Paper Print - Framed, 33, 43, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(90, response.getTotalPieces());
        int totalContainers = 0;
        int totalPallets = 0;
        int totalOversizePallets = 0;
        int totalCrates = 0;
        for (Container container : response.getContainers()){
        totalContainers++;
        if (container.getType() == ArchDesign.entities.Container.Type.Pallet){
            totalPallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Oversize){
            totalOversizePallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Crate){
            totalCrates++;
        }
        }
        assertEquals(0, totalPallets);
        assertEquals(3, totalOversizePallets);
        assertEquals(0, totalCrates);
        assertEquals(0, response.getStandardPalletCount());
        assertEquals(3, response.getOversizedPalletCount());
        assertEquals(0, response.getCrateCount());
    }

    @Test
    public void standard_box_20(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 120, 1, Paper Print - Framed, 33, 43, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(120, response.getTotalPieces());
        int totalContainers = 0;
        int totalPallets = 0;
        int totalOversizePallets = 0;
        int totalCrates = 0;
        for (Container container : response.getContainers()){
        totalContainers++;
        if (container.getType() == ArchDesign.entities.Container.Type.Pallet){
            totalPallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Oversize){
            totalOversizePallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Crate){
            totalCrates++;
        }
        }
        assertEquals(0, totalPallets);
        assertEquals(4, totalOversizePallets);
        assertEquals(0, totalCrates);
        assertEquals(0, response.getStandardPalletCount());
        assertEquals(4, response.getOversizedPalletCount());
        assertEquals(0, response.getCrateCount());
    }

    @Test
    public void standard_box_5(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 30, 1, Paper Print - Framed, 33, 43, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(30, response.getTotalPieces());
        int totalContainers = 0;
        int totalPallets = 0;
        int totalOversizePallets = 0;
        int totalCrates = 0;
        for (Container container : response.getContainers()){
        totalContainers++;
        if (container.getType() == ArchDesign.entities.Container.Type.Pallet){
            totalPallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Oversize){
            totalOversizePallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Crate){
            totalCrates++;
        }
        }
        assertEquals(0, totalPallets);
        assertEquals(1, totalOversizePallets);
        assertEquals(0, totalCrates);
        assertEquals(0, response.getStandardPalletCount());
        assertEquals(1, response.getOversizedPalletCount());
        assertEquals(0, response.getCrateCount());
    }
    
    @Test
    public void standard_box_9(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 54, 1, Paper Print - Framed, 33, 43, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(54, response.getTotalPieces());
        int totalContainers = 0;
        int totalPallets = 0;
        int totalOversizePallets = 0;
        int totalCrates = 0;
        for (Container container : response.getContainers()){
        totalContainers++;
        if (container.getType() == ArchDesign.entities.Container.Type.Pallet){
            totalPallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Oversize){
            totalOversizePallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Crate){
            totalCrates++;
        }
        }
        assertEquals(0, totalPallets);
        assertEquals(2, totalOversizePallets);
        assertEquals(0, totalCrates);
        assertEquals(0, response.getStandardPalletCount());
        assertEquals(2, response.getOversizedPalletCount());
        assertEquals(0, response.getCrateCount());
    }

    @Test
    public void large_box_3(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 18, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(18, response.getTotalPieces());
        int totalContainers = 0;
        int totalPallets = 0;
        int totalOversizePallets = 0;
        int totalCrates = 0;
        for (Container container : response.getContainers()){
        totalContainers++;
        if (container.getType() == ArchDesign.entities.Container.Type.Pallet){
            totalPallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Oversize){
            totalOversizePallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Crate){
            totalCrates++;
        }
        }
        assertEquals(1, totalPallets);
        assertEquals(0, totalOversizePallets);
        assertEquals(0, totalCrates);
        assertEquals(1, response.getStandardPalletCount());
        assertEquals(0, response.getOversizedPalletCount());
        assertEquals(0, response.getCrateCount());
    }

    @Test
    public void large_box_4(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 24, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(24, response.getTotalPieces());
        int totalContainers = 0;
        int totalPallets = 0;
        int totalOversizePallets = 0;
        int totalCrates = 0;
        for (Container container : response.getContainers()){
        totalContainers++;
        if (container.getType() == ArchDesign.entities.Container.Type.Pallet){
            totalPallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Oversize){
            totalOversizePallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Crate){
            totalCrates++;
        }
        }
        assertEquals(2, totalPallets);
        assertEquals(0, totalOversizePallets);
        assertEquals(0, totalCrates);
        assertEquals(2, response.getStandardPalletCount());
        assertEquals(0, response.getOversizedPalletCount());
        assertEquals(0, response.getCrateCount());
    }

    @Test
    public void standard_box_1_large_box_2(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 12, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");
        strings.add("2, 6, 2, Paper Print - Framed, 36, 43, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(18, response.getTotalPieces());
        int totalContainers = 0;
        int totalPallets = 0;
        int totalOversizePallets = 0;
        int totalCrates = 0;
        for (Container container : response.getContainers()){
        totalContainers++;
        if (container.getType() == ArchDesign.entities.Container.Type.Pallet){
            totalPallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Oversize){
            totalOversizePallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Crate){
            totalCrates++;
        }
        }
        assertEquals(": " + response.getBoxes().size(), 1, totalPallets);
        assertEquals(0, totalOversizePallets);
        assertEquals(0, totalCrates);
        assertEquals(1, response.getStandardPalletCount());
        assertEquals(0, response.getOversizedPalletCount());
        assertEquals(0, response.getCrateCount());
    }

    @Test
    public void standard_box_1_large_box_5(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 30, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");
        strings.add("2, 6, 2, Paper Print - Framed, 36, 43, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(36, response.getTotalPieces());
        int totalContainers = 0;
        int totalPallets = 0;
        int totalOversizePallets = 0;
        int totalCrates = 0;
        for (Container container : response.getContainers()){
        totalContainers++;
        if (container.getType() == ArchDesign.entities.Container.Type.Pallet){
            totalPallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Oversize){
            totalOversizePallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Crate){
            totalCrates++;
        }
        }
        assertEquals(2, totalPallets);
        assertEquals(0, totalOversizePallets);
        assertEquals(0, totalCrates);
        assertEquals(2, response.getStandardPalletCount());
        assertEquals(0, response.getOversizedPalletCount());
        assertEquals(0, response.getCrateCount());
    }

    @Test
    public void standard_box_2_large_box_1(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 6, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");
        strings.add("2, 12, 2, Paper Print - Framed, 36, 43, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(18, response.getTotalPieces());
        int totalContainers = 0;
        int totalPallets = 0;
        int totalOversizePallets = 0;
        int totalCrates = 0;
        for (Container container : response.getContainers()){
        totalContainers++;
        if (container.getType() == ArchDesign.entities.Container.Type.Pallet){
            totalPallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Oversize){
            totalOversizePallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Crate){
            totalCrates++;
        }
        }
        assertEquals(1, totalPallets);
        assertEquals(0, totalOversizePallets);
        assertEquals(0, totalCrates);
        assertEquals(1, response.getStandardPalletCount());
        assertEquals(0, response.getOversizedPalletCount());
        assertEquals(0, response.getCrateCount());
    }

    @Test
    public void standard_box_6_large_box_1(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 6, 1, Paper Print - Framed, 43, 43, Regular Glass, N/A, N/A");
        strings.add("2, 36, 2, Paper Print - Framed, 36, 43, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(42, response.getTotalPieces());
        int totalContainers = 0;
        int totalPallets = 0;
        int totalOversizePallets = 0;
        int totalCrates = 0;
        for (Container container : response.getContainers()){
        totalContainers++;
        if (container.getType() == ArchDesign.entities.Container.Type.Pallet){
            totalPallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Oversize){
            totalOversizePallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Crate){
            totalCrates++;
        }
        }
        assertEquals(2, totalPallets);
        assertEquals(0, totalOversizePallets);
        assertEquals(0, totalCrates);
        assertEquals(2, response.getStandardPalletCount());
        assertEquals(0, response.getOversizedPalletCount());
        assertEquals(0, response.getCrateCount());
    }

    @Test
    public void standard_box_4(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 24, 1, Paper Print - Framed, 33, 43, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(24, response.getTotalPieces());
        int totalContainers = 0;
        int totalPallets = 0;
        int totalOversizePallets = 0;
        int totalCrates = 0;
        for (Container container : response.getContainers()){
        totalContainers++;
        if (container.getType() == ArchDesign.entities.Container.Type.Pallet){
            totalPallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Oversize){
            totalOversizePallets++;
        }
        else if (container.getType() == ArchDesign.entities.Container.Type.Crate){
            totalCrates++;
        }
        }
        assertEquals(1, totalPallets);
        assertEquals(0, totalOversizePallets);
        assertEquals(0, totalCrates);
        assertEquals(1, response.getStandardPalletCount());
        assertEquals(0, response.getOversizedPalletCount());
        assertEquals(0, response.getCrateCount());
    }
}
