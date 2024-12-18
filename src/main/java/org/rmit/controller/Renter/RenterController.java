package org.rmit.controller.Renter;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import org.rmit.model.ModelCentral;

public class RenterController {

    @FXML
    BorderPane borderPane;

    public RenterController() {
        ModelCentral.getInstance().getViewFactory().renterSelectedMenuItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case DASHBOARD -> borderPane.setCenter(ModelCentral.getInstance().getViewFactory().getDashboardView());
                case PAYMENT_MANAGER -> System.out.println("Payment Manager");
                case AGREEMENT_MANAGER -> System.out.println("Agreement Manager");
                case LOGOUT -> System.out.println("Goodbye");
                case EDIT_PROFILE -> borderPane.setCenter(ModelCentral.getInstance().getViewFactory().getEditProfile());
                case SEARCH -> System.out.println("Search");
            }
        });
    }

}
