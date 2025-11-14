package ArchDesign.responses;

import java.awt.Container;

import ArchDesign.interactors.Packing.oversizeObjects;

public class ExtendedSerializer {

    private String summaryMessage = "";

    public ExtendedSerializer(Response response){

        // TODO: copy extended results from https://piazza.com/class/mf6woqytzb81zq/post/10

        concatToSummary("Work Order Summary: ");
        concatToSummary("- Total Pieces: " + response.getTotalPieces());
        concatToSummary("- Standard Size Pieces: " + response.getStandardSizePieces());
        concatToSummary("- Oversize Pieces: " + response.getOversizedPieces().length);
        for (oversizeObjects oversizeObject : response.getOversizedPieces()){
            concatToSummary("o " + oversizeObject.side1 + "\" x " + oversizeObject.side2 + " \" (Qty: " + 
            oversizeObject.quantity + ") = " + oversizeObject.weight + " lbs ea. " + 
            oversizeObject.weight*oversizeObject.quantity + " total lbs.");
        }
        concatToSummary("Total Artwork Weight: " + response.getTotalArtworkWeight());
        concatToSummary("Total Packaging Weight: " + response.getTotalPackagingWeight());
        concatToSummary("Total Shipment Weight: " + response.getFinalShipmentWeight());
        concatToSummary("");
        concatToSummary("");
        concatToSummary("---Extended Summary---");
        concatToSummary("");
        concatToSummary("1. Weight Summary");
        concatToSummary("Total Artwork Weight: " + response.getTotalArtworkWeight() + " lbs");
        concatToSummary("- Standard pieces: " + response.getStandardSizePiecesWeight() + " lbs");
        concatToSummary("- Oversize pieces: " + response.getOversizeSizePiecesWeight() + " lbs");
        concatToSummary("");
        concatToSummary("Total Packaging Weight: " + response.getTotalPackagingWeight() + " lbs");
        concatToSummary("- Standard Pallets: " + 60*response.getStandardPalletCount() + " lbs (" 
        + response.getStandardPalletCount() + " standard pallets @ 60 lbs each)");
        concatToSummary("- Oversize Pallets: " + 75*response.getOversizedPalletCount() + " lbs (" 
        + response.getOversizedPalletCount() + " oversize pallets @ 75 lbs each)");
        concatToSummary("- Crates: " + 125*response.getCrateCount() + "lbs (" 
        + response.getCrateCount() + " crates @ 125 lbs each)");
        concatToSummary("");
        concatToSummary("Final Shipment Weight: " + response.getTotalWeight());
        concatToSummary("");
        concatToSummary("");
        concatToSummary("2. Packing Summary");
        concatToSummary("Box Requirements:");
        concatToSummary("- Standard boxes (37\"x11\"x31\"): " + response.getStandardBoxCount() + " boxes");
        concatToSummary("- Large boxes (44\"x13\"x48\"): " + response.getLargeBoxCount() + " boxes");
        concatToSummary("- Total Boxes: " + response.getStandardBoxCount() + response.getLargeBoxCount());
        concatToSummary("");
        concatToSummary("Pallet/Crate Requirements:");
        concatToSummary("- Standard pallets: (48\"x40\"): " + response.getStandardPalletCount() + " pallets");
        concatToSummary("- Oversize pallets: (60\"x40\"): " + response.getOversizedPalletCount() + " pallets");
        concatToSummary("- Crates: (50\"x38\"): " + response.getCrateCount() + " crates");
        concatToSummary("");
        concatToSummary("Final Dimensions:");
        int i = 1;
        ArchDesign.entities.Container.Type prevType = ArchDesign.entities.Container.Type.Custom;
        for (ArchDesign.entities.Container container : response.getContainers()){
            if (prevType != container.getType()){
                i = 1;
            }
            prevType = container.getType();
            concatToSummary("- " + container.getType() + " " + i + ": " + container.getLength() + "\"x" + 
            container.getWidth() + "\"x" + container.getHeight() + "\"H @ " + container.getWeight() + " lbs");
            i++;
        }
        concatToSummary("");
        concatToSummary("3. Business Intelligence");
        concatToSummary("Oversized Items Flagged: ");
        for (oversizeObjects oversizeObject : response.getOversizedPieces()){
            concatToSummary("- " + oversizeObject.side1 + "\" x " + oversizeObject.side2 + " \" (Qty: " + 
            oversizeObject.quantity + ") = " + oversizeObject.weight + " lbs ea. " + 
            oversizeObject.weight*oversizeObject.quantity + " total lbs.");
        }
        concatToSummary("");
        for (ArchDesign.entities.Art customArt : response.getCustomArts()){
            concatToSummary("- Line Number: " + customArt.getLineNumber() + ". Dimensions: " + customArt.getHeight() + 
            "\" x " + customArt.getWidth() + ". Weight: " + customArt.getWeight() + " lbs");
        }
    }

    public String getSummary() {
        return this.summaryMessage;
    }

    private void concatToSummary(String string){
        this.summaryMessage += string + "\n";
    }
    
}
