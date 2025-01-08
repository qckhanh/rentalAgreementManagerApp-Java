package org.rmit.controller.Renter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import net.synedra.validatorfx.Validator;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.database.DAOInterface;
import org.rmit.database.PaymentDAO;
import org.rmit.database.RenterDAO;
import org.rmit.model.Agreement.AgreementStatus;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Agreement.PaymentMethod;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Person;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.Property;
import org.rmit.model.Session;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

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

    public Label payment_method_err;
    public Label agreement_err;
    public Validator validator = new Validator();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            selectedProperty = new SimpleObjectProperty<>();
            selectedPaymentMethod = new SimpleObjectProperty<>();
            selectedRentalAgreement = new SimpleObjectProperty<>();

            mainRenter_input.setDisable(true);
            property_input.setDisable(true);
            amount_input.setEditable(false);
            purchaseDate_datepicker.setDisable(true);
            purchaseDate_datepicker.setValue(LocalDate.now());

            // Format the date
            purchaseDate_datepicker.setConverter(new StringConverter<LocalDate>() {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

                @Override
                public String toString(LocalDate date) {
                    if (date != null) {
                        return dateFormatter.format(date);
                    } else {
                        return "";
                    }
                }

                @Override
                public LocalDate fromString(String string) {
                    if (string != null && !string.isEmpty()) {
                        return LocalDate.parse(string, dateFormatter);
                    } else {
                        return null;
                    }
                }
            });


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
            rentalAgreement_ComboBox.setOnAction(e -> {
                selectedRentalAgreement();
                agreement_err.setText("");
            });
            paymentMethod_comboBox.getItems().addAll(
                    PaymentMethod.CARD,
                    PaymentMethod.CASH
            );
            paymentMethod_comboBox.setOnAction(e -> {
                selectedPaymentMethod.set((PaymentMethod) paymentMethod_comboBox.getValue());
                payment_method_err.setText("");
            });

            submit_btn.setOnAction(e -> {
                if (validator.validate()) submitPayment();
            });
            validateInput();
            clearLabels();
            submit_btn.disableProperty().bind(validator.containsErrorsProperty());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateInput() {
        validator.createCheck()
                .dependsOn("paymentMethod", paymentMethod_comboBox.valueProperty())
                .withMethod(context -> {
                    PaymentMethod input = context.get("paymentMethod");
                    if (input == null) {
                        context.error("Please select a payment method");
                        payment_method_err.setText("Please select a payment method");
                    }
                })
                .decorates(paymentMethod_comboBox)
                .immediateClear();

        validator.createCheck()
                .dependsOn("rentalAgreement", rentalAgreement_ComboBox.valueProperty())
                .withMethod(context -> {
                    RentalAgreement input = context.get("rentalAgreement");
                    if (input == null) {
                        context.error("Please select a rental agreement");
                        agreement_err.setText("Please select a rental agreement");
                    }
                })
                .decorates(rentalAgreement_ComboBox)
                .immediateClear();
    }

    private void submitPayment() {
        if(!ModelCentral.getInstance().getStartViewFactory().confirmMessage("Do you want to make this payment?")) return;
        Payment newPayment = new Payment();
        newPayment.setRentalAgreement(selectedRentalAgreement.get());
        newPayment.setProperty(selectedProperty.get());
        newPayment.setAmount(Float.parseFloat(amount_input.getText()));
        newPayment.setPaymentMethod(selectedPaymentMethod.get());
        newPayment.setDate(purchaseDate_datepicker.getValue());
        newPayment.setMainRenter((Renter) Session.getInstance().getCurrentUser());
        PaymentDAO dao = new PaymentDAO();
        if (dao.add(newPayment)) {
            clearLabels();
            System.out.println("Payment successful");
            RenterDAO renterDAO = new RenterDAO();
            int id = (int)Session.getInstance().getCurrentUser().getId();
            Renter currentRenter = renterDAO.get(id, EntityGraphUtils::RenterFULL);
            Session.getInstance().setCurrentUser(currentRenter);
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

    private void clearLabels() {
        payment_method_err.setText("");
        agreement_err.setText("");
    }
}
