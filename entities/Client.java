package entities;

import java.util.List;

public class Client {
    
    private enum serviceType { Delivery, Installation , DeliveryAndInstallation}
    private String jobSite;
    private String clientName;
    private boolean acceptsPallets;
    private boolean acceptsCrates;
    private boolean loadingDockAccess;
    private boolean liftGateNeeded;
    private boolean insideDeliveryNeeded;
    private serviceType serviceType;

    public Client(String jobSite, String clientName, boolean acceptsPallets, boolean acceptsCrates, 
    boolean loadingDockAccess, boolean liftGateNeeded, boolean insideDeliveryNeeded, serviceType serviceType) {
        super();
        // UNfinished
    }

    //Getter Setters


}
