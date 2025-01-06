package org.rmit.controller.Owner;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.rmit.Notification.Notification;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Person;
import org.rmit.model.Property.Property;
import org.rmit.model.Session;
import org.rmit.view.Owner.OWNER_MENU_OPTION;

import java.net.URL;
import java.util.*;

public class Owner_DashboardController implements Initializable {
    public Label welcomeLabel;
    public TableView<Notification> recentNotiTableView;
    private final ObservableList<Notification> notificationObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeLabel.setText("Welcome " + Session.getInstance().getCurrentUser().getName());
        Session.getInstance().getCurrentUser().namePropertyProperty().addListener((observableValue, oldValue, newValue) ->
                welcomeLabel.setText("Welcome " + newValue)
        );

        loadRecentNotification(((Owner) Session.getInstance().getCurrentUser()).getReceivedNotifications());
        recentNotiTableView.getColumns().addAll(
                createColumn("Header", "header"),
                createColumn("Time", "timestamp")
        );
        recentNotiTableView.setItems(notificationObservableList);

    }

    private void loadRecentNotification(Set<Notification> s) {
        notificationObservableList.addAll(s);
    }

    private TableColumn<Notification, ?> createColumn(String columnName, String propertyName) {
        TableColumn<Notification, ?> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }


}
