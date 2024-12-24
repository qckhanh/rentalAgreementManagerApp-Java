package org.rmit.controller.Host;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.rmit.model.ModelCentral;
import org.rmit.model.Session;
import org.rmit.view.Host.HOST_MENU_OPTION;
import org.rmit.view.Renter.RENTER_MENU_OPTION;

import java.net.URL;
import java.util.ResourceBundle;

public class Host_MenuController implements Initializable {
    public Label name_label;
    public Label userType_label;
    public Button editProfile_btn;
    public Button dashboard_btn;
    public Button logOut_btn;
    public Button propertyManager_btn;
//    public Button rentalManager_btn;
//    public Button makePayment_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userType_label.setText(Session.getInstance().getCurrentUser().getClass().getSimpleName());

        name_label.setText(Session.getInstance().getCurrentUser().getName());
        Session.getInstance().getCurrentUser().namePropertyProperty().addListener((observable, oldValue, newValue) ->
                name_label.setText(newValue)
        );

        editProfile_btn.setOnAction(e -> editProfile());
        dashboard_btn.setOnAction(e -> openDashboard());
        logOut_btn.setOnAction(e -> logOut());
        propertyManager_btn.setOnAction(e -> manageProperty());
    }



    private void logOut() {
        Session.getInstance().setCurrentUser(null);
        Stage stage = (Stage) logOut_btn.getScene().getWindow();
        ModelCentral.getInstance().getHostViewFactory().resetView();
        ModelCentral.getInstance().getStartViewFactory().closeStage(stage);
        ModelCentral.getInstance().getStartViewFactory().startApplication();
    }

    private void openDashboard() {
        ModelCentral.getInstance().getHostViewFactory().setSelectedMenuItem(HOST_MENU_OPTION.DASHBOARD);
    }

    private void editProfile() {
        ModelCentral.getInstance().getHostViewFactory().setSelectedMenuItem(HOST_MENU_OPTION.EDIT_PROFILE);
    }

    private void manageProperty() {
        ModelCentral.getInstance().getHostViewFactory().setSelectedMenuItem(HOST_MENU_OPTION.MANAGE_PROPERTY);
    }
}
