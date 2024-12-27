package org.rmit.controller.Renter;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.rmit.Helper.ImageUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.model.ModelCentral;
import org.rmit.model.Session;
import org.rmit.view.Host.HOST_MENU_OPTION;
import org.rmit.view.Renter.RENTER_MENU_OPTION;

import java.net.URL;
import java.util.ResourceBundle;

public class Renter_MenuController implements Initializable {

    public Label userType_label;
    public Label name_label;

    public Button editProfile_btn;
    public Button dashboard_btn;
    public Button logOut_btn;
    public Button paymentManager_btn;
    public Button rentalManager_btn;
    public Button makePayment_btn;
    public ImageView avatar_ImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userType_label.setText(Session.getInstance().getCurrentUser().getClass().getSimpleName());

        avatar_ImageView.setImage(ImageUtils.byteToImage(Session.getInstance().getCurrentUser().getProfileAvatar()));
        Session.getInstance().getCurrentUser().profileAvatarPropertyProperty().addListener((observable, oldValue, newValue) -> {
            avatar_ImageView.setImage(ImageUtils.byteToImage(newValue));
        });

        name_label.setText(Session.getInstance().getCurrentUser().getName());
        Session.getInstance().getCurrentUser().namePropertyProperty().addListener((observable, oldValue, newValue) ->
                name_label.setText(newValue)
        );
        Session.getInstance().getCurrentUser().profileAvatarPropertyProperty().addListener((observable, oldValue, newValue) -> {
            avatar_ImageView.setImage(ImageUtils.byteToImage(newValue));
        });

        editProfile_btn.setOnAction(e -> editProfile());
        dashboard_btn.setOnAction(e -> openDashboard());
        logOut_btn.setOnAction(e -> logOut());
        paymentManager_btn.setOnAction(e -> paymentManager());
        rentalManager_btn.setOnAction(e -> rentalManager());
        makePayment_btn.setOnAction(e -> makePayment());

        decor();
    }

    private void decor(){
        UIDecorator.setNormalButton(dashboard_btn, UIDecorator.USER, "Dashboard");
        UIDecorator.setNormalButton(editProfile_btn, UIDecorator.PROFILE, "Edit Profile");
        UIDecorator.setDangerButton(logOut_btn, UIDecorator.LOG_OUT, "Log Out");
        UIDecorator.setNormalButton(paymentManager_btn, UIDecorator.PAYMENT, "Payment Manager");
        UIDecorator.setNormalButton(makePayment_btn, UIDecorator.NEW_PAYMENT, "New Payment");
        UIDecorator.setNormalButton(rentalManager_btn, UIDecorator.RENTAL, "Rental Manager");
    }

    private void logOut(){
        ModelCentral.getInstance().getRenterViewFactory().setRenterSelectedMenuItem(RENTER_MENU_OPTION.DASHBOARD);
        ModelCentral.getInstance().getRenterViewFactory().resetView();
        ModelCentral.getInstance().getStartViewFactory().logOut(logOut_btn);
    }

    private void openDashboard(){
        ModelCentral.getInstance().getRenterViewFactory().setRenterSelectedMenuItem(RENTER_MENU_OPTION.DASHBOARD);
    }

    private void editProfile(){
        ModelCentral.getInstance().getRenterViewFactory().setRenterSelectedMenuItem(RENTER_MENU_OPTION.EDIT_PROFILE);
    }

    private void paymentManager(){
        ModelCentral.getInstance().getRenterViewFactory().setRenterSelectedMenuItem(RENTER_MENU_OPTION.PAYMENT_MANAGER);
    }

    private void rentalManager(){
        ModelCentral.getInstance().getRenterViewFactory().setRenterSelectedMenuItem(RENTER_MENU_OPTION.AGREEMENT_MANAGER);
    }

    private void makePayment(){
        ModelCentral.getInstance().getRenterViewFactory().setRenterSelectedMenuItem(RENTER_MENU_OPTION.MAKE_PAYMENT);
    }
}
