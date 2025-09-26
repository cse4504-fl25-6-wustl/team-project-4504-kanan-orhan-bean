/**
 * RequirementsFileParser.java
 *
 * Concrete implementation of Parser for parsing requirements Excel files.
 * Responsible for reading the requirements file and producing a Client.
 */

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;

import ProjectData.Project;
import ProjectData.Client;

// TODO: add lots of checks
public class RequirementsFileParser extends Parser {

    // private Client client;

    @Override
    public ParseReturnVals parse(String filePath, Project project) {
        try (FileInputStream fis = new FileInputStream(filePath);
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


            project.setClient(location, name, acceptsPallets, acceptsCrates,
                    loadingDockAccess, liftgateRequired, insideDeliveryNeeded, serviceType
            );
            return ParseReturnVals.SUCCESS;

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR: file not found");
            return ParseReturnVals.FILE_NOT_FOUND;
        } catch (Exception e) {
            System.err.println("ERROR: could not parse excel sheet");
            e.printStackTrace();
            return ParseReturnVals.PARSE_ERROR;
        }
    }

    // Helper for "y"/"n" → boolean
    private boolean parseYesNo(String val) {
        return val != null && val.trim().equalsIgnoreCase("y");
    }
}