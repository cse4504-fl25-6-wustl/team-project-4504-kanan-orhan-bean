package ArchDesign.responses;

import ArchDesign.interactors.Packing.oversizeObjects;

public class CommandLineSerializer {

    private String summaryMessage = "";

    public CommandLineSerializer(Response response){

        this.summaryMessage += "Work Order Summary: \n";
        this.summaryMessage += "- Total Pieces: " + response.getTotalPieces() + "\n";
        this.summaryMessage += "- Standard Size Pieces: " + response.getStandardSizePieces() + "\n";
        this.summaryMessage += "- Oversize Pieces: " + response.getOversizedPieces().length + "\n";
        for (oversizeObjects oversizeObject : response.getOversizedPieces()){
            this.summaryMessage += "o " + oversizeObject.side1 + "\" x " + oversizeObject.side2 + " \" (Qty: " + oversizeObject.quantity + ") = " + oversizeObject.weight + " lbs \n";
        }
        this.summaryMessage += "Total Artwork Weight: " + response.getTotalArtworkWeight() + "\n";
        this.summaryMessage += "Total Packaging Weight: " + response.getTotalPackagingWeight() + "\n";
        this.summaryMessage += "Total Shipment Weight: " + response.getFinalShipmentWeight() + "\n";
    }

    public String getSummary() {
        return this.summaryMessage;
    }
    
}
