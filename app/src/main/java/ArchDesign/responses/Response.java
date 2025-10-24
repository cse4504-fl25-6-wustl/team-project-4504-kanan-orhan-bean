package ArchDesign.responses;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collections;

import ArchDesign.entities.Art;
import ArchDesign.entities.Box;
import ArchDesign.entities.Container;
import ArchDesign.interactors.Packing.oversizeObjects;;

public class Response {

    private final List<Art> arts;
    private final List<Box> boxes;
    private final List<Container> containers;

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

    /**
     * @param arts        items to be shipped (non-null; may be empty)
     * @param boxes       boxes produced by packing (non-null; may be empty)
     * @param containers  containers produced by packing (non-null; may be empty)
     * @param totalWeight precomputed total shipment weight (lbs)
     * @param summary     precomputed human-readable summary (nullable/optional)
     */
    public Response(List<Art> arts, List<Box> boxes, List<Container> containers, int total_pieces, int standard_size_pieces, 
    oversizeObjects[] oversized_pieces, int standard_box_count, int large_box_count, int custom_piece_count, int standard_pallet_count,
    int oversized_pallet_count, int crate_count, int total_artwork_weight, int total_packaging_weight, int final_shipment_weight) {
        this.arts = Collections
                .unmodifiableList(new ArrayList<>(Objects.requireNonNull(arts, "arts must not be null")));
        this.boxes = Collections
                .unmodifiableList(new ArrayList<>(Objects.requireNonNull(boxes, "boxes must not be null")));
        this.containers = Collections
                .unmodifiableList(new ArrayList<>(Objects.requireNonNull(containers, "containers must not be null")));
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

    public List<Art> getArts() {
        return this.arts;
    }

    public List<Box> getBoxes() {
        return this.boxes;
    }

    public List<Container> getContainers() {
        return this.containers;
    }

    public int getTotalWeight() {
        return this.final_shipment_weight;
    }
}
