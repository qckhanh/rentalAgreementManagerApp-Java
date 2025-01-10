package org.rmit.controller.Owner;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.rmit.Helper.ImageUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.view.ViewCentral;
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
    public Button hostManager_btn;
    public ImageView avatar_ImageView;
    public Button notifications_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userType_label.setText(Session.getInstance().getCurrentUser().getClass().getSimpleName());

        //avatar
        avatar_ImageView.setImage(ImageUtils.byteToImage(Session.getInstance().getCurrentUser().getProfileAvatar()));
        Session.getInstance().getCurrentUser().profileAvatarPropertyProperty().addListener((observable, oldValue, newValue) -> {
            avatar_ImageView.setImage(ImageUtils.byteToImage(newValue));
        });

        //name
        name_label.setText(Session.getInstance().getCurrentUser().getName());
        Session.getInstance().getCurrentUser().namePropertyProperty().addListener((observable, oldValue, newValue) ->
                name_label.setText(newValue)
        );

        setActionButton();
        decor();
    }

    private void decor(){
        UIDecorator.setNormalButton(dashboard_btn, UIDecorator.USER(), "Dashboard");
        UIDecorator.setNormalButton(editProfile_btn, UIDecorator.PROFILE(), "Edit Profile");
        UIDecorator.setDangerButton(logOut_btn, UIDecorator.LOG_OUT(), "Log Out");
        UIDecorator.setNormalButton(propertiesManager_btn, UIDecorator.PROPERTY(), "Properties Manager");
        UIDecorator.setNormalButton(hostManager_btn, UIDecorator.OTHER_PERSON(), "Host Manager");
        UIDecorator.setNormalButton(notifications_btn, UIDecorator.NOTIFICATION(), "Notifications");
    }

    private void setActionButton(){
        editProfile_btn.setOnAction(e->editProfile());
        dashboard_btn.setOnAction(e->openDashboard());
        propertiesManager_btn.setOnAction(e->propertiesManager());
        logOut_btn.setOnAction(e->logOut());
        hostManager_btn.setOnAction(e->hostManager());
        notifications_btn.setOnAction(e->notifications());
    }

    private void notifications() {
        ViewCentral.getInstance().getOwnerViewFactory().setOwnerSelectedMenuItem(OWNER_MENU_OPTION.NOTIFICATION);
    }


    private void logOut() {
        ViewCentral.getInstance().getOwnerViewFactory().setOwnerSelectedMenuItem(OWNER_MENU_OPTION.DASHBOARD);
        ViewCentral.getInstance().getOwnerViewFactory().resetView();
        ViewCentral.getInstance().getStartViewFactory().logOut(logOut_btn);
    }

    private void openDashboard() {
        ViewCentral.getInstance().getOwnerViewFactory().setOwnerSelectedMenuItem(OWNER_MENU_OPTION.DASHBOARD);
        System.out.println("hallo!");
    }

    private void propertiesManager() {
        System.out.println("hallo! ich bin lam");
        ViewCentral.getInstance().getOwnerViewFactory().setOwnerSelectedMenuItem(OWNER_MENU_OPTION.PROPERTIES_MANAGER);
    }

    private void editProfile() {
        ViewCentral.getInstance().getOwnerViewFactory().setOwnerSelectedMenuItem(OWNER_MENU_OPTION.EDIT_PROFILE);
    }

    private void hostManager() {
        ViewCentral.getInstance().getOwnerViewFactory().setOwnerSelectedMenuItem(OWNER_MENU_OPTION.HOST_MANAGER);
    }


}
