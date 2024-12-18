package org.rmit.view;

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
import org.rmit.view.Renter.RenterMenuOption;

public class ViewFactory {
    private String FXML_PATH = "/org/rmit/demo/FXMLs/";
    private ACCOUNT_TYPE accountLoginType;
    private InitController initController;
    private AnchorPane loginView;
    private AnchorPane registerView;

    private final ObjectProperty<RenterMenuOption> renterOption;


    public ViewFactory() {
        accountLoginType = ACCOUNT_TYPE.RENTER;
        this.renterOption = new SimpleObjectProperty<>();
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


    public void startInit(){
        FXMLLoader initLoad = new FXMLLoader(getClass().getResource(FXML_PATH + "init.fxml"));
        showStage(initLoad);
        initController = initLoad.getController();

    }

    public void showRegisterView() {
        initController.openRegister();
    }

    public void showLoginView() {
        initController.openLogin();
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

    public void showRenterView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH + "renter.fxml"));
        RenterController controller = new RenterController();
        loader.setController(controller);
        showStage(loader);
    }
}