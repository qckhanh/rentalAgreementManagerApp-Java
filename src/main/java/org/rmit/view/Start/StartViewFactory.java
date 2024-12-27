package org.rmit.view.Start;

import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import org.rmit.controller.Start.InitController;
import org.rmit.model.ModelCentral;
import org.rmit.model.Session;

public class StartViewFactory {
    private String FXML_PATH;
    private BooleanProperty isLogin = new SimpleBooleanProperty();
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
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText(message);
            alert.setContentText("ALL CHANGES CANNOT BE REVERTED");

            ButtonType yesBtn = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType cancelBtn = new ButtonType(
                    "Cancel", ButtonBar.ButtonData.CANCEL_CLOSE
            );

            alert.getButtonTypes().setAll(yesBtn, cancelBtn);
            alert.showAndWait();
            return alert.getResult().equals(yesBtn);
    }

    public void showNotification(String message){
//        var success = new Notification(
//                message,
//                new FontIcon(Material2OutlinedAL.HELP_OUTLINE)
//        );

        var basicTtp = new Tooltip("FAKER.harryPotter().spell()");
        basicTtp.setHideDelay(Duration.seconds(3));

        var basicLbl = new Label("Basic");
        basicLbl.setTooltip(basicTtp);

        var longTtp = new Tooltip("ABC");
        longTtp.setHideDelay(Duration.seconds(3));
        longTtp.setPrefWidth(200);
        longTtp.setWrapText(true);

        var longLbl = new Label("Long Text");
        longLbl.setTooltip(longTtp);




        Stage stage = new Stage();
        stage.setScene(new Scene(new VBox(longLbl)));
        stage.show();
    }

    public void logOut(Button btn){
        Stage stage = (Stage) btn.getScene().getWindow();
        closeStage(stage);
        Session.getInstance().setCurrentUser(null);
        startApplication();
    }

    //Getters and Setters
    public ACCOUNT_TYPE getAccountLoginType() {
        return accountLoginType;
    }

    public void setAccountLoginType(ACCOUNT_TYPE accountLoginType) {
        this.accountLoginType = accountLoginType;
    }

    public boolean isIsLogin() {
        return isLogin.get();
    }

    public BooleanProperty isLoginProperty() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin.set(isLogin);
    }
}