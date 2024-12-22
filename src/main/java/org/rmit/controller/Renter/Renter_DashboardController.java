package org.rmit.controller.Renter;

import javafx.beans.property.ObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.rmit.model.Persons.Person;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Session;

import java.net.URL;
import java.util.ResourceBundle;

public class Renter_DashboardController implements Initializable {
    public TextField search_input;
    public Button search_button;
    public Label welcomeLabel;
    public Label totalAgreement_label;
    public Label totalPayments_label;
    public ListView upcommingPayment_listView;
    public ObjectProperty<Person> currentUser = Session.getInstance().currentUserProperty();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        welcomeLabel.setText("Welcome " + Session.getInstance().getCurrentUser().getName());
        Session.getInstance().getCurrentUser().namePropertyProperty().addListener((observable, oldValue, newValue) ->
                welcomeLabel.setText("Welcome " + newValue)
        );

//        currentUser.addListener((observable, oldValue, newValue) -> {
//            if(oldValue.equals(newValue)) return;
//            totalAgreement_label.setText("Total Agreements: " + ((Renter)currentUser.get()).getAgreementList().size());
//            totalAgreement_label.setText("Total Agreements: " + ((Renter)currentUser.get()).getAgreementList().size());
//
//        });
        totalAgreement_label.setText("Total Agreements: " + ((Renter)currentUser.get()).getAgreementList().size());
        totalPayments_label.setText("Total Payments: " + ((Renter)currentUser.get()).getPayments().size());

    }
}
