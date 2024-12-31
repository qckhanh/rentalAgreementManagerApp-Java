package org.rmit.controller.Admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.rmit.model.ModelCentral;

import java.net.URL;
import java.util.ResourceBundle;

import static org.rmit.view.Admin.ADMIN_MENU_OPTION.DASHBOARD;
import static org.rmit.view.Admin.ADMIN_MENU_OPTION.RENTER_MANAGER;

public class AdminController implements Initializable {
    @FXML
    BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // nothing here
        ModelCentral.getInstance().getAdminViewFactory().selectedMenuItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case RENTER_MANAGER -> borderPane.setCenter(ModelCentral.getInstance().getAdminViewFactory().getRenterManagerView());
                case DASHBOARD -> borderPane.setCenter(ModelCentral.getInstance().getAdminViewFactory().getAdmin_dashboardView());
            }
        });
    }
}
