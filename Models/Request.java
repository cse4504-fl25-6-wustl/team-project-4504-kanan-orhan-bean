package Models;

/**
 * Request
 *
 * Input model for the system (passed from CLI).
 * Example CLI call:
 *   java Main items.xlsx "Sunrise Senior Living" customer_rules.xlsx
 *
 * args[0] = line item file name (String)
 * args[1] = client name (String)
 * args[2] = customer specifics file name (String)
 */
public class Request {

    // === Variables ===
    private String lineItemFileName;          // e.g., "Order123_LineItems.xlsx"
    private String clientName;                // e.g., "Sunrise Senior Living"
    private String customerSpecificsFileName; // e.g., "CustomerRules_Sunrise.xlsx"

    // === Constructor ===
    public Request(String lineItemFileName, String clientName, String customerSpecificsFileName) {
        this.lineItemFileName = lineItemFileName;
        this.clientName = clientName;
        this.customerSpecificsFileName = customerSpecificsFileName;
    }

    // === Factory Method (from CLI arguments) ===
    /**
     * Builds a Request object from command-line arguments.
     * @param args String[] passed from main()
     * @return Request object
     * @throws IllegalArgumentException if args are missing or invalid
     */
    public static Request fromCommandLineArgs(String[] args) {
        if (args.length < 3) {
            throw new IllegalArgumentException(
                "Usage: java Main <LineItemFileName> <ClientName> <CustomerSpecificsFileName>"
            );
        }

        String fileName = args[0];
        String client = args[1];
        String customerSpecifics = args[2];

        // TODO: Add optional validation (file existence, name checks, etc.)
        return new Request(fileName, client, customerSpecifics);
    }

    // === Getters ===
    public String getLineItemFileName() {
        return lineItemFileName;
    }

    public String getClientName() {
        return clientName;
    }

    public String getCustomerSpecificsFileName() {
        return customerSpecificsFileName;
    }

    // === Debug/Utility ===
    @Override
    public String toString() {
        return "Request{" +
                "lineItemFileName='" + lineItemFileName + '\'' +
                ", clientName='" + clientName + '\'' +
                ", customerSpecificsFileName='" + customerSpecificsFileName + '\'' +
                '}';
    }
}
