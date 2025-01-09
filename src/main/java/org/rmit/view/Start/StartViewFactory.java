package org.rmit.view.Start;

import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import org.rmit.Helper.UIDecorator;
import org.rmit.controller.Start.InitController;
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

    public void pushNotification(NOTIFICATION_TYPE type, Pane pane, String message){
        if (pane.getChildren().size() > 0) {
            for (Node node : pane.getChildren()) {
                if (node instanceof Notification) {
                    pane.getChildren().remove(node);
                    break;
                }
            }
        }

        FontIcon icon = null;
        String style = null;

        if(type == NOTIFICATION_TYPE.SUCCESS){
            icon = new FontIcon(Material2OutlinedAL.CHECK_CIRCLE);
            style = Styles.SUCCESS;
        } else if (type == NOTIFICATION_TYPE.ERROR){
            icon = UIDecorator.FAIL();
            style = Styles.DANGER;
        } else if (type == NOTIFICATION_TYPE.INFO){
            icon = UIDecorator.INFO();
            style = Styles.ACCENT;
        }
        else if (type == NOTIFICATION_TYPE.WARNING){
            icon = UIDecorator.WARNING();
            style = Styles.WARNING;
        }


        Notification notification = new Notification(
                message,
                icon
        );
        notification.getStyleClass().addAll(
                style, Styles.ELEVATED_1
        );

        notification.setPrefHeight(Region.USE_PREF_SIZE);
        notification.setMaxHeight(Region.USE_PREF_SIZE);
//        StackPane.setAlignment(notification, Pos.BOTTOM_LEFT);

        notification.setLayoutY(10); // 10px from the top
        notification.setLayoutX(pane.getWidth() - notification.getPrefWidth() - 10); // 10px from the right

        // Listen to the width and height changes of the pane
        pane.widthProperty().addListener((obs, oldVal, newVal) -> {
            notification.setLayoutX(pane.getWidth() - notification.getPrefWidth() - 10); // Recalculate X position
        });

        pane.heightProperty().addListener((obs, oldVal, newVal) -> {
            notification.setLayoutY(pane.getHeight() - notification.getPrefHeight() - 10); // Recalculate Y position
        });

        StackPane.setMargin(notification, new Insets(10, 10, 0, 0));

        Button btn = new Button("Show");

        notification.setOnClose(e -> {
            Timeline out = Animations.slideOutUp(notification, Duration.millis(300));
            out.setOnFinished(f -> pane.getChildren().remove(notification));
            out.playFromStart();
        });

        Timeline autoDismiss = new Timeline(
                new KeyFrame(Duration.seconds(2), e -> { // Set duration here (e.g., 5 seconds)
                    Timeline fadeOut = Animations.slideOutUp(notification, Duration.millis(300));
                    fadeOut.setOnFinished(f -> pane.getChildren().remove(notification));
                    fadeOut.playFromStart();
                })
        );
        autoDismiss.setCycleCount(1); // Execute once
        autoDismiss.play();


        Timeline slideIn = Animations.slideInDown(notification, Duration.millis(250));
        if (!pane.getChildren().contains(notification)) {
            pane.getChildren().add(notification);
        }
        slideIn.playFromStart();

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