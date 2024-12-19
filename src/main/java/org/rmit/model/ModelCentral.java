package org.rmit.model;

import org.rmit.view.Renter.RenterViewFactory;
import org.rmit.view.StartView.ViewFactory;

public class ModelCentral {
    private static ModelCentral modelCentral;

    private ViewFactory viewFactory;
    private RenterViewFactory renterViewFactory;


    private ModelCentral() {
        viewFactory = new ViewFactory();
        renterViewFactory = new RenterViewFactory();
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
}
