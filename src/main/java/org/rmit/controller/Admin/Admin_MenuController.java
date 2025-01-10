package org.rmit.controller.Admin;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.rmit.Helper.ImageUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.view.ViewCentral;
import org.rmit.model.Session;
import org.rmit.view.Admin.ADMIN_MENU_OPTION;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class Admin_MenuController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(Admin_MenuController.class);

    public ImageView avatar_ImageView;
    public Label name_label;
    public Label userType_label;
    public Button editProfile_btn;
    public Button dashboard_btn;
    public Button logOut_btn;
    public Button renterManager_btn;
    public Button agreementManager_btn;
    public Button hostManager_btn;
    public Button ownerManager_btn;
    public Button paymentManager_btn;
    public Button property_manager;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // nothing here
        userType_label.setText(Session.getInstance().getCurrentUser().getClass().getSimpleName());

        // name:
        name_label.setText(Session.getInstance().getCurrentUser().getName());
        Session.getInstance().getCurrentUser().namePropertyProperty().addListener((observable, oldValue, newValue) ->
                name_label.setText(newValue)
        );

        // avatar:
        avatar_ImageView.setImage(ImageUtils.byteToImage(Session.getInstance().getCurrentUser().getProfileAvatar()));
        Session.getInstance().getCurrentUser().profileAvatarPropertyProperty().addListener((observable, oldValue, newValue) -> {
            avatar_ImageView.setImage(ImageUtils.byteToImage(newValue));
        });
        setActionButton();
        decor();
    }

    private void setActionButton(){
        editProfile_btn.setOnAction(e -> editProfile());
        dashboard_btn.setOnAction(e -> openDashboard());
        logOut_btn.setOnAction(e -> logOut());
        renterManager_btn.setOnAction(e -> manageRenter());
        agreementManager_btn.setOnAction(e -> manageAgreement());
        hostManager_btn.setOnAction(e -> manageHost());
        ownerManager_btn.setOnAction(e -> manageOwner());
        paymentManager_btn.setOnAction(e -> managePayment());
        property_manager.setOnAction(e -> manageProperty());
    }

    private void decor(){
        UIDecorator.setNormalButton(dashboard_btn, UIDecorator.USER(), "Dashboard");
        UIDecorator.setNormalButton(editProfile_btn, UIDecorator.PROFILE(), "Edit Profile");
        UIDecorator.setDangerButton(logOut_btn, UIDecorator.LOG_OUT(), "Log Out");
        UIDecorator.setNormalButton(renterManager_btn, UIDecorator.USER_ROLE(), "Renter Manager");
        UIDecorator.setNormalButton(hostManager_btn, UIDecorator.USER_ROLE(), "Host Manager");
        UIDecorator.setNormalButton(ownerManager_btn, UIDecorator.USER_ROLE(), "Owner Manager");
        UIDecorator.setNormalButton(property_manager, UIDecorator.PROPERTY(), "Property Manager");
        UIDecorator.setNormalButton(paymentManager_btn, UIDecorator.PAYMENT(), "Payment Manager");
        UIDecorator.setNormalButton(agreementManager_btn, UIDecorator.RENTAL(), "Agreement Manager");
    }

    private void manageProperty() {
        ViewCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.PROPERTY_MANAGER);
    }

    private void editProfile(){
        // Write your code here:
        ViewCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.EDIT_PROFILE);
    }

    private void openDashboard(){
        ViewCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.DASHBOARD);
    }

    private void logOut(){
        ViewCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.DASHBOARD);
        ViewCentral.getInstance().getAdminViewFactory().resetView();
        ViewCentral.getInstance().resetAdminView();
        ViewCentral.getInstance().getStartViewFactory().logOut(logOut_btn);
    }

    private void manageRenter(){
        ViewCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.RENTER_MANAGER);
    }

    private void manageAgreement(){
        ViewCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.AGREEMENT_MANAGER);
    }

    private void manageHost(){
        ViewCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.HOST_MANAGER);
    }

    private void manageOwner(){
        ViewCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.OWNER_MANAGER);
    }

    private void managePayment(){
        ViewCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.PAYMENT_MANAGER);
    }

    private void manageAdmin(){
        ViewCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.ADMIN_MANAGER);
    }
}
