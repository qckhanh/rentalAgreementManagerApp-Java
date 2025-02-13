package org.rmit.controller.Host;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.rmit.Helper.ImageUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.view.ViewCentral;
import org.rmit.model.Session;
import org.rmit.view.Host.HOST_MENU_OPTION;

import java.net.URL;
import java.util.ResourceBundle;

public class Host_MenuController implements Initializable {
    public Label name_label;
    public Label userType_label;
    public Button editProfile_btn;
    public Button dashboard_btn;
    public Button logOut_btn;
    public Button propertyManager_btn;
    public Button agreement_btn;
    public Button ownerManager_btn;
    public ImageView avatar_ImageView;
    public Button notifications_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //usertype
        userType_label.setText(Session.getInstance().getCurrentUser().getClass().getSimpleName());

        //name
        name_label.setText(Session.getInstance().getCurrentUser().getName());
        Session.getInstance().getCurrentUser().namePropertyProperty().addListener((observable, oldValue, newValue) ->
                name_label.setText(newValue)
        );

        //avatar
        avatar_ImageView.setImage(ImageUtils.byteToImage(Session.getInstance().getCurrentUser().getProfileAvatar()));
        Session.getInstance().getCurrentUser().profileAvatarPropertyProperty().addListener((observable, oldValue, newValue) -> {
            avatar_ImageView.setImage(ImageUtils.byteToImage(newValue));
        });
        setActionButton();
        decor();
    }
    private void decor(){
        UIDecorator.setNormalButton(dashboard_btn, UIDecorator.USER(), "Dashboard");
        UIDecorator.setNormalButton(editProfile_btn, UIDecorator.PROFILE(), "Edit Profile");
        UIDecorator.setDangerButton(logOut_btn, UIDecorator.LOG_OUT(), "Log Out");
        UIDecorator.setNormalButton(propertyManager_btn, UIDecorator.PROPERTY(), "Property Manager");
        UIDecorator.setNormalButton(ownerManager_btn, UIDecorator.OTHER_PERSON(), "Cooperating Owner");
        UIDecorator.setNormalButton(agreement_btn, UIDecorator.RENTAL(), "Agreement Manager");
        UIDecorator.setNormalButton(notifications_btn, UIDecorator.NOTIFICATION(), "Notification");
    }

    private void setActionButton(){
        editProfile_btn.setOnAction(e -> editProfile());
        dashboard_btn.setOnAction(e -> openDashboard());
        logOut_btn.setOnAction(e -> logOut());
        propertyManager_btn.setOnAction(e -> manageProperty());
        agreement_btn.setOnAction(e -> manageAgreement());
        ownerManager_btn.setOnAction(e -> manageOwner());
        notifications_btn.setOnAction(e -> openNotification());
    }



    private void logOut() {
        ViewCentral.getInstance().getHostViewFactory().setSelectedMenuItem(HOST_MENU_OPTION.DASHBOARD);
        ViewCentral.getInstance().getHostViewFactory().resetView();
        ViewCentral.getInstance().getStartViewFactory().logOut(logOut_btn);
    }

    private void openDashboard() {
        ViewCentral.getInstance().getHostViewFactory().setSelectedMenuItem(HOST_MENU_OPTION.DASHBOARD);
    }

    private void editProfile() {
        ViewCentral.getInstance().getHostViewFactory().setSelectedMenuItem(HOST_MENU_OPTION.EDIT_PROFILE);
    }

    private void manageProperty() {
        ViewCentral.getInstance().getHostViewFactory().setSelectedMenuItem(HOST_MENU_OPTION.MANAGE_PROPERTY);
    }

    private void manageAgreement() {
        ViewCentral.getInstance().getHostViewFactory().setSelectedMenuItem(HOST_MENU_OPTION.MANAGE_AGREEMENT);
    }

    private void manageOwner(){
        ViewCentral.getInstance().getHostViewFactory().setSelectedMenuItem(HOST_MENU_OPTION.MANAGE_OWNER);
    }

    private void openNotification(){
        ViewCentral.getInstance().getHostViewFactory().setSelectedMenuItem(HOST_MENU_OPTION.NOTIFICATION);
    }
}
