package org.rmit.controller.Owner;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.rmit.Notification.Notification;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Person;
import org.rmit.model.Session;
import org.rmit.view.Owner.OWNER_MENU_OPTION;

import java.net.URL;
import java.util.*;

public class Owner_DashboardController implements Initializable {
    public TextField search_input;
    public Label welcomeLabel;
    public TextArea propertyTextArea;
    public TextArea notiTextArea;
    public ObjectProperty<Person> currentUser = Session.getInstance().currentUserProperty();
    public Button notificationBtn;
    public Button propertyBtn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeLabel.setText("Welcome " + Session.getInstance().getCurrentUser().getName());
        Session.getInstance().getCurrentUser().namePropertyProperty().addListener((observableValue, oldValue, newValue) ->
                welcomeLabel.setText("Welcome " + newValue)
        );
        propertyTextArea.setText("You have " + ((Owner)currentUser.get()).getPropertiesOwned().size() + " properties\n");
        notiTextArea.setText("You have " + currentUser.get().getReceivedNotifications().size() + " notifications waiting\n");

        notificationBtn.setText("See Notification");
        propertyBtn.setText("See property owning");
        notificationBtn.setOnAction(e-> seeNotification());
        propertyBtn.setOnAction(e -> seeProperty());
    }

    private void seeProperty() {
        ModelCentral.getInstance().getOwnerViewFactory().setOwnerSelectedMenuItem(OWNER_MENU_OPTION.PROPERTIES_MANAGER);
    }

    private void seeNotification() {
        ModelCentral.getInstance().getOwnerViewFactory().setOwnerSelectedMenuItem(OWNER_MENU_OPTION.NOTIFICATION);
    }


}
