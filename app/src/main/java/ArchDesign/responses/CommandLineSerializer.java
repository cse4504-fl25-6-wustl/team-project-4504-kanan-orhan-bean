package ArchDesign.responses;

public class CommandLineSerializer {

    private final String summaryMessage;

    public CommandLineSerializer(Response response){
        // TODO: It correctly creates a summary Message

        this.summaryMessage = "";
    }

    public String getSummary() {
        return this.summaryMessage;
    }
    
}
