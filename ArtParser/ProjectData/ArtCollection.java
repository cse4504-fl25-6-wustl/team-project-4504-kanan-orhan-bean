/**
 * ArtCollection.java
 *
 * Collection of artPiece objects in a HashMap, where the pieces unique line item number is the object key, and the artPiece is the value
 */

package ProjectData;

import java.util.HashMap;
import java.util.Map;

// similar to client, should we make instantiation protected?
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

    // find and return a specific art piece by key (line number)
    public ArtPiece getArtPieceByLineNumber(int lineNumber) {
        return this.artCollection.get(lineNumber);
    }

    /**
     * Removes an ArtPiece from the collection by its line number key.
     * @return the removed ArtPiece if it existed, or null if not found
     */
    public ArtPiece removeArtPiece(int lineNumber) {
        return this.artCollection.remove(lineNumber);
    }

}
