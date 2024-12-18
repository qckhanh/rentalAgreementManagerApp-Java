package org.rmit.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmit.controller.InitController;
import org.rmit.controller.Renter.RenterController;
import org.rmit.view.Renter.RENTER_MENU_OPTION;

public class ViewFactory {
    private String FXML_PATH;
    private String RENTER_PATH;

    private ACCOUNT_TYPE accountLoginType;
    private ObjectProperty<RENTER_MENU_OPTION> renterSelectedMenuItem;
    private InitController initController;

    //Common
    private AnchorPane loginView;
    private AnchorPane registerView;

    //Renter
    private AnchorPane renter_editProfileView;
    private AnchorPane renter_dashboardView;
    private AnchorPane renter_paymentManagerView;
    private AnchorPane renter_agreementManagerView;


    //Constructor
    public ViewFactory() {
        FXML_PATH = "/org/rmit/demo/FXMLs/";
        RENTER_PATH = FXML_PATH + "Renter/";

        renterSelectedMenuItem = new SimpleObjectProperty<>(RENTER_MENU_OPTION.DASHBOARD);
        accountLoginType = ACCOUNT_TYPE.RENTER;
    }


    //Common: Login, Register
    public void startInit(){
        FXMLLoader initLoad = new FXMLLoader(getClass().getResource(FXML_PATH + "init.fxml"));
        createStage(initLoad);
        initController = initLoad.getController();
    }
    public AnchorPane getLoginView(){
        if (loginView == null){
            try {
                loginView = new FXMLLoader(getClass().getResource(FXML_PATH + "login.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading login.fxml");
            }
        }
        return loginView;
    }
    public AnchorPane getRegisterView(){
        if (registerView == null){
            try {
                registerView = new FXMLLoader(getClass().getResource(FXML_PATH + "register.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading register.fxml");
            }
        }
        return registerView;
    }
    public void showLoginView() {
        initController.openLogin();
    }
    public void showRegisterView() {
        initController.openRegister();
    }

    //Renter
    public void showRenterView(){
        FXMLLoader renterLoad = new FXMLLoader(getClass().getResource(RENTER_PATH + "renter.fxml"));
        RenterController renterController = new RenterController();
        renterLoad.setController(renterController);
        createStage(renterLoad);
    }
    public AnchorPane getRenter_EditProfileView(){
        if (renter_editProfileView == null){
            try {
                renter_editProfileView = new FXMLLoader(getClass().getResource(RENTER_PATH + "editProfile.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading edit profile.fxml");
            }
        }
        return renter_editProfileView;
    }
    public AnchorPane getRenter_dashboardView(){
        if (renter_dashboardView == null){
            try {
                renter_dashboardView = new FXMLLoader(getClass().getResource(RENTER_PATH + "dashboard.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading dashboard.fxml");
            }
        }
        return renter_dashboardView;
    }
    public AnchorPane getRenter_paymentManagerView(){
        if (renter_paymentManagerView == null){
            try {
                renter_paymentManagerView = new FXMLLoader(getClass().getResource(RENTER_PATH + "paymentManager.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading paymentManager.fxml");
            }
        }
        return renter_paymentManagerView;
    }

    public AnchorPane getRenter_agreementManagerView(){
        if (renter_agreementManagerView == null){
            try {
                renter_agreementManagerView = new FXMLLoader(getClass().getResource(RENTER_PATH + "agreementManager.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading paymentManager.fxml");
            }
        }
        return renter_agreementManagerView;
    }


    //helper methods
    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void closeStage(Stage stage) {
        stage.close();
    }

    public void resetView(){
        renter_editProfileView = null;
        renter_dashboardView = null;
    }

    public boolean confirmMessage(String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Rental Agreement Application");
        alert.setHeaderText(message);
        alert.setContentText("ALL CHANGES CANNOT BE REVERTED");
        alert.setResizable(false);
        alert.showAndWait();
        return alert.getResult().getText().equals("OK");
    }



    //Getters and Setters
    public ACCOUNT_TYPE getAccountLoginType() {
        return accountLoginType;
    }

    public void setAccountLoginType(ACCOUNT_TYPE accountLoginType) {
        this.accountLoginType = accountLoginType;
    }

    public RENTER_MENU_OPTION getRenterSelectedMenuItem() {
        return renterSelectedMenuItem.get();
    }

    public ObjectProperty<RENTER_MENU_OPTION> renterSelectedMenuItemProperty() {
        return renterSelectedMenuItem;
    }

    public void setRenterSelectedMenuItem(RENTER_MENU_OPTION renterSelectedMenuItem) {
        this.renterSelectedMenuItem.set(renterSelectedMenuItem);
    }
}