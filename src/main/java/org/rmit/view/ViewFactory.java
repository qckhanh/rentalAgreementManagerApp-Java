package org.rmit.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmit.controller.InitController;
import org.rmit.controller.Renter.RenterController;
import org.rmit.model.Session;
import org.rmit.view.Renter.RENTER_MENU_OPTION;

public class ViewFactory {
    private String FXML_PATH = "/org/rmit/demo/FXMLs/";
    private ACCOUNT_TYPE accountLoginType;

    public RENTER_MENU_OPTION getRenterSelectedMenuItem() {
        return renterSelectedMenuItem.get();
    }

    public ObjectProperty<RENTER_MENU_OPTION> renterSelectedMenuItemProperty() {
        return renterSelectedMenuItem;
    }

    public void setRenterSelectedMenuItem(RENTER_MENU_OPTION renterSelectedMenuItem) {
        this.renterSelectedMenuItem.set(renterSelectedMenuItem);
    }

    private ObjectProperty<RENTER_MENU_OPTION> renterSelectedMenuItem;

    private AnchorPane editProfileView;
    private AnchorPane dashboardView;

    private AnchorPane loginView;
    private AnchorPane registerView;
    private String FXML_PATH = "/org/rmit/demo/FXMLs/";

    private InitController initController;

    public ViewFactory() {
        renterSelectedMenuItem = new SimpleObjectProperty<>();
        accountLoginType = ACCOUNT_TYPE.RENTER;
    }

    public AnchorPane getEditProfile(){
        if (editProfileView == null){
            try {
                editProfileView = new FXMLLoader(getClass().getResource(FXML_PATH + "editProfile.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading edit profile.fxml");
            }
        }
        return editProfileView;
    }

    public AnchorPane getDashboardView(){
        if (dashboardView == null){
            try {
                dashboardView = new FXMLLoader(getClass().getResource(FXML_PATH + "dashboard.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading dashboard.fxml");
            }
        }
        return dashboardView;
    }

    public AnchorPane getLoginView(){
        if (loginView == null){
            try {
                loginView = new FXMLLoader(getClass().getResource(FXML_PATH + "login.fxml")).load();
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
                registerView = new FXMLLoader(getClass().getResource(FXML_PATH + "register.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading register.fxml");
            }
        }
        return registerView;
    }

    public void showRenterView(){
        FXMLLoader renterLoad = new FXMLLoader(getClass().getResource(FXML_PATH + "renter.fxml"));
        RenterController renterController = new RenterController();
        renterLoad.setController(renterController);
        createStage(renterLoad);
    }

    public void startInit(){
        FXMLLoader initLoad = new FXMLLoader(getClass().getResource(FXML_PATH + "init.fxml"));
        showStage(initLoad);
        initController = initLoad.getController();
    }
    public void showLoginView() {
        initController.openLogin();
    }
    public void showRegisterView() {
        initController.openRegister();
    }

    private void showStage(FXMLLoader newFXML) {
        Scene scene = null;
        try {
            scene = new Scene(newFXML.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


    public void logOut(){

    }


    public void closeStage(Stage stage) {
        stage.close();
    }

    public ACCOUNT_TYPE getAccountLoginType() {
        return accountLoginType;
    }

    public ObjectProperty<RenterMenuOption> renterOptionProperty() {
        return renterOption;
    }

    public void setRenterOption(RenterMenuOption option) {
        renterOption.setValue(option);
    }

    public void setAccountLoginType(ACCOUNT_TYPE accountLoginType) {
        this.accountLoginType = accountLoginType;
    }

}