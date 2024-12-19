package org.rmit.controller.Renter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.rmit.model.ModelCentral;
import org.rmit.view.Renter.RENTER_MENU_OPTION;

import java.net.URL;
import java.util.ResourceBundle;

public class RenterController implements Initializable{

    @FXML
    BorderPane borderPane;

    public RenterController() {
        // khi nguoi dung bam nut --> selectedMenuItem thay doi --> thay doi view
        ModelCentral.getInstance().getRenterViewFactory().renterSelectedMenuItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case DASHBOARD -> borderPane.setCenter(ModelCentral.getInstance().getRenterViewFactory().getRenter_dashboardView());
                case PAYMENT_MANAGER -> borderPane.setCenter(ModelCentral.getInstance().getRenterViewFactory().getRenter_paymentManagerView());
                case AGREEMENT_MANAGER -> borderPane.setCenter(ModelCentral.getInstance().getRenterViewFactory().getRenter_agreementManagerView());
                case EDIT_PROFILE -> borderPane.setCenter(ModelCentral.getInstance().getRenterViewFactory().getRenter_EditProfileView());
                case SEARCH -> System.out.println("Search");
            }
        });
        //set default view
        ModelCentral.getInstance().getRenterViewFactory().setRenterSelectedMenuItem(RENTER_MENU_OPTION.DASHBOARD);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // nothing here
    }
}
