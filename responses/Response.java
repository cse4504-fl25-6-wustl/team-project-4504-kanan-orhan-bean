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
        //TODO
    }

    private double calculateTotalWeight(){
        return -1;
    };

}
