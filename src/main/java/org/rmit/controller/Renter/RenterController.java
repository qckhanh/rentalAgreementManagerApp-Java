package org.rmit.controller.Renter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.rmit.model.ModelCentral;

import java.net.URL;
import java.util.ResourceBundle;

public class RenterController implements Initializable{

    @FXML
    BorderPane borderPane;

    public RenterController() {
        ModelCentral.getInstance().getViewFactory().renterSelectedMenuItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case DASHBOARD -> borderPane.setCenter(ModelCentral.getInstance().getViewFactory().getRenter_dashboardView());
                case PAYMENT_MANAGER -> borderPane.setCenter(ModelCentral.getInstance().getViewFactory().getRenter_paymentManagerView());
                case AGREEMENT_MANAGER -> borderPane.setCenter(ModelCentral.getInstance().getViewFactory().getRenter_agreementManagerView());
                case EDIT_PROFILE -> borderPane.setCenter(ModelCentral.getInstance().getViewFactory().getRenter_EditProfileView());
                case SEARCH -> System.out.println("Search");
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
