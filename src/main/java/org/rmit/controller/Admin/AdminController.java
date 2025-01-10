package org.rmit.controller.Admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.rmit.view.ViewCentral;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    @FXML
    BorderPane borderPane;
    public AdminController() {
        ViewCentral.getInstance().getAdminViewFactory().selectedMenuItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case RENTER_MANAGER -> borderPane.setCenter(ViewCentral.getInstance().getAdminViewFactory().getRenterManagerView());
                case DASHBOARD -> borderPane.setCenter(ViewCentral.getInstance().getAdminViewFactory().getAdmin_dashboardView());
                case EDIT_PROFILE -> borderPane.setCenter(ViewCentral.getInstance().getAdminViewFactory().getAdmin_editProfileView());
                case AGREEMENT_MANAGER -> borderPane.setCenter(ViewCentral.getInstance().getAdminViewFactory().getAdmin_agreementManagerView());
                case HOST_MANAGER -> borderPane.setCenter(ViewCentral.getInstance().getAdminViewFactory().getAdmin_HostManagerView());
                case OWNER_MANAGER -> borderPane.setCenter(ViewCentral.getInstance().getAdminViewFactory().getAdmin_OwnerManagerView());
                case PAYMENT_MANAGER -> borderPane.setCenter(ViewCentral.getInstance().getAdminViewFactory().getAdmin_PaymentManagerView());
                case ADMIN_MANAGER -> borderPane.setCenter(ViewCentral.getInstance().getAdminViewFactory().getAdmin_AdminManagerView());
                case PROPERTY_MANAGER -> borderPane.setCenter(ViewCentral.getInstance().getAdminViewFactory().getPropertyManagerView());
            }
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}
