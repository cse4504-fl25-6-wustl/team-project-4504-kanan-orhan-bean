/**
 * FileParser.java
 * Author: Bean Royal
 *
 * Abstract base class for all file parsers in the project.
 * Defines the contract (parse method) and shared return values.
 * Concrete subclasses will implement the actual parsing logic for their respective file types.
 */

package ArchDesign.parser;

public abstract class FileParser {

    // Possible outcomes of parsing
    public static enum ParseReturnVals {
        SUCCESS,
        FILE_NOT_FOUND,
        PARSE_ERROR,
        INVALID_FORMAT,
        INVALID_FILE_TYPE
    }

    // Abstract parse method to be implemented by subclasses
    protected abstract ParseReturnVals parse(String filePath);
}
