package org.rmit.view.Start;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmit.controller.Start.InitController;

public class StartViewFactory {
    private String FXML_PATH;
    private ACCOUNT_TYPE accountLoginType;
    private InitController initController;
    private AnchorPane loginView;
    private AnchorPane registerView;

    //Constructor
    public StartViewFactory() {
        FXML_PATH = "/org/rmit/demo/FXMLs/Start/";
        accountLoginType = ACCOUNT_TYPE.RENTER;
    }

    //Common: Login, Register
    public void startApplication(){
        FXMLLoader initLoad = new FXMLLoader(getClass().getResource(FXML_PATH + "init.fxml"));
        createStage(initLoad);
        initController = initLoad.getController();
    }

    public void loading(){
        FXMLLoader loading = new FXMLLoader(getClass().getResource(FXML_PATH + "loading.fxml"));
        createStage(loading);
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
}