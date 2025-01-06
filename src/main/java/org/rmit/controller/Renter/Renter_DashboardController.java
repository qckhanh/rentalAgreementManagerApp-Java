package org.rmit.controller.Renter;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

public class Renter_DashboardController implements Initializable {
    public TextField search_input;
    public Button search_button;
    public Label welcomeLabel;
    public ObjectProperty<Person> currentUser = Session.getInstance().currentUserProperty();
    public ListView recentPayment;
    public ListView recentNotification;
    public TextArea numberOfRA;
    public TextArea numberOfPayments;

    private ObservableList<Payment> paymentObservableList = FXCollections.observableArrayList();
    private ObservableList<Notification> notificationObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        welcomeLabel.setText("Welcome " + Session.getInstance().getCurrentUser().getName());
        Session.getInstance().getCurrentUser().namePropertyProperty().addListener((observable, oldValue, newValue) ->
                welcomeLabel.setText("Welcome " + newValue)
        );
        recentPayment.setItems(paymentObservableList);
        numberOfRA.setText("Number of Rental Agreements: " + ((Renter) currentUser.get()).getAgreementList().size());
        numberOfPayments.setText("Number of Payments: " + ((Renter) currentUser.get()).getPayments().size());
        recentNotification.setItems(notificationObservableList);
        loadRecentPayment(((Renter) currentUser.get()).getPayments());
        Set<Notification> set = (currentUser.get()).getReceivedNotifications();
        System.out.println(set.size());
        loadRecentNotification(set);
        recentNotification.setItems(notificationObservableList);

    }

    private void loadRecentPayment(Set<Payment> p) {
        paymentObservableList.addAll(p);
    }

    private void loadRecentNotification(Set<Notification> s) {
        notificationObservableList.addAll(s);
    }
}
