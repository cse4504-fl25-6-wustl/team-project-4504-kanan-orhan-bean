package ArchDesign.parser;

import ArchDesign.entities.Art;
import ArchDesign.entities.Client;
import ArchDesign.entities.Client.DeliveryCapabilities;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    private final String correctLineInput = "input/parserTesting/LineItemInput.csv";
    private final String simpleLineInput = "input/parserTesting/SimpleLineInput.csv";
    private final String malformattedLineInput = "input/parserTesting/MalformattedLineInput.csv";
    private final String invalidExtensionInput = "input/parserTesting/LineItemInput.invalid";

    private final String correctClientInput = "input/parserTesting/ClientInput.csv";
    private final String malformattedClientInput = "input/parserTesting/MalformattedClientInput.csv";
/*     @Before
    public void setUp() {} */

    // --- Line Items file parsing testing ---
    // 1) invalid file extension
    @Test
    public void testParseArt_invalidExtension() {
        // List<Art> result = Parser.parseArt(invalidExtensionInput);
        // assertNull("Expected null for unsupported line input file extension", result);
        assertThrows("Expected an Error", IllegalArgumentException.class, ()-> Parser.parseArt(invalidExtensionInput));
    }

    // 2) file not found
    @Test
    public void testParseArt_fileNotFound() {
        // List<Art> result = Parser.parseArt("input/parserTesting/nonexistent.csv");
        // assertNull("Expected null for nonexistent line input file name", result);
        assertThrows("Expected an Error", IllegalArgumentException.class, ()-> Parser.parseArt("input/parserTesting/nonexistent.csv"));
    }

    // 3) parsing failure
    @Test
    public void testParseArt_csv_invalidFormat() {
        List<Art> result = Parser.parseArt(malformattedLineInput);
        assertNotNull("List of Arts should not be null for badly formatted input", result);
        assertTrue("List of Arts should be empty for badly formatted input", result.isEmpty());
    }

    // 4) parsing works
    @Test
    public void testParseArt_csv_parsingSuccessful() throws Exception {
        List<Art> result = Parser.parseArt(correctLineInput);
        assertNotNull("List of arts should not be null", result);
        assertEquals(7, result.size());
    }

    // 5) correct values assigned
    @Test
    public void testParseArt_csv_simpleAssignment() throws Exception {
        List<Art> result = Parser.parseArt(simpleLineInput);
        Art artResult = result.get(0);
        assertNotNull("List of arts should not be null", result);
        assertNotNull("art object in list should not be null", artResult);
        assertEquals(2, result.size());
        assertEquals(1, artResult.getLineNumber());
        assertEquals(Art.Type.PaperPrintFramed, artResult.getType());
        assertEquals(31.3, artResult.getWidth(), 0.00001);
        assertEquals(45.3, artResult.getHeight(), 0.00001);
        assertEquals(Art.Glazing.Glass, artResult.getGlazing());
        assertEquals(4, artResult.getHardware());
    }



    // --- Client file parsing testing ---
    // 1) invalid file extension
    @Test
    public void testParseClient_invalidExtension() {
        // Client result = Parser.parseClient(invalidExtensionInput);
        // assertNull("Expected null for unsupported client file extension", result);
        assertThrows("Expected an Error", IllegalArgumentException.class, ()-> Parser.parseClient(invalidExtensionInput));
    }

    // 2) file not found
    @Test
    public void testParseClient_fileNotFound() {
        // Client result = Parser.parseClient("input/parserTesting/nonexistent.csv");
        // assertNull("Expected null for nonexistent client file name", result);
        assertThrows("Expected an Error", IllegalArgumentException.class, ()-> Parser.parseClient("input/parserTesting/nonexistent.csv"));
    }

    // 3) parsing failure
    @Test
    public void testParseClient_csv_invalidFormat() {
        // Client result = Parser.parseClient(malformattedClientInput);
        // assertNull("Expected null for badly formatted client file", result);
        assertThrows("Expected an Error", IllegalArgumentException.class, ()-> Parser.parseClient(malformattedClientInput));
    }

    // 4) correct values assigned
    @Test
    public void testParseClient_csv_simpleAssignment() throws Exception {
        Client result = Parser.parseClient(correctClientInput);
        assertNotNull("Client object should not be null", result);
        assertEquals("Chevy Chase MD", result.getLocation());
        assertEquals("MedStar", result.getName());
        assertEquals(Client.ServiceType.DELIVERY_AND_INSTALLATION, result.getServiceType());
        
        DeliveryCapabilities dcResult = result.getDeliveryCapabilities();
        assertNotNull("Delivery capabilities object of client object should not be null", dcResult);
        assertEquals(false, dcResult.doesAcceptPallets());
        assertEquals(false, dcResult.doesAcceptCrates());
        assertEquals(true, dcResult.hasLoadingDockAccess());
        assertEquals(true, dcResult.isLiftgateRequired());
        assertEquals(true, dcResult.isInsideDeliveryNeeded());
    }


    // --- Delivery Capability boolean parsing testing ---
    @Test
    public void testParseYesNoHelper() {
        assertEquals(true, ClientFileParser.parseYesNo("y"));
        assertEquals(true, ClientFileParser.parseYesNo("Y"));
        assertEquals(true, ClientFileParser.parseYesNo("yes"));
        assertEquals(true, ClientFileParser.parseYesNo("YES"));
        assertEquals(false, ClientFileParser.parseYesNo("n"));
        assertEquals(false, ClientFileParser.parseYesNo("N"));
        assertEquals(false, ClientFileParser.parseYesNo("no"));
        assertEquals(false, ClientFileParser.parseYesNo("NO"));
        assertEquals(false, ClientFileParser.parseYesNo("n/a"));
        assertEquals(false, ClientFileParser.parseYesNo(""));
        assertEquals(false, ClientFileParser.parseYesNo(" "));
        assertEquals(false, ClientFileParser.parseYesNo(null));
    }
}
