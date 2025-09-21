package Models;

/**
 * Request
 *
 * Input model for the system (passed from CLI).
 * Example CLI call:
 *   java Main items.xlsx "Sunrise Senior Living"
 *
 * args[0] = file name (String)
 * args[1] = client name (String)
 */
public class Request {

    // === Variables ===
    private String lineItemFileName;   // e.g., "Order123_LineItems.xlsx"
    private String clientName;         // e.g., "Sunrise Senior Living"

    // === Constructor ===
    public Request(String lineItemFileName, String clientName) {
        this.lineItemFileName = lineItemFileName;
        this.clientName = clientName;
    }

    // === Factory Method (from CLI arguments) ===
    /**
     * Builds a Request object from command-line arguments.
     * @param args String[] passed from main()
     * @return Request object
     * @throws IllegalArgumentException if args are missing or invalid
     */
    public static Request fromCommandLineArgs(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException(
                "Usage: java Main <LineItemFileName> <ClientName>"
            );
        }

        String fileName = args[0];
        String client = args[1];

        // TODO: Add optional validation (file existence, name checks, etc.)
        return new Request(fileName, client);
    }

    // === Getters ===
    public String getLineItemFileName() {
        return lineItemFileName;
    }

    public String getClientName() {
        return clientName;
    }

    // === Debug/Utility ===
    @Override
    public String toString() {
        return "Request{" +
                "lineItemFileName='" + lineItemFileName + '\'' +
                ", clientName='" + clientName + '\'' +
                '}';
    }
}

