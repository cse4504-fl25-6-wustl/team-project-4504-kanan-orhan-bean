/**
 * Parser.java
 *
 * Abstract base class for all file parsers in the project.
 * Defines the contract (parse method) and shared return values.
 * Concrete subclasses will implement the actual parsing logic for their respective file types.
 */

import ProjectData.Project;

public abstract class Parser {

    // Possible outcomes of parsing
    public static enum ParseReturnVals {
        SUCCESS,
        FILE_NOT_FOUND,
        PARSE_ERROR,
        INVALID_FORMAT
    }

    // Abstract parse method to be implemented by subclasses
    public abstract ParseReturnVals parse(String filePath, Project project);
}
