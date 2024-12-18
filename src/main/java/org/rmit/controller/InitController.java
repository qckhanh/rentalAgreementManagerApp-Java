package org.rmit.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.rmit.model.ModelCentral;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InitController implements Initializable {
    @FXML
    private BorderPane parent_BorderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        openLogin();
    }

    public void openRegister(){
        try{
            parent_BorderPane.setCenter(ModelCentral.getInstance().getViewFactory().getRegisterView());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void openLogin(){
        try{
            parent_BorderPane.setCenter(ModelCentral.getInstance().getViewFactory().getLoginView());
        }
        catch (Exception e){
            System.out.println("Error loading login.fxml");
        }
    }

}
