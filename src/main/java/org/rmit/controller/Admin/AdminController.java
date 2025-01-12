package org.rmit.controller.Admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.rmit.view.ViewCentral;

import java.net.URL;
import java.util.ResourceBundle;
/**
 *  * @author 4Knights
 * The AdminController class serves as the main controller for managing the Admin view in the application.
 * It dynamically updates the content of the main border pane based on the selected menu item
 * in the Admin view.
 *
 * <Key responsibilities of this class:
 * - Observes changes to the selected menu item from the ViewCentral's AdminViewFactory.
 * - Updates the content displayed in the `BorderPane` based on the selected menu item.
 * - Ensures that the correct view is loaded dynamically for each menu option.
 *
 * <This class implements the `Initializable` interface for JavaFX initialization.
 *
 * <Key Components:
 * - `BorderPane`: The layout container for the Admin view.
 * - `ViewCentral`: Manages the views and provides the required views based on the selected menu item.
 * - Menu options include:
 *   - Dashboard
 *   - Edit Profile
 *   - Renter Manager
 *   - Agreement Manager
 *   - Host Manager
 *   - Owner Manager
 *   - Payment Manager
 *   - Admin Manager
 *   - Property Manager
 *
 * <p>Dependencies:
 * - JavaFX `BorderPane` for layout management.
 * - `ViewCentral` and `AdminViewFactory` for managing and retrieving the respective views.
 */
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
