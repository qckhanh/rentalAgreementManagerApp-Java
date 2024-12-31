package org.rmit.controller.Admin;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.rmit.Helper.ImageUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.model.ModelCentral;
import org.rmit.model.Session;
import org.rmit.view.Admin.ADMIN_MENU_OPTION;

import java.net.URL;
import java.util.ResourceBundle;

public class Admin_MenuController implements Initializable {
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
    }

    private void decor(){
        UIDecorator.setNormalButton(dashboard_btn, UIDecorator.USER, "Dashboard");
        UIDecorator.setNormalButton(editProfile_btn, UIDecorator.PROFILE, "Edit Profile");
    }

    private void editProfile(){
        // Write your code here:
        ModelCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.EDIT_PROFILE);
    }

    private void openDashboard(){
        // Write your code here:
        ModelCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.DASHBOARD);
    }

    private void logOut(){
        // Write your code here:
    }

    private void manageRenter(){
        // Write your code here:
        ModelCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.RENTER_MANAGER);
    }

    private void manageAgreement(){
        // Write your code here:
    }

    private void manageHost(){
        // Write your code here:
    }

    private void manageOwner(){
        // Write your code here:
    }

    private void managePayment(){
        // Write your code here:
    }
}
