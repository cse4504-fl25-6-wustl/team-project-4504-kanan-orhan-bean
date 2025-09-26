public class Client {
    private String location;
    private String name;
    private DeliveryCapabilities deliveryCapabilities;
    private ServiceType serviceType;

    // Enum for service type
    public static enum ServiceType {
        DELIVERY,
        INSTALLATION,
        DELIVERY_AND_INSTALLATION,
        NONE
    }

    // Non-static inner class for delivery-related booleans
    private class DeliveryCapabilities {
        private boolean acceptsPallets;
        private boolean acceptsCrates;
        private boolean loadingDockAccess;
        private boolean liftgateRequired;
        private boolean insideDeliveryNeeded;

        private DeliveryCapabilities(boolean acceptsPallets, boolean acceptsCrates,
                                    boolean loadingDockAccess, boolean liftgateRequired,
                                    boolean insideDeliveryNeeded) {
            this.acceptsPallets = acceptsPallets;
            this.acceptsCrates = acceptsCrates;
            this.loadingDockAccess = loadingDockAccess;
            this.liftgateRequired = liftgateRequired;
            this.insideDeliveryNeeded = insideDeliveryNeeded;
        }

        // Getters
        public boolean acceptsPallets() { return acceptsPallets; }
        public boolean acceptsCrates() { return acceptsCrates; }
        public boolean hasLoadingDockAccess() { return loadingDockAccess; }
        public boolean isLiftgateRequired() { return liftgateRequired; }
        public boolean isInsideDeliveryNeeded() { return insideDeliveryNeeded; }

        // Setters
        public void setAcceptsPallets(boolean acceptsPallets) { this.acceptsPallets = acceptsPallets; }
        public void setAcceptsCrates(boolean acceptsCrates) { this.acceptsCrates = acceptsCrates; }
        public void setLoadingDockAccess(boolean loadingDockAccess) { this.loadingDockAccess = loadingDockAccess; }
        public void setLiftgateRequired(boolean liftgateRequired) { this.liftgateRequired = liftgateRequired; }
        public void setInsideDeliveryNeeded(boolean insideDeliveryNeeded) { this.insideDeliveryNeeded = insideDeliveryNeeded; }
    }
    
    // client constructor
    public Client(String jobSiteLocation, String clientName,
                  boolean acceptsPallets, boolean acceptsCrates,
                  boolean loadingDockAccess, boolean liftgateRequired,
                  boolean insideDeliveryNeeded, ServiceType serviceType) {
        this.location = jobSiteLocation;
        this.name = clientName;
        this.deliveryCapabilities = new DeliveryCapabilities(acceptsPallets, acceptsCrates, loadingDockAccess, liftgateRequired, insideDeliveryNeeded);
        this.serviceType = serviceType;
    }

    // Getters
    public String getLocation() { return location; }
    public String getName() { return name; }
    public DeliveryCapabilities getDeliveryCapabilities() { return this.deliveryCapabilities; }
    public ServiceType getServiceType() { return serviceType; }

    // Setters
    public void setLocation(String location) { this.location = location; }
    // public String setName() { return name; }
    public void setServiceType(ServiceType serviceType) { this.serviceType = serviceType; }
}
