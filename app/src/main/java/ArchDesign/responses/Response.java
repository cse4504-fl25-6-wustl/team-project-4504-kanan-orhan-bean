package ArchDesign.responses;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collections;

import ArchDesign.entities.Art;
import ArchDesign.entities.Box;
import ArchDesign.entities.Container;

public class Response {

    private final List<Art> arts;
    private final List<Box> boxes;
    private final List<Container> containers;
    private final double totalWeight;
    private final String summary;

    /**
     * @param arts        items to be shipped (non-null; may be empty)
     * @param boxes       boxes produced by packing (non-null; may be empty)
     * @param containers  containers produced by packing (non-null; may be empty)
     * @param totalWeight precomputed total shipment weight (lbs)
     * @param summary     precomputed human-readable summary (nullable/optional)
     */
    public Response(List<Art> arts, List<Box> boxes, List<Container> containers,
            double totalWeight, String summary) {
        this.arts = Collections
                .unmodifiableList(new ArrayList<>(Objects.requireNonNull(arts, "arts must not be null")));
        this.boxes = Collections
                .unmodifiableList(new ArrayList<>(Objects.requireNonNull(boxes, "boxes must not be null")));
        this.containers = Collections
                .unmodifiableList(new ArrayList<>(Objects.requireNonNull(containers, "containers must not be null")));
        this.totalWeight = totalWeight;
        this.summary = summary;
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

    public double getTotalWeight() {
        return this.totalWeight;
    }

    public String getSummary() {
        return this.summary;
    }

    @Override
    public String toString() {
        return this.summary != null ? this.summary : "";
    }
}
