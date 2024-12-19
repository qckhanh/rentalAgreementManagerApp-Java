package org.rmit.model;

import org.rmit.view.Owner.OwnerViewFactory;
import org.rmit.view.Renter.RenterViewFactory;
import org.rmit.view.Start.ViewFactory;

public class ModelCentral {
    private static ModelCentral modelCentral;

    private ViewFactory viewFactory;
    private RenterViewFactory renterViewFactory;
    private OwnerViewFactory ownerViewFactory;


    private ModelCentral() {
        viewFactory = new ViewFactory();
        renterViewFactory = new RenterViewFactory();
        ownerViewFactory = new OwnerViewFactory();
        //declare more view factories here
    }

    public static ModelCentral getInstance() {
        if (modelCentral == null) {
            modelCentral = new ModelCentral();
        }
        return modelCentral;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public RenterViewFactory getRenterViewFactory() {
        return renterViewFactory;
    }

    public OwnerViewFactory getOwnerViewFactory() {
        return ownerViewFactory;
    }
}
