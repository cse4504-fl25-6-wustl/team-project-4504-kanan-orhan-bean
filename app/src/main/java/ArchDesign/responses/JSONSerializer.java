package ArchDesign.responses;

import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

public class JSONSerializer {
    
    // takes the response object and output file path, serializes response and write to file
    public static void ShipmentToJSONSummary(Response response, String outputFilePath) {
        Gson gson = new Gson();
        ShipmentSummary shipmentSummary = response.getShipmentSummary();
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            gson.toJson(shipmentSummary, writer);
            System.out.println("Response successfully written to " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error writing response to file: " + e.getMessage());
        }
    }
}


// ----- OUTPUT STRUCTURE ------
// {
//   "total_pieces": <value>,
//   "standard_size_pieces": <value>,
//   "oversized_pieces": [
//     {
//       "side1": <value>,
//       "side2": <value>,
//       "quantity": <value>
//     }
//   ],
//   "standard_box_count": <value>,
//   "large_box_count": <value>,
//   "custom_piece_count": <value>,
//   "standard_pallet_count": <value>,
//   "oversized_pallet_count": <value>,
//   "crate_count": <value>
//   "total_artwork_weight": <value>,
//   "total_packaging_weight": <value>,
//   "final_shipment_weight": <value>
// }
