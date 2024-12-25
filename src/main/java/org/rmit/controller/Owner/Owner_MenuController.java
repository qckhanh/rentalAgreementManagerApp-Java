package org.rmit.controller.Owner;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.rmit.model.ModelCentral;
import org.rmit.model.Session;
import org.rmit.view.Owner.OWNER_MENU_OPTION;

import java.net.URL;
import java.util.ResourceBundle;

public class Owner_MenuController implements Initializable {
    public Label name_label;
    public Label userType_label;
    public Button editProfile_btn;
    public Button dashboard_btn;
    public Button logOut_btn;
    public Button propertiesManager_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userType_label.setText(Session.getInstance().getCurrentUser().getClass().getSimpleName());

        name_label.setText(Session.getInstance().getCurrentUser().getName());

        editProfile_btn.setOnAction(e->editProfile());
        dashboard_btn.setOnAction(e->openDashboard());
        propertiesManager_btn.setOnAction(e->propertiesManager());
        logOut_btn.setOnAction(e->logOut());
    }

    private void logOut() {
        Session.getInstance().setCurrentUser(null);
        Stage stage = (Stage) logOut_btn.getScene().getWindow();
        ModelCentral.getInstance().getOwnerViewFactory().resetView();
        ModelCentral.getInstance().getStartViewFactory().closeStage(stage);
        ModelCentral.getInstance().getStartViewFactory().startApplication();
    }

    private void openDashboard() {
        ModelCentral.getInstance().getOwnerViewFactory().setOwnerSelectedMenuItem(OWNER_MENU_OPTION.DASHBOARD);
        System.out.println("hallo!");
    }

    private void propertiesManager() {
        System.out.println("hallo! ich bin lam");
        ModelCentral.getInstance().getOwnerViewFactory().setOwnerSelectedMenuItem(OWNER_MENU_OPTION.PROPERTIES_MANAGER);
    }

    private void editProfile() {
        ModelCentral.getInstance().getOwnerViewFactory().setOwnerSelectedMenuItem(OWNER_MENU_OPTION.EDIT_PROFILE);
    }


}
