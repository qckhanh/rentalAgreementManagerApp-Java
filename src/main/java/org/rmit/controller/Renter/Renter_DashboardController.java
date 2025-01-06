package org.rmit.controller.Renter;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.hibernate.property.access.spi.SetterMethodImpl;
import org.rmit.Notification.Notification;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Persons.Person;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Session;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class Renter_DashboardController implements Initializable {
    public TextField search_input;
    public Button search_button;
    public Label welcomeLabel;
    public ObjectProperty<Person> currentUser = Session.getInstance().currentUserProperty();
    public ListView<Payment> recentPayment;
    public ListView<Notification> recentNotification;
    public TextArea numberOfRA;
    public TextArea numberOfPayments;

    private final ObservableList<Payment> paymentObservableList = FXCollections.observableArrayList();
    private final ObservableList<Notification> notificationObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        welcomeLabel.setText("Welcome " + Session.getInstance().getCurrentUser().getName());
        Session.getInstance().getCurrentUser().namePropertyProperty().addListener((observable, oldValue, newValue) ->
                welcomeLabel.setText("Welcome " + newValue)
        );

        loadRecentNotification();
        loadRecentPayment();
        recentNotification.setItems(notificationObservableList);
        recentPayment.setItems(paymentObservableList);
    }

    private void loadRecentPayment() {
        Set<Payment> set = ((Renter) currentUser.get()).getPayments();
        Set<Payment> recentPayments = set.stream()
                .sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate()))
                .limit(3)
                .collect(Collectors.toSet());
        paymentObservableList.addAll(recentPayments);
    }



    private void loadRecentNotification() {
        Set<Notification> set = (currentUser.get()).getReceivedNotifications();
        Set<Notification> recentNotifications = set.stream()
                .sorted((n1, n2) -> n2.getTimestamp().compareTo(n1.getTimestamp())) // Assuming Notification has a getDate() method
                .limit(3)
                .collect(Collectors.toSet());
        notificationObservableList.addAll(recentNotifications);
    }

}
