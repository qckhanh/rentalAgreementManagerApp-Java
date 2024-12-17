package org.rmit.model;

import org.rmit.view.ViewFactory;

public class ModelCentral {
    private static ModelCentral modelCentral;
    private static ViewFactory viewFactory;


    private ModelCentral() {
        viewFactory = new ViewFactory();
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
}
