/**
 * ArtFileParser.java
 *
 * Concrete implementation of Parser for parsing art Excel files.
 * Responsible for reading the art file and producing an ArtCollection.
 */

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;

import ProjectData.Project;
import ProjectData.ArtCollection;
import ProjectData.ArtPiece;

// TODO: add lots of checks
public class ArtFileParser extends Parser {

    @Override
    public ParseReturnVals parse(String filePath, Project project) {
        ArtCollection collection = new ArtCollection();   // start with empty collection

        try (FileInputStream fis = new FileInputStream(filePath);
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

            project.setArtCollection(collection);
            return ParseReturnVals.SUCCESS;

        } catch (IOException e) {
            System.err.println("ERROR: file not found");
            e.printStackTrace();
            return ParseReturnVals.FILE_NOT_FOUND;
        } catch (Exception e) {
            System.err.println("ERROR: could not parse excel sheet");
            e.printStackTrace();
            return ParseReturnVals.PARSE_ERROR;
        }
    }

}