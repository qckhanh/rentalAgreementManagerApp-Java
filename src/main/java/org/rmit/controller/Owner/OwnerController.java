package org.rmit.controller.Owner;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.rmit.view.ViewCentral;
import org.rmit.view.Owner.OWNER_MENU_OPTION;

import java.net.URL;
import java.util.ResourceBundle;

public class OwnerController implements Initializable {

    @FXML
    BorderPane borderPane;

    public OwnerController() {
        ViewCentral.getInstance().getOwnerViewFactory().ownerSelectedMenuItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            switch (newValue) {
                case EDIT_PROFILE -> borderPane.setCenter(ViewCentral.getInstance().getOwnerViewFactory().getOwner_editProfileView());
                case DASHBOARD -> borderPane.setCenter(ViewCentral.getInstance().getOwnerViewFactory().getOwner_dashboardView());
                case PROPERTIES_MANAGER -> borderPane.setCenter(ViewCentral.getInstance().getOwnerViewFactory().getOwner_propertyManagerView());
                case ADD_PROPERTY -> borderPane.setCenter(ViewCentral.getInstance().getOwnerViewFactory().getOwner_addPropertyView());
                case UPDATE_PROPERTY -> borderPane.setCenter(ViewCentral.getInstance().getOwnerViewFactory().getOwner_updatePropertyView());
                case HOST_MANAGER -> borderPane.setCenter(ViewCentral.getInstance().getOwnerViewFactory().getOwner_hostManagerView());
                case NOTIFICATION -> borderPane.setCenter(ViewCentral.getInstance().getOwnerViewFactory().getOwner_NotificationView());
            }
        }));
        ViewCentral.getInstance().getOwnerViewFactory().setOwnerSelectedMenuItem(OWNER_MENU_OPTION.DASHBOARD);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
