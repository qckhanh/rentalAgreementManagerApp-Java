package org.rmit.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmit.controller.InitController;
import org.rmit.controller.Renter.RenterController;
import org.rmit.model.Session;

public class ViewFactory {
    private ACCOUNT_TYPE accountLoginType;

    private AnchorPane loginView;
    private AnchorPane registerView;

    private String FXML_PATH = "/org/rmit/demo/FXMLs/";

    private FXMLLoader loginFXML;
    private FXMLLoader registerFXML;
    private FXMLLoader renterFXML;
    private FXMLLoader initFXML;
    private InitController initController;

    public ViewFactory() {
        accountLoginType = ACCOUNT_TYPE.RENTER;
    }

    public AnchorPane getLoginView(){
        if (loginView == null){
            try {
                getLoginFXML();
                loginView = loginFXML.load();
            } catch (Exception e){
                System.out.println("Error loading login.fxml");
            }
        }
        return loginView;
    }

    public AnchorPane getRegisterView(){
        if (registerView == null){
            try {
                registerView = getRegisterFXML().load();
            } catch (Exception e){
                System.out.println("Error loading register.fxml");
            }
        }
        return registerView;
    }


    ////////////////


    public FXMLLoader getLoginFXML() {
        if (loginFXML == null) {
            try {
                loginFXML = new FXMLLoader(getClass().getResource(FXML_PATH + "login.fxml"));
            } catch (Exception e) {
                System.out.println("Error loading register.fxml");

            }
        }
        return loginFXML;
    }

    public FXMLLoader getRegisterFXML() {
        if (registerView == null) {
            try {
                registerFXML = new FXMLLoader(getClass().getResource(FXML_PATH + "register.fxml"));
            } catch (Exception e) {
                System.out.println("Error loading register.fxml");
            }
        }
        return registerFXML;
    }

    public FXMLLoader getRenterView(){
        if(renterFXML == null) renterFXML = new FXMLLoader(getClass().getResource(FXML_PATH + "HOME.fxml"));
        return renterFXML;
    }

    ///////////////
    public void showRenterView(){
        getRenterView();
        createStage(renterFXML);
    }
    public void showLoginView() {
        initController.openLogin();
    }

    public void startInit(){
        FXMLLoader initLoad = new FXMLLoader(getClass().getResource(FXML_PATH + "init.fxml"));
        createStage(initLoad);
        initController = initLoad.getController();
    }

    public void showRegisterView() {
        try{
            initController.openRegister();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

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

    public ACCOUNT_TYPE getAccountLoginType() {
        return accountLoginType;
    }

    public void setAccountLoginType(ACCOUNT_TYPE accountLoginType) {
        this.accountLoginType = accountLoginType;
    }

    public void logOut(){
        Session.getInstance().setCurrentUser(null);
        startInit();
    }
}