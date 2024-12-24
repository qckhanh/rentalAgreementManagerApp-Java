package org.rmit.controller.Host;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.rmit.model.ModelCentral;

import java.net.URL;
import java.util.ResourceBundle;

public class HostController implements Initializable {

    @FXML
    private BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // nothing here
        ModelCentral.getInstance().getHostViewFactory().selectedMenuItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case DASHBOARD -> borderPane.setCenter(ModelCentral.getInstance().getHostViewFactory().getHost_dashboardView());
                case EDIT_PROFILE -> borderPane.setCenter(ModelCentral.getInstance().getHostViewFactory().getHost_EditProfileView());
//                case RENTAL_MANAGER -> borderPane.setCenter(ModelCentral.getInstance().getHostViewFactory().getHost_rentalManagerView());
//                case PAYMENT_MANAGER -> borderPane.setCenter(ModelCentral.getInstance().getHostViewFactory().getHost_paymentManagerView());
                case SEARCH -> System.out.println("Search");
            }
        });
    }
}
