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


    private ModelCentral() {
        startViewFactory = new StartViewFactory();
        renterViewFactory = new RenterViewFactory();
        ownerViewFactory = new OwnerViewFactory();
        hostViewFactory = new HostViewFactory();
        //declare more view factories here
    }

    public static ModelCentral getInstance() {
        if (modelCentral == null) {
            modelCentral = new ModelCentral();
        }
        return modelCentral;
    }

    public StartViewFactory getStartViewFactory() {
        return startViewFactory;
    }

    public RenterViewFactory getRenterViewFactory() {
        return renterViewFactory;
    }

    public OwnerViewFactory getOwnerViewFactory() {
        return ownerViewFactory;
    }

    public HostViewFactory getHostViewFactory() {
        return hostViewFactory;
    }
}
