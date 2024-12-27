package org.rmit.controller.Owner;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.rmit.model.ModelCentral;
import org.rmit.view.Owner.OWNER_MENU_OPTION;

import java.net.URL;
import java.util.ResourceBundle;

public class OwnerController implements Initializable {

    @FXML
    BorderPane borderPane;

    public OwnerController() {
        ModelCentral.getInstance().getOwnerViewFactory().ownerSelectedMenuItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            switch (newValue) {
                case EDIT_PROFILE -> borderPane.setCenter(ModelCentral.getInstance().getOwnerViewFactory().getOwner_editProfileView());
                case DASHBOARD -> borderPane.setCenter(ModelCentral.getInstance().getOwnerViewFactory().getOwner_dashboardView());
                case PROPERTIES_MANAGER -> borderPane.setCenter(ModelCentral.getInstance().getOwnerViewFactory().getOwner_propertyManagerView());
                case ADD_PROPERTY -> borderPane.setCenter(ModelCentral.getInstance().getOwnerViewFactory().getOwner_addPropertyView());
                case UPDATE_PROPERTY -> borderPane.setCenter(ModelCentral.getInstance().getOwnerViewFactory().getOwner_updatePropertyView());
                case HOST_MANAGER -> borderPane.setCenter(ModelCentral.getInstance().getOwnerViewFactory().getOwner_hostManagerView());
            }
        }));
        ModelCentral.getInstance().getOwnerViewFactory().setOwnerSelectedMenuItem(OWNER_MENU_OPTION.DASHBOARD);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
