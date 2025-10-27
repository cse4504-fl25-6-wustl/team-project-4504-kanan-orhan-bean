package ArchDesign.responses;

import ArchDesign.interactors.Packing.oversizeObjects;

public class CommandLineSerializer {

    private final String summaryMessage = "";

    public CommandLineSerializer(Response response){
        // TODO: It correctly creates a summary Message

        this.summaryMessage += "Work Order Summary: \n";
        this.summaryMessage += "- Total Pieces: " + response.getTotalPieces() + "\n";
        this.summaryMessage += "- Standard Size Pieces: " + response.getStandardSizePieces() + "\n";
        this.summaryMessage += "- Oversize Pieces: " + response.getOversizePieces().size() + "\n";
        for (oversizeObjects oversizeObject : response.getOversizePieces()){
            this.summaryMessage += "o " + oversizeObject.side1 + "\" x " + oversizeObject.side2 + " \" (Qty: " + oversizeObject.quantity + ") = " + oversizeObject.weight + " lbs \n";
        }
        this.summaryMessage += "Total Artwork Weight: " + response.getTotalArtworkWeight() + "\n";
        this.summaryMessage += "Total Packaging Weight: " + response.getTotalPackagingWeight() + "\n";
        this.summaryMessage += "Total Shipment Weight: " + response.getTotalShipmentWeight() + "\n";
    }

    public String getSummary() {
        return this.summaryMessage;
    }
    
}
