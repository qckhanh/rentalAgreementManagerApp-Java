package org.rmit.controller.Admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.database.*;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Admin;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.Property;
import org.rmit.model.Property.ResidentialProperty;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import static org.rmit.view.Admin.ADMIN_MENU_OPTION.DASHBOARD;
import static org.rmit.view.Admin.ADMIN_MENU_OPTION.RENTER_MANAGER;

public class AdminController implements Initializable {
    @FXML
    BorderPane borderPane;
    public AdminController() {
        ModelCentral.getInstance().getAdminViewFactory().selectedMenuItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case RENTER_MANAGER -> borderPane.setCenter(ModelCentral.getInstance().getAdminViewFactory().getRenterManagerView());
                case DASHBOARD -> borderPane.setCenter(ModelCentral.getInstance().getAdminViewFactory().getAdmin_dashboardView());
                case EDIT_PROFILE -> borderPane.setCenter(ModelCentral.getInstance().getAdminViewFactory().getAdmin_editProfileView());
                case AGREEMENT_MANAGER -> borderPane.setCenter(ModelCentral.getInstance().getAdminViewFactory().getAdmin_agreementManagerView());
                case HOST_MANAGER -> borderPane.setCenter(ModelCentral.getInstance().getAdminViewFactory().getAdmin_HostManagerView());
                case OWNER_MANAGER -> borderPane.setCenter(ModelCentral.getInstance().getAdminViewFactory().getAdmin_OwnerManagerView());
                case PAYMENT_MANAGER -> borderPane.setCenter(ModelCentral.getInstance().getAdminViewFactory().getAdmin_PaymentManagerView());
                case ADMIN_MANAGER -> borderPane.setCenter(ModelCentral.getInstance().getAdminViewFactory().getAdmin_AdminManagerView());
                case PROPERTY_MANAGER -> borderPane.setCenter(ModelCentral.getInstance().getAdminViewFactory().getPropertyManagerView());
            }
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}
