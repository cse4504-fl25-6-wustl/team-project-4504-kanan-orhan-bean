// import java.util.Map;
// import java.util.HashMap;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;


public class ArtParser {
    
    // Member variables to store the Excel file paths
    private String requirementsFilePath;
    private String artFilePath;
    // member variables to store the parsed input
    private ArtCollection collection;
    private Client client;

    // possible outcomes of parsing method 
    public static enum ParseReturnVals {
        SUCCESS,
        FILE_NOT_FOUND,
        PARSE_ERROR,
        INVALID_FORMAT
    }
    
    // Constructor to initialize with two file paths
    public ArtParser(String requirementsFilePath, String artFilePath) {
        this.requirementsFilePath = requirementsFilePath;
        this.artFilePath = artFilePath;
    }

    // parent method for parsing
    // TODO: later make this static so you can just pass in the filepaths without making instance of art parser
    public Project parse() {
        if ((parseArtFile() == ParseReturnVals.SUCCESS) && (parseRequirementsFile() == ParseReturnVals.SUCCESS)) {
            return new Project(this.client, this.collection);
        }
        else return null;
    }

    // Private helper method for parsing art files
    private ParseReturnVals parseArtFile() {
        ArtCollection collection = new ArtCollection();   // start with empty collection

        try (FileInputStream fis = new FileInputStream(artFilePath);
            Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // assume first sheet
            // formatter returns a string in the same format (display style) as shown in the excel sheet
            DataFormatter formatter = new DataFormatter();

            // Skip header row (row 0), start at row 1
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue; // skip empty rows

                try {
                    int lineNumber   = Integer.parseInt(formatter.formatCellValue(row.getCell(0)));
                    int quantity     = Integer.parseInt(formatter.formatCellValue(row.getCell(1)));
                    int tagNumber    = Integer.parseInt(formatter.formatCellValue(row.getCell(2)));
                    String medium    = formatter.formatCellValue(row.getCell(3));
                    double width     = Double.parseDouble(formatter.formatCellValue(row.getCell(4)));
                    double height    = Double.parseDouble(formatter.formatCellValue(row.getCell(5)));

                    // Glazing enum mapping
                    String glazingStr = formatter.formatCellValue(row.getCell(6)).trim().toLowerCase();
                    ArtPiece.GlazingType glazing;
                    if (glazingStr.contains("glass")) {
                        glazing = ArtPiece.GlazingType.REGULAR_GLASS;
                    } else if (glazingStr.contains("acrylic")) {
                        glazing = ArtPiece.GlazingType.ACRYLIC;
                    } else {
                        glazing = ArtPiece.GlazingType.NONE;
                    }

                    String frameMoulding = formatter.formatCellValue(row.getCell(7));
                    String hardware      = formatter.formatCellValue(row.getCell(8));

                    // Build ArtPiece and add to collection
                    ArtPiece piece = new ArtPiece(lineNumber, quantity, tagNumber, medium,
                                                width, height, glazing, frameMoulding, hardware);
                    collection.addArtPiece(piece);

                } catch (Exception e) {
                    System.err.println("Skipping row " + i + " due to parse error: " + e.getMessage());
                }
            }

            return ParseReturnVals.SUCCESS;

        } catch (IOException e) {
            e.printStackTrace();
            return ParseReturnVals.FILE_NOT_FOUND;
        } catch (Exception e) {
            e.printStackTrace();
            return ParseReturnVals.PARSE_ERROR;
        }

    }

    // Private helper method for parsing art files
    private ParseReturnVals parseRequirementsFile() {
        try (FileInputStream fis = new FileInputStream(requirementsFilePath);
            Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            // Row 0 = headers, Row 1 = first data row
            Row row = sheet.getRow(1);
            if (row == null) {
                System.err.println("No data row found in requirements file.");
                return ParseReturnVals.INVALID_FORMAT;
            }

            // Extract values
            String location   = formatter.formatCellValue(row.getCell(0));
            String name       = formatter.formatCellValue(row.getCell(1));

            boolean acceptsPallets       = parseYesNo(formatter.formatCellValue(row.getCell(2)));
            boolean acceptsCrates        = parseYesNo(formatter.formatCellValue(row.getCell(3)));
            boolean loadingDockAccess    = parseYesNo(formatter.formatCellValue(row.getCell(4)));
            boolean liftgateRequired     = parseYesNo(formatter.formatCellValue(row.getCell(5)));
            boolean insideDeliveryNeeded = parseYesNo(formatter.formatCellValue(row.getCell(6)));

            String serviceStr = formatter.formatCellValue(row.getCell(7)).trim().toLowerCase();
            Client.ServiceType serviceType;
            if (serviceStr.contains("delivery") && serviceStr.contains("installation")) {
                serviceType = Client.ServiceType.DELIVERY_AND_INSTALLATION;
            } else if (serviceStr.contains("delivery")) {
                serviceType = Client.ServiceType.DELIVERY;
            } else if (serviceStr.contains("installation")) {
                serviceType = Client.ServiceType.INSTALLATION;
            }
            else {
                serviceType = Client.ServiceType.NONE;
            }

            // Initialize and store client
            this.client = new Client(location, name,
                                    acceptsPallets, acceptsCrates,
                                    loadingDockAccess, liftgateRequired,
                                    insideDeliveryNeeded, serviceType);
            return ParseReturnVals.SUCCESS;

        } catch (IOException e) {
            e.printStackTrace();
            return ParseReturnVals.FILE_NOT_FOUND;
        } catch (Exception e) {
            e.printStackTrace();
            return ParseReturnVals.PARSE_ERROR;
        }
    }
// Helper for "y"/"n" → boolean
private boolean parseYesNo(String val) {
    return val != null && val.trim().equalsIgnoreCase("y");
}




    // OPTIONALS
    // a method to reset/update the files later
    public void setFiles(String filePath1, String filePath2) {
        this.requirementsFilePath = filePath1;
        this.artFilePath = filePath2;
    }
    // Getters if other classes need access
    public String getRequirementsFilePath() {
        return this.requirementsFilePath;
    }
    public String getArtFilePath() {
        return this.artFilePath;
    }


}

