package org.rmit.controller.Owner;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.Notification.Notification;
import org.rmit.database.OwnerDAO;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Person;
import org.rmit.model.Property.Property;
import org.rmit.model.Session;
import org.rmit.view.Owner.OWNER_MENU_OPTION;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class Owner_DashboardController implements Initializable {
    public Label welcomeLabel;
    public TableView<Notification> recentNotiTableView;
    private final ObservableList<Notification> notificationObservableList = FXCollections.observableArrayList();
    public Label dateShow;
    public Label propertiesValueLabel;
    public Label hostsValueLabel;
    public Button refreshButton;
    public ObjectProperty<Person> currentUser = Session.getInstance().currentUserProperty();

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
        String text = LocalDate.now().getDayOfWeek().toString() + ", " +
                LocalDate.now().getDayOfMonth() + " " +
                LocalDate.now().getMonth().toString() + " " +
                LocalDate.now().getYear();
        dateShow.setText(text);

        propertiesValueLabel.setText(String.valueOf(((Owner) Session.getInstance().getCurrentUser()).getPropertiesOwned().size()));
        hostsValueLabel.setText(String.valueOf(((Owner) Session.getInstance().getCurrentUser()).getHosts().size()));
        refreshButton.setOnAction(e -> refreshData());

    }

    private void refreshData() {
        OwnerDAO ownerDAO = new OwnerDAO();
        int id = Integer.parseInt(currentUser.get().getId()+"");
        Owner owner = ownerDAO.get(id, EntityGraphUtils::SimpleOwnerFull);
        propertiesValueLabel.setText(String.valueOf(owner.getPropertiesOwned().size()));
        hostsValueLabel.setText(String.valueOf(owner.getHosts().size()));
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
