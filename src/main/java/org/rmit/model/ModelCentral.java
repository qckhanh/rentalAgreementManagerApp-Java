package org.rmit.model;

import org.rmit.view.Host.HostViewFactory;
import org.rmit.view.Owner.OwnerViewFactory;
import org.rmit.view.Renter.RenterViewFactory;
import org.rmit.view.Start.StartViewFactory;

public class ModelCentral {
    private static ModelCentral modelCentral;

    private StartViewFactory startViewFactory;
    private RenterViewFactory renterViewFactory;
    private OwnerViewFactory ownerViewFactory;
    private HostViewFactory hostViewFactory;


    private ModelCentral() {}

    public static ModelCentral getInstance() {
        if (modelCentral == null) {
            modelCentral = new ModelCentral();
        }
        return modelCentral;
    }

    public StartViewFactory getStartViewFactory() {
        if (startViewFactory == null) {
            startViewFactory = new StartViewFactory();
        }
        return startViewFactory;
    }

    public RenterViewFactory getRenterViewFactory() {
        if(renterViewFactory == null){
            renterViewFactory = new RenterViewFactory();
        }
        return renterViewFactory;
    }

    public OwnerViewFactory getOwnerViewFactory() {
        if (ownerViewFactory == null) {
            ownerViewFactory = new OwnerViewFactory();
        }
        return ownerViewFactory;
    }

    public HostViewFactory getHostViewFactory() {
        if (hostViewFactory == null) {
            hostViewFactory = new HostViewFactory();
        }
        return hostViewFactory;
    }
}
