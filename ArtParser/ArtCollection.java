import java.util.HashMap;
import java.util.Map;

public class ArtCollection {
    private Map<Integer, ArtPiece> artCollection = new HashMap<>();

    // no-arg constructor
    public ArtCollection() {
        this.artCollection = new HashMap<>();
    }

    // constructor with existing map
    public ArtCollection(Map<Integer, ArtPiece> artPieces) {
        this.artCollection = artPieces;
    }
    
    // return map of art pieces
    public Map<Integer, ArtPiece> getArtCollection() {
        return this.artCollection;
    }
    
    // add a singular art piece to the map
    public void addArtPiece(ArtPiece piece) {
        this.artCollection.put(piece.getLineNumber(), piece);
    }
}
