package org.rmit.controller.Renter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Person;
import org.rmit.model.Persons.Renter;

import java.net.URL;
import java.util.ResourceBundle;

public class RenterController implements Initializable {

    @FXML
    public BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ModelCentral.getInstance().getViewFactory().renterOptionProperty().addListener((observableValue, oldVal, newVal) -> {
            switch (newVal) {
//                case DASHBOARD -> borderPane.setCenter(ModelCentral.getInstance().getViewFactory().getRenterDashboardView());
//                case LOGOUT -> borderPane.setCenter(ModelCentral.getInstance().getViewFactory().getRenterMenuView());
            }
        });
    }
}
