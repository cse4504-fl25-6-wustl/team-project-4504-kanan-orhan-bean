package ArchDesign.responses;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import ArchDesign.entities.*;
import ArchDesign.interactors.Packing.oversizeObjects;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class JSONSerializerTest {

    private Path tempFile;
    private Art art;
    private Box box;
    private Container pallet;
    private List<Container> containers;
    private oversizeObjects[] oversized_pieces;
    private Response response;

    @Before
    public void setUp() throws IOException {
        // Create a temporary file before each test
        Path outputDir = Path.of("output");
        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }
        tempFile = Files.createTempFile(outputDir, "ShipmentTestOutput", ".json");

        Client client = new Client("", "", false, false, false, false, false, null);

        this.art = new Art(Art.Type.PaperPrintFramed, Art.Glazing.Glass, 1, 25, 25, 4);
        this.box = Box.createBoxForArt(art);
        this.box.addArt(art);
        this.pallet = new Container(Container.Type.Pallet, false);
        this.pallet = pallet.constructContainerForBox(box);
        this.containers = new ArrayList<Container>();
        this.containers.add(pallet);
        this.oversized_pieces = new oversizeObjects[]{new oversizeObjects(10, 45, 1, 25)};
        this.response = new Response(box.getArts(), pallet.getBoxes(), containers, 1, 1, oversized_pieces, 1, 0, 0, 1, 0, 0, 50, 100, 125, box.getArts(), 0, 0, client);
    }

    @After
    public void tearDown() throws IOException {
        // Clean up after each test
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testWritesExpectedJsonToFile() throws IOException {
        // Act
        JSONSerializer.ShipmentToJSONSummary(response, tempFile.toString());

        // JSONSerializer.ShipmentToJSONSummary(response, "output/Output.json");
        // Path outputFile = Path.of("output", "Output.json");
        // String fileContents = Files.readString(outputFile);

        String fileContents = Files.readString(tempFile);
        JsonObject root = JsonParser.parseString(fileContents).getAsJsonObject();

        assertEquals(1, root.get("total_pieces").getAsInt());
        assertEquals(1, root.get("standard_size_pieces").getAsInt());
        assertEquals(1, root.get("standard_box_count").getAsInt());
        assertEquals(0, root.get("large_box_count").getAsInt());
        assertEquals(0, root.get("custom_piece_count").getAsInt());
        assertEquals(1, root.get("standard_pallet_count").getAsInt());
        assertEquals(0, root.get("oversized_pallet_count").getAsInt());
        assertEquals(0, root.get("crate_count").getAsInt());
        assertEquals(50, root.get("total_artwork_weight").getAsInt());
        assertEquals(100, root.get("total_packaging_weight").getAsInt());
        assertEquals(125, root.get("final_shipment_weight").getAsInt());

        JsonArray oversizedPieces = root.getAsJsonArray("oversized_pieces");
        assertEquals(1, oversizedPieces.size());
        JsonObject piece = oversizedPieces.get(0).getAsJsonObject();
        assertEquals(10, piece.get("side1").getAsInt());
        assertEquals(45, piece.get("side2").getAsInt());
        assertEquals(1, piece.get("quantity").getAsInt());
    }

    @Test
    public void testFileIsCreatedAndNotEmpty() throws IOException {
        // Act
        JSONSerializer.ShipmentToJSONSummary(response, tempFile.toString());

        // Assert
        assertTrue(Files.exists(tempFile));
        assertTrue(Files.size(tempFile) > 0);
    }

    @Test
    public void testHandlesInvalidPathGracefully() {
        String invalidPath = "/invalid/path/output.json";

        // Act
        // Your method catches IOException internally, so it won’t throw.
        JSONSerializer.ShipmentToJSONSummary(response, invalidPath);

        // Assert
        // Nothing to assert here since errors are logged, not thrown.
        // But we can at least confirm the invalid file was not created.
        assertFalse(Files.exists(new java.io.File(invalidPath).toPath()));
    }
}