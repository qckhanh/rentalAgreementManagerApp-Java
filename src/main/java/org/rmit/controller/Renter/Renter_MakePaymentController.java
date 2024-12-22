package org.rmit.controller.Renter;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.rmit.database.DAOInterface;
import org.rmit.database.PaymentDAO;
import org.rmit.database.RenterDAO;
import org.rmit.model.Agreement.AgreementStatus;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Agreement.PaymentMethod;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.Persons.Person;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.Property;
import org.rmit.model.Session;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

public class Renter_MakePaymentController implements Initializable {
    public TextField mainRenter_input;
    public ComboBox rentalAgreement_ComboBox;
    public TextField property_input;
    public TextField amount_input;
    public ComboBox paymentMethod_comboBox;
    public Button submit_btn;
    public DatePicker purchaseDate_datepicker;

    public ObjectProperty<Property> selectedProperty;
    public ObjectProperty<PaymentMethod> selectedPaymentMethod;
    public ObjectProperty<RentalAgreement> selectedRentalAgreement;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            selectedProperty = new SimpleObjectProperty<>();
            selectedPaymentMethod = new SimpleObjectProperty<>();
            selectedRentalAgreement = new SimpleObjectProperty<>();
            mainRenter_input.setDisable(true);
            property_input.setDisable(true);

            mainRenter_input.setText(Session.getInstance().getCurrentUser().getName());
            Session.getInstance().getCurrentUser().namePropertyProperty().addListener((observable, oldValue, newValue) ->
                    mainRenter_input.setText(newValue)
            );

            selectedRentalAgreement.addListener((observable, oldValue, newValue) -> {
                selectedProperty.set(newValue.getProperty());
                property_input.setText(newValue.getProperty().getAddress());
                amount_input.setText(String.valueOf(newValue.getProperty().getPrice()));
            });

            selectedProperty.addListener((observable, oldValue, newValue) -> {
                property_input.setText(newValue.getAddress());
                newValue.addressPropertyProperty().addListener((observable1, oldAddress, newAddress) ->
                        property_input.setText(newAddress)
                );
            });


            amount_input.textProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue.isBlank()) return;
                if(Float.parseFloat(newValue) != (float) selectedProperty.get().getPrice() ) {
                    amount_input.setStyle("-fx-text-fill: red");
                }
                else{
                    amount_input.setStyle("-fx-text-fill: black");
                }
            });

            rentalAgreement_ComboBox.setItems(validRentalAgreement());
            rentalAgreement_ComboBox.setOnAction(e -> selectedRentalAgreement());
            paymentMethod_comboBox.getItems().addAll(
                    PaymentMethod.CARD,
                    PaymentMethod.CASH
            );
            paymentMethod_comboBox.setOnAction(e -> {
                selectedPaymentMethod.set((PaymentMethod) paymentMethod_comboBox.getValue());
            });

            submit_btn.setOnAction(e -> submitPayment());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void submitPayment() {
        Payment newPayment = new Payment();
        newPayment.setRentalAgreement(selectedRentalAgreement.get());
        newPayment.setProperty(selectedProperty.get());
        newPayment.setAmount(Float.parseFloat(amount_input.getText()));
        newPayment.setPaymentMethod(selectedPaymentMethod.get());
        newPayment.setDate(purchaseDate_datepicker.getValue());
        newPayment.setMainRenter((Renter) Session.getInstance().getCurrentUser());
        DAOInterface dao = new PaymentDAO();
        if (dao.add(newPayment)) {
            System.out.println("Payment successful");
            dao = new RenterDAO();
            int id = (int)Session.getInstance().getCurrentUser().getId();
            Session.getInstance().setCurrentUser((Person)dao.get(id));
        } else {
            System.out.println("Payment failed");
        }
    }

    private void selectedRentalAgreement() {
        selectedRentalAgreement.setValue((RentalAgreement) rentalAgreement_ComboBox.getValue());
    }

    private ObservableList<RentalAgreement> validRentalAgreement(){
        ObservableList<RentalAgreement> list = FXCollections.observableArrayList();
        Renter currentUser = (Renter)Session.getInstance().getCurrentUser();
        for(RentalAgreement ra : currentUser.getAgreementList()){
            if(!ra.getMainTenant().equals(currentUser)) continue;
            if(ra.getStatus() == AgreementStatus.COMPLETED) continue;
            list.add(ra);
        }
        return list;
    }
}
