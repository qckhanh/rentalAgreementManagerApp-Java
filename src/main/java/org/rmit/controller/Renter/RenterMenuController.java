package org.rmit.controller.Renter;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Person;
import org.rmit.model.Session;
import org.rmit.view.Renter.RENTER_MENU_OPTION;

import java.net.URL;
import java.util.ResourceBundle;

public class RenterMenuController implements Initializable {

    public Label userType_label;
    public Label name_label;

    public Button editProfile_btn;
    public Button dashboard_btn;
    public Button logOut_btn;
    public Button paymentManager_btn;
    public Button rentalManager_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userType_label.setText(Session.getInstance().getCurrentUser().getClass().getSimpleName());
        name_label.setText(Session.getInstance().getCurrentUser().getName());

        editProfile_btn.setOnAction(e -> editProfile());
        dashboard_btn.setOnAction(e -> openDashboard());
        logOut_btn.setOnAction(e -> logOut());
        paymentManager_btn.setOnAction(e -> paymentManager());
        rentalManager_btn.setOnAction(e -> rentalManager());

    }

    private void logOut(){
        Stage stage = (Stage) logOut_btn.getScene().getWindow();
        ModelCentral.getInstance().getViewFactory().closeStage(stage);
        ModelCentral.getInstance().getViewFactory().startInit();
    }

    private void openDashboard(){
        ModelCentral.getInstance().getViewFactory().setRenterSelectedMenuItem(RENTER_MENU_OPTION.DASHBOARD);
    }

    private void editProfile(){
        ModelCentral.getInstance().getViewFactory().setRenterSelectedMenuItem(RENTER_MENU_OPTION.EDIT_PROFILE);
    }

    private void paymentManager(){
        ModelCentral.getInstance().getViewFactory().setRenterSelectedMenuItem(RENTER_MENU_OPTION.PAYMENT_MANAGER);
    }

    private void rentalManager(){
        ModelCentral.getInstance().getViewFactory().setRenterSelectedMenuItem(RENTER_MENU_OPTION.AGREEMENT_MANAGER);
    }
}
