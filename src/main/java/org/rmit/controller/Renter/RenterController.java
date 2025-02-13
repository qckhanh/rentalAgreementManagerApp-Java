package org.rmit.controller.Renter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.rmit.view.ViewCentral;
import org.rmit.view.Renter.RENTER_MENU_OPTION;

import java.net.URL;
import java.util.ResourceBundle;

public class RenterController implements Initializable{

    @FXML
    BorderPane borderPane;

    public RenterController() {
        ViewCentral.getInstance().getRenterViewFactory().setSelectedMenuItem(RENTER_MENU_OPTION.DASHBOARD); // set the intial view
        ViewCentral.getInstance().getRenterViewFactory().selectedMenuItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case DASHBOARD -> borderPane.setCenter(ViewCentral.getInstance().getRenterViewFactory().getRenter_dashboardView());
                case PAYMENT_MANAGER -> borderPane.setCenter(ViewCentral.getInstance().getRenterViewFactory().getRenter_paymentManagerView());
                case AGREEMENT_MANAGER -> borderPane.setCenter(ViewCentral.getInstance().getRenterViewFactory().getRenter_agreementManagerView());
                case EDIT_PROFILE -> borderPane.setCenter(ViewCentral.getInstance().getRenterViewFactory().getRenter_EditProfileView());
                case MAKE_PAYMENT -> borderPane.setCenter(ViewCentral.getInstance().getRenterViewFactory().getRenter_makePaymentView());
                case MAKE_RENTAL_AGREEMENT -> borderPane.setCenter(ViewCentral.getInstance().getRenterViewFactory().getRenter_makeRentalAgreementView());
                case NOTIFICATION -> borderPane.setCenter(ViewCentral.getInstance().getRenterViewFactory().getRenter_NotificationView());
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // nothing here
    }
}
