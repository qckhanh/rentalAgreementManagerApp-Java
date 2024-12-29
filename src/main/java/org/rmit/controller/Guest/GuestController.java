package org.rmit.controller.Guest;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.rmit.model.ModelCentral;
import org.rmit.view.Guest.GUEST_MENU_OPTION;
import org.rmit.view.Host.HOST_MENU_OPTION;

import java.net.URL;
import java.util.ResourceBundle;

public class GuestController implements Initializable {

    @FXML
    private BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ModelCentral.getInstance().getGuestViewFactory().getSelectedMenuItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case DASHBOARD -> borderPane.setCenter(ModelCentral.getInstance().getGuestViewFactory().getGuest_dashboardView());
                case BROWSE_USER -> borderPane.setCenter(ModelCentral.getInstance().getGuestViewFactory().getGuest_BrowsePersonView());
                case BROWSE_PROPERTY -> borderPane.setCenter(ModelCentral.getInstance().getGuestViewFactory().getGuest_BrowsePropertyView());
//                case REGISTER -> borderPane.setCenter(ModelCentral.getInstance().getStartViewFactory().getRegisterView());
//                case LOG_OUT -> System.out.println("Log out");
            }
        });
    }
}
