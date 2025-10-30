package ArchDesign.entities;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ArchDesign.interactors.Packing;
import ArchDesign.parser.Parser;
import ArchDesign.requests.Request;
import ArchDesign.responses.Response;

public class BoxVaryingSizesTest {

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
    public void box_35_5x35_5(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 1, 1, Paper Print - Framed, 35.5, 35.5, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(1, response.getTotalPieces());
        int totalBoxes = 0;
        int totalStandardBoxes = 0;
        int totalOversizeBoxes = 0;
        int totalCustomBoxes = 0;
        for (Box box : response.getBoxes()){
        totalBoxes++;
        if (box.isCustom()){
            totalCustomBoxes++;
        }
        else if (box.isOversized()){
            totalOversizeBoxes++;
        }
        else{
            totalStandardBoxes++;
        }
        }
        assertEquals(1, totalStandardBoxes);
        assertEquals(0, totalOversizeBoxes);
        assertEquals(0, totalCustomBoxes);
    }

    @Test
    public void box_35_5x85(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 1, 1, Paper Print - Framed, 35.5, 85, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(1, response.getTotalPieces());
        int totalBoxes = 0;
        int totalStandardBoxes = 0;
        int totalOversizeBoxes = 0;
        int totalCustomBoxes = 0;
        for (Box box : response.getBoxes()){
        totalBoxes++;
        if (box.isCustom()){
            totalCustomBoxes++;
        }
        else if (box.isOversized()){
            totalOversizeBoxes++;
        }
        else{
            totalStandardBoxes++;
        }
        }
        assertEquals(1, totalStandardBoxes);
        assertEquals(0, totalOversizeBoxes);
        assertEquals(0, totalCustomBoxes);
    }

    @Test
    public void box_36_5x36_5(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 1, 1, Paper Print - Framed, 36.5, 36.5, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(1, response.getTotalPieces());
        int totalBoxes = 0;
        int totalStandardBoxes = 0;
        int totalOversizeBoxes = 0;
        int totalCustomBoxes = 0;
        for (Box box : response.getBoxes()){
        totalBoxes++;
        if (box.isCustom()){
            totalCustomBoxes++;
        }
        else if (box.isOversized()){
            totalOversizeBoxes++;
        }
        else{
            totalStandardBoxes++;
        }
        }
        assertEquals(1, totalStandardBoxes);
        assertEquals(0, totalOversizeBoxes);
        assertEquals(0, totalCustomBoxes);
    }

    @Test
    public void box_36_5x88(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 1, 1, Paper Print - Framed, 36.5, 88, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(1, response.getTotalPieces());
        int totalBoxes = 0;
        int totalStandardBoxes = 0;
        int totalOversizeBoxes = 0;
        int totalCustomBoxes = 0;
        for (Box box : response.getBoxes()){
        totalBoxes++;
        if (box.isCustom()){
            totalCustomBoxes++;
        }
        else if (box.isOversized()){
            totalOversizeBoxes++;
        }
        else{
            totalStandardBoxes++;
        }
        }
        assertEquals(1, totalStandardBoxes);
        assertEquals(0, totalOversizeBoxes);
        assertEquals(0, totalCustomBoxes);
    }

    @Test
    public void box_36x36(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 1, 1, Paper Print - Framed, 36, 36, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(1, response.getTotalPieces());
        int totalBoxes = 0;
        int totalStandardBoxes = 0;
        int totalOversizeBoxes = 0;
        int totalCustomBoxes = 0;
        for (Box box : response.getBoxes()){
        totalBoxes++;
        if (box.isCustom()){
            totalCustomBoxes++;
        }
        else if (box.isOversized()){
            totalOversizeBoxes++;
        }
        else{
            totalStandardBoxes++;
        }
        }
        assertEquals(1, totalStandardBoxes);
        assertEquals(0, totalOversizeBoxes);
        assertEquals(0, totalCustomBoxes);
    }

    @Test
    public void box_36x88(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 1, 1, Paper Print - Framed, 36, 88, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(1, response.getTotalPieces());
        int totalBoxes = 0;
        int totalStandardBoxes = 0;
        int totalOversizeBoxes = 0;
        int totalCustomBoxes = 0;
        for (Box box : response.getBoxes()){
        totalBoxes++;
        if (box.isCustom()){
            totalCustomBoxes++;
        }
        else if (box.isOversized()){
            totalOversizeBoxes++;
        }
        else{
            totalStandardBoxes++;
        }
        }
        assertEquals(1, totalStandardBoxes);
        assertEquals(0, totalOversizeBoxes);
        assertEquals(0, totalCustomBoxes);
    }

    @Test
    public void box_37x88(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 1, 1, Paper Print - Framed, 37, 88, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(1, response.getTotalPieces());
        int totalBoxes = 0;
        int totalStandardBoxes = 0;
        int totalOversizeBoxes = 0;
        int totalCustomBoxes = 0;
        for (Box box : response.getBoxes()){
        totalBoxes++;
        if (box.isCustom()){
            totalCustomBoxes++;
        }
        else if (box.isOversized()){
            totalOversizeBoxes++;
        }
        else{
            totalStandardBoxes++;
        }
        }
        assertEquals(0, totalStandardBoxes);
        assertEquals(1, totalOversizeBoxes);
        assertEquals(0, totalCustomBoxes);
    }

    @Test
    public void box_43_5x43_5(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 1, 1, Paper Print - Framed, 43.5, 43.5, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(1, response.getTotalPieces());
        int totalBoxes = 0;
        int totalStandardBoxes = 0;
        int totalOversizeBoxes = 0;
        int totalCustomBoxes = 0;
        for (Box box : response.getBoxes()){
        totalBoxes++;
        if (box.isCustom()){
            totalCustomBoxes++;
        }
        else if (box.isOversized()){
            totalOversizeBoxes++;
        }
        else{
            totalStandardBoxes++;
        }
        }
        assertEquals(0, totalStandardBoxes);
        assertEquals(1, totalOversizeBoxes);
        assertEquals(0, totalCustomBoxes);
    }

    @Test
    public void box_43_5x88(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 1, 1, Paper Print - Framed, 43.5, 88, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(1, response.getTotalPieces());
        int totalBoxes = 0;
        int totalStandardBoxes = 0;
        int totalOversizeBoxes = 0;
        int totalCustomBoxes = 0;
        for (Box box : response.getBoxes()){
        totalBoxes++;
        if (box.isCustom()){
            totalCustomBoxes++;
        }
        else if (box.isOversized()){
            totalOversizeBoxes++;
        }
        else{
            totalStandardBoxes++;
        }
        }
        assertEquals(0, totalStandardBoxes);
        assertEquals(1, totalOversizeBoxes);
        assertEquals(0, totalCustomBoxes);
    }

    @Test
    public void box_43x88_5(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 1, 1, Paper Print - Framed, 43.5, 88.5, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(1, response.getTotalPieces());
        int totalBoxes = 0;
        int totalStandardBoxes = 0;
        int totalOversizeBoxes = 0;
        int totalCustomBoxes = 0;
        for (Box box : response.getBoxes()){
        totalBoxes++;
        if (box.isCustom()){
            totalCustomBoxes++;
        }
        else if (box.isOversized()){
            totalOversizeBoxes++;
        }
        else{
            totalStandardBoxes++;
        }
        }
        assertEquals(0, totalStandardBoxes);
        assertEquals(0, totalOversizeBoxes);
        assertEquals(1, totalCustomBoxes);
    }

    @Test
    public void box_44x44(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 1, 1, Paper Print - Framed, 44, 44, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(1, response.getTotalPieces());
        int totalBoxes = 0;
        int totalStandardBoxes = 0;
        int totalOversizeBoxes = 0;
        int totalCustomBoxes = 0;
        for (Box box : response.getBoxes()){
        totalBoxes++;
        if (box.isCustom()){
            totalCustomBoxes++;
        }
        else if (box.isOversized()){
            totalOversizeBoxes++;
        }
        else{
            totalStandardBoxes++;
        }
        }
        assertEquals(0, totalStandardBoxes);
        assertEquals(0, totalOversizeBoxes);
        assertEquals(1, totalCustomBoxes);
    }

    @Test
    public void box_88_5x35_5(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 1, 1, Paper Print - Framed, 88.5, 35.5, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(1, response.getTotalPieces());
        int totalBoxes = 0;
        int totalStandardBoxes = 0;
        int totalOversizeBoxes = 0;
        int totalCustomBoxes = 0;
        for (Box box : response.getBoxes()){
        totalBoxes++;
        if (box.isCustom()){
            totalCustomBoxes++;
        }
        else if (box.isOversized()){
            totalOversizeBoxes++;
        }
        else{
            totalStandardBoxes++;
        }
        }
        assertEquals(0, totalStandardBoxes);
        assertEquals(0, totalOversizeBoxes);
        assertEquals(1, totalCustomBoxes);
    }

    @Test
    public void box_88x43_5(){
        List<String> strings = new ArrayList<>();
        strings.add("1, 1, 1, Paper Print - Framed, 88, 43.5, Regular Glass, N/A, N/A");
        Response response = pallet_art_creator(strings);
        assertEquals(1, response.getTotalPieces());
        int totalBoxes = 0;
        int totalStandardBoxes = 0;
        int totalOversizeBoxes = 0;
        int totalCustomBoxes = 0;
        for (Box box : response.getBoxes()){
        totalBoxes++;
        if (box.isCustom()){
            totalCustomBoxes++;
        }
        else if (box.isOversized()){
            totalOversizeBoxes++;
        }
        else{
            totalStandardBoxes++;
        }
        }
        assertEquals(0, totalStandardBoxes);
        assertEquals(1, totalOversizeBoxes);
        assertEquals(0, totalCustomBoxes);
    }

}
