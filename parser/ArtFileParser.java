/**
 * ArtParser.java
 * Author: Bean Royal
 *
 * Concrete implementation of Parser for parsing art files (line item input).
 * Responsible for reading the art file based on its type, and producing a list of art objects.
 */

package parser;

// import org.apache.poi.ss.usermodel.*;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

import java.util.List;

import entities.Art;

// *TODO: add lots of checks for input
// *TODO: add methods for other input file types
public class ArtFileParser extends FileParser {
    private List<Art> artCollection;
    
    protected ArtFileParser(List<Art> artCollection) {
        this.artCollection = artCollection;
    }

    protected List<Art> getArt() {
        return this.artCollection;
    }

    // check for type, call appropriate parser
    @Override
    protected ParseReturnVals parse(String filePath) {
        // Normalize case and extract extension
        String extension = Path.of(filePath).getFileName().toString().toLowerCase();

        if (extension.endsWith(".xlsx")) {
            // return parseExcel(filePath);
            System.err.println("csv type not yet supported for parsing");
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

    //         Sheet sheet = workbook.getSheetAt(0); // assume first sheet
    //         // formatter returns a string in the same format (display style) as shown in the excel sheet
    //         DataFormatter formatter = new DataFormatter();

    //         // Skip header row (row 0), start at row 1
    //         for (int i = 1; i <= sheet.getLastRowNum(); i++) {
    //             Row row = sheet.getRow(i);
    //             if (row == null) continue; // skip empty rows

    //             try {
    //                 int lineNumber   = Integer.parseInt(formatter.formatCellValue(row.getCell(0)));
    //                 int quantity     = Integer.parseInt(formatter.formatCellValue(row.getCell(1)));
    //                 int tagNumber    = Integer.parseInt(formatter.formatCellValue(row.getCell(2)));
                    
    //                 // Type enum mapping
    //                 String typeStr    = formatter.formatCellValue(row.getCell(3));
    //                 Type type = setType(typeStr);
                    
    //                 double width     = Double.parseDouble(formatter.formatCellValue(row.getCell(4)));
    //                 double height    = Double.parseDouble(formatter.formatCellValue(row.getCell(5)));

    //                 // Glazing enum mapping
    //                 String glazingStr = formatter.formatCellValue(row.getCell(6)).trim().toLowerCase();
    //                 GlazingType glazing = setGlazingType(glazingStr);

    //                 String frameMoulding = formatter.formatCellValue(row.getCell(7));

    //                 // hardware int extracting
    //                 String hardwareStr      = formatter.formatCellValue(row.getCell(8));
    //                 int hardware = setHardware(hardwareStr);

    //                 // Build ArtPiece and add to collection
    //                 // FIX LATER
    //                 Art artPiece = new Art(Art.Type.PaperPrint, Art.Glazing.Glass, lineNumber, -1, -1, hardware);
    //                 this.artCollection.add(artPiece);

    //             } catch (Exception e) {
    //                 System.err.println("Skipping row " + i + " due to parse error: " + e.getMessage());
    //             }
    //         }

    //         return ParseReturnVals.SUCCESS;

    //     } catch (IOException e) {
    //         System.err.println("ERROR: file not found");
    //         e.printStackTrace();
    //         return ParseReturnVals.FILE_NOT_FOUND;
    //     } catch (Exception e) {
    //         System.err.println("ERROR: could not parse excel sheet");
    //         e.printStackTrace();
    //         return ParseReturnVals.PARSE_ERROR;
    //     }
    // }
    // private ParseReturnVals parseCSV(String filePath);
    // private ParseReturnVals parseTXT(String filePath);

    // helper method for setting type
    // *UPDATE WITH ORHANS ART ENTITY LATER*
    private Type setType(String typeStr) {
        if (typeStr.contains("paper")) {
            if (typeStr.contains("title")) {
                return Type.PaperPrintFramedWithTitlePlate;
            }
            else { return Type.PaperPrintFramed; }
        }
        if (typeStr.contains("canvas")) {
            return Type.CanvasFloatFrame;
        }
        if (typeStr.contains("wall")) {
            return Type.WallDecor;
        }
        if (typeStr.contains("acoustic")) {
            if (typeStr.contains("framed")) {
                return Type.AcousticPanelFramed;
            }
            else { return Type.AcousticPanel; }
        }
        if (typeStr.contains("metal")) {
            return Type.MetalPrint;
        }
        if (typeStr.contains("mirror")) {
            return Type.Mirror;
        }
        return null;
    }
    // helper method for setting glaze
    // *UPDATE WITH ORHANS ART ENTITY LATER*
    private GlazingType setGlazingType(String glazingStr) {
        if (glazingStr.contains("glass")) {
            return GlazingType.Glass;
        } else if (glazingStr.contains("acrylic")) {
            return GlazingType.Acrylic;
        } else {
            return GlazingType.NoGlaze;
        }
    }
    // helper method for setting hardware number
    private int setHardware(String hardwareStr) {
        if (hardwareStr == null || hardwareStr.isEmpty()) {
            return -1;
        }

        // Split on whitespace to isolate the first word
        String firstWord = hardwareStr.split("\\s+")[0];
        // Keep only digits from the first word
        String digits = firstWord.replaceAll("\\D+", ""); // \D = non-digit
        if (digits.isEmpty()) {
            return -1;
        }

        return Integer.parseInt(digits);
    }
}
