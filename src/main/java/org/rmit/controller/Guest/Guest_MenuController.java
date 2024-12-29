package org.rmit.controller.Guest;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.rmit.Helper.UIDecorator;
import org.rmit.model.ModelCentral;
import org.rmit.view.Guest.GUEST_MENU_OPTION;

import java.net.URL;
import java.util.ResourceBundle;

import static org.rmit.Helper.UIDecorator.REGISTER;

public class Guest_MenuController implements Initializable {

    public Button signUp_btn;
    public Button rentalManager_btn;
    public Button browseUser_btn;
    public Button logOut_btn;
    public Button dashboard_btn;
    public Button browseProperty_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        ModelCentral.getInstance().getGuestViewFactory().setSelectedMenuItem(GUEST_MENU_OPTION.DASHBOARD);
        signUp_btn.setOnAction(e -> signUp());
        rentalManager_btn.setOnAction(e -> rentalManager());
        browseProperty_btn.setOnAction(e -> browseProperty());
        browseUser_btn.setOnAction(e -> browseUser());
        logOut_btn.setOnAction(e -> logOut());
        dashboard_btn.setOnAction(e -> dashboard());
        decor();
    }

    private void decor(){
        UIDecorator.setNormalButton(dashboard_btn, UIDecorator.USER, "Dashboard");
        UIDecorator.setNormalButton(signUp_btn, UIDecorator.REGISTER, "Sign Up");
        UIDecorator.setNormalButton(rentalManager_btn, UIDecorator.RENTAL, "Rental Manager");
        UIDecorator.setNormalButton(browseUser_btn, UIDecorator.OTHER_PERSON, "Browse User");
        UIDecorator.setNormalButton(browseProperty_btn, UIDecorator.PROPERTY, "Browse Property");
        UIDecorator.setNormalButton(logOut_btn, UIDecorator.LOG_OUT, "Log Out");
    }

    private void browseProperty() {
        ModelCentral.getInstance().getGuestViewFactory().setSelectedMenuItem(GUEST_MENU_OPTION.BROWSE_PROPERTY);
    }

    private void signUp() {
        ModelCentral.getInstance().getGuestViewFactory().setSelectedMenuItem(GUEST_MENU_OPTION.REGISTER);
        Stage currentStage = (Stage) logOut_btn.getScene().getWindow();
        ModelCentral.getInstance().getStartViewFactory().closeStage(currentStage);
        ModelCentral.getInstance().getStartViewFactory().startApplication();
        ModelCentral.getInstance().getStartViewFactory().showRegisterView();
    }

    private void rentalManager() {
        ModelCentral.getInstance().getStartViewFactory().confirmMessage("This feature is not available for guest" +
                "Register now! to become a host and enjoy this feature");
    }

    private void browseUser() {
        ModelCentral.getInstance().getGuestViewFactory().setSelectedMenuItem(GUEST_MENU_OPTION.BROWSE_USER);
    }

    private void logOut() {
        ModelCentral.getInstance().getGuestViewFactory().setSelectedMenuItem(GUEST_MENU_OPTION.LOG_OUT);

        Stage currentStage = (Stage) logOut_btn.getScene().getWindow();
        ModelCentral.getInstance().getStartViewFactory().closeStage(currentStage);
        ModelCentral.getInstance().getStartViewFactory().startApplication();
    }

    private void dashboard() {
        ModelCentral.getInstance().getGuestViewFactory().setSelectedMenuItem(GUEST_MENU_OPTION.DASHBOARD);
    }
}
