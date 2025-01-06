package org.rmit.controller.Admin;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.rmit.Helper.ImageUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.model.ModelCentral;
import org.rmit.model.Session;
import org.rmit.view.Admin.ADMIN_MENU_OPTION;
import org.rmit.view.Renter.RENTER_MENU_OPTION;
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
    public Button adminManager_btn;
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
        adminManager_btn.setOnAction(e -> manageAdmin());
        property_manager.setOnAction(e -> manageProperty());
    }

    private void decor(){
        UIDecorator.setNormalButton(dashboard_btn, UIDecorator.USER, "Dashboard");
        UIDecorator.setNormalButton(editProfile_btn, UIDecorator.PROFILE, "Edit Profile");
    }

    private void manageProperty() {
        ModelCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.PROPERTY_MANAGER);
    }

    private void editProfile(){
        // Write your code here:
        ModelCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.EDIT_PROFILE);
    }

    private void openDashboard(){
        // Write your code here:
        ModelCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.DASHBOARD);
        // Call the update data function in the dashboard controller
        AnchorPane anchorPane = ModelCentral.getInstance().getAdminViewFactory().getAdmin_dashboardView();
        Admin_DashboardController controller = ModelCentral.getInstance().getAdminViewFactory().getAdminDashboardController();
        controller.updateData();

    }

    private void logOut(){
        ModelCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.DASHBOARD);
        ModelCentral.getInstance().getAdminViewFactory().resetView();
        ModelCentral.getInstance().resetAdminView();
        ModelCentral.getInstance().getStartViewFactory().logOut(logOut_btn);
    }

    private void manageRenter(){
        ModelCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.RENTER_MANAGER);
    }

    private void manageAgreement(){
        ModelCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.AGREEMENT_MANAGER);
    }

    private void manageHost(){
        ModelCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.HOST_MANAGER);
    }

    private void manageOwner(){
        ModelCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.OWNER_MANAGER);
    }

    private void managePayment(){
        ModelCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.PAYMENT_MANAGER);
    }

    private void manageAdmin(){
        ModelCentral.getInstance().getAdminViewFactory().setSelectedMenuItem(ADMIN_MENU_OPTION.ADMIN_MANAGER);
    }
}
