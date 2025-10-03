/**
 * ClientFileParser.java
 *
 * Concrete implementation of Parser for parsing client specific requirements file.
 * Responsible for reading the file based on type and producing a Client object.
 */

package parser;

// import org.apache.poi.ss.usermodel.*;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

import entities.Client;

// *TODO: add lots of checks for input
// *TODO: add methods for other input file types
public class ClientFileParser extends FileParser {
    private Client client;
    
    protected ClientFileParser() {
        this.client = null;
    }

    protected Client getClient() {
        return this.client;
    }

    // check for type, call appropriate parser
    @Override
    protected ParseReturnVals parse(String filePath) {
                // Normalize case and extract extension
        String extension = Path.of(filePath).getFileName().toString().toLowerCase();

        if (extension.endsWith(".xlsx")) {
            // return parseExcel(filePath);
            System.err.println("excel type not yet supported for parsing");
            return null;
        } else if (extension.endsWith(".csv")) {
            System.err.println("csv type not yet supported for parsing");
            return null;
        } else if (extension.endsWith(".txt")) {
            System.err.println("txt type not yet supported for parsing");
            return null;
        } else {
            return ParseReturnVals.INVALID_FILE_TYPE;
        }
    }

    // private ParseReturnVals parseExcel(String filePath) {
    //     try (FileInputStream fis = new FileInputStream(filePath);
    //         Workbook workbook = new XSSFWorkbook(fis)) {

    //         Sheet sheet = workbook.getSheetAt(0);
    //         DataFormatter formatter = new DataFormatter();

    //         // Row 0 = headers, Row 1 = first data row
    //         Row row = sheet.getRow(1);
    //         if (row == null) {
    //             System.err.println("No data row found in requirements file.");
    //             return ParseReturnVals.INVALID_FORMAT;
    //         }

    //         // Extract values
    //         String location   = formatter.formatCellValue(row.getCell(0));
    //         String name       = formatter.formatCellValue(row.getCell(1));

    //         boolean acceptsPallets       = parseYesNo(formatter.formatCellValue(row.getCell(2)));
    //         boolean acceptsCrates        = parseYesNo(formatter.formatCellValue(row.getCell(3)));
    //         boolean loadingDockAccess    = parseYesNo(formatter.formatCellValue(row.getCell(4)));
    //         boolean liftgateRequired     = parseYesNo(formatter.formatCellValue(row.getCell(5)));
    //         boolean insideDeliveryNeeded = parseYesNo(formatter.formatCellValue(row.getCell(6)));

    //         // service type enum mapping
    //         String serviceStr = formatter.formatCellValue(row.getCell(7)).trim().toLowerCase();
    //         Client.ServiceType serviceType;
    //         if (serviceStr.contains("delivery") && serviceStr.contains("installation")) {
    //             serviceType = Client.ServiceType.DELIVERY_AND_INSTALLATION;
    //         } else if (serviceStr.contains("delivery")) {
    //             serviceType = Client.ServiceType.DELIVERY;
    //         } else if (serviceStr.contains("installation")) {
    //             serviceType = Client.ServiceType.INSTALLATION;
    //         }
    //         else { serviceType = null; }

    //         this.client = new Client(location, name, acceptsPallets, acceptsCrates,
    //                 loadingDockAccess, liftgateRequired, insideDeliveryNeeded, serviceType);
    //         return ParseReturnVals.SUCCESS;

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         System.err.println("ERROR: file not found");
    //         return ParseReturnVals.FILE_NOT_FOUND;
    //     } catch (Exception e) {
    //         System.err.println("ERROR: could not parse excel sheet");
    //         e.printStackTrace();
    //         return ParseReturnVals.PARSE_ERROR;
    //     }
    // }
    // private ParseReturnVals parseCSV(String filePath) {}
    // private ParseReturnVals parseTXT(String filePath) {}

    // Helper for "y"/"n" → boolean
    private boolean parseYesNo(String val) {
        return val != null && val.trim().equalsIgnoreCase("y");
    } 
}
