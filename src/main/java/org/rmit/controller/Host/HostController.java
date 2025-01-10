package org.rmit.controller.Host;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.rmit.view.ViewCentral;

import java.net.URL;
import java.util.ResourceBundle;

public class HostController implements Initializable {

    @FXML
    private BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // nothing here
        ViewCentral.getInstance().getHostViewFactory().selectedMenuItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case DASHBOARD -> borderPane.setCenter(ViewCentral.getInstance().getHostViewFactory().getHost_dashboardView());
                case EDIT_PROFILE -> borderPane.setCenter(ViewCentral.getInstance().getHostViewFactory().getHost_EditProfileView());
                case MANAGE_PROPERTY -> borderPane.setCenter(ViewCentral.getInstance().getHostViewFactory().getHost_ManagePropertyView());
                case MANAGE_AGREEMENT -> borderPane.setCenter(ViewCentral.getInstance().getHostViewFactory().getHost_agreementManagerView());
                case MANAGE_OWNER -> borderPane.setCenter(ViewCentral.getInstance().getHostViewFactory().getHost_OwnerManagerView());
                case SEARCH -> System.out.println("Search");
                case NOTIFICATION -> borderPane.setCenter(ViewCentral.getInstance().getHostViewFactory().getHost_NotificationView());
            }
        });
    }
}
