package ArchDesign.responses;

import ArchDesign.interactors.Packing.oversizeObjects;

public class ShipmentSummary {
    private final int total_pieces;
    private final int standard_size_pieces;
    private final oversizeObjects[] oversized_pieces;
    private final int standard_box_count;
    private final int large_box_count;
    private final int custom_piece_count;
    private final int standard_pallet_count;
    private final int oversized_pallet_count;
    private final int crate_count;
    private final int total_artwork_weight;
    private final int total_packaging_weight;
    private final int final_shipment_weight;

    public ShipmentSummary(
        int total_pieces, int standard_size_pieces, oversizeObjects[] oversized_pieces, int standard_box_count, int large_box_count, 
        int custom_piece_count, int standard_pallet_count, int oversized_pallet_count, int crate_count, int total_artwork_weight, int total_packaging_weight, int final_shipment_weight) 
        {
            this.total_pieces = total_pieces;
            this.standard_size_pieces = standard_size_pieces;
            this.oversized_pieces = oversized_pieces;
            this.standard_box_count = standard_box_count;
            this.large_box_count = large_box_count;
            this.custom_piece_count = custom_piece_count;
            this.standard_pallet_count = standard_pallet_count;
            this.oversized_pallet_count = oversized_pallet_count;
            this.crate_count = crate_count;
            this.total_artwork_weight = total_artwork_weight;
            this.total_packaging_weight = total_packaging_weight;
            this.final_shipment_weight = final_shipment_weight;
    }
}
