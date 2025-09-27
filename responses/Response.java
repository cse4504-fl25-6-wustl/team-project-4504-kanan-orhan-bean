package responses;

import java.util.List;

import entities.Art;
import entities.Box;
import entities.Container;

public class Response {
    
    private List<Art> arts;
    private List<Box> boxes;
    private List<Container> containers;
    private double totalWeight;

    public Response(List<Art> arts, List<Box> boxes, List<Container> containers) {
        super();
        //a. Weight of each individual item
        // b. How items are packed in boxes (which items are packed together
        // c. The weight of each box
        // d. How boxes are packed in crates or pallets (which boxes are packed together and are they on a pallet or a crate)
        // e. The weight and height of each pallet/crate.
        // f. Total weight of the shipment
        //TODO
    }

    private double calculateTotalWeight(){
        return -1;
    };

}
