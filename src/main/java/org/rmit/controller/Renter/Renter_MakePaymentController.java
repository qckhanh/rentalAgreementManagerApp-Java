package org.rmit.controller.Renter;

import atlantafx.base.controls.ModalPane;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.util.StringConverter;
import net.synedra.validatorfx.Validator;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import org.rmit.Helper.EntityGraphUtils;
import org.rmit.Helper.TaskUtils;
import org.rmit.database.PaymentDAO;
import org.rmit.database.RenterDAO;
import org.rmit.model.Agreement.AgreementStatus;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Agreement.PaymentMethod;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.view.ViewCentral;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.Property;
import org.rmit.model.Session;
import org.rmit.view.Start.NOTIFICATION_TYPE;

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
    public AnchorPane anchorPane;

    public Label payment_method_err;
    public Label agreement_err;
    public Validator validator = new Validator();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            selectedProperty = new SimpleObjectProperty<>();
            selectedPaymentMethod = new SimpleObjectProperty<>();
            selectedRentalAgreement = new SimpleObjectProperty<>();

            mainRenter_input.setEditable(false);
            mainRenter_input.setText("You");
            property_input.setEditable(false);
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
                if(newValue == null) return;
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
                submitPayment();

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
        if (!validator.validate()){
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Please fill in all fields");
            return;
        }
        if(!ViewCentral.getInstance().getStartViewFactory().confirmMessage("Do you want to make this payment?")) return;

        submit_btn.disableProperty().unbind();
        submit_btn.setDisable(true);
        Payment newPayment = new Payment();
        newPayment.setRentalAgreement(selectedRentalAgreement.get());
        newPayment.setProperty(selectedProperty.get());
        newPayment.setAmount(Float.parseFloat(amount_input.getText()));
        newPayment.setPaymentMethod(selectedPaymentMethod.get());
        newPayment.setDate(purchaseDate_datepicker.getValue());
        newPayment.setMainRenter((Renter) Session.getInstance().getCurrentUser());
        PaymentDAO dao = new PaymentDAO();
        RenterDAO renterDAO = new RenterDAO();
        int id = (int)Session.getInstance().getCurrentUser().getId();

        Task<Boolean> paymentTask = TaskUtils.createTask(() -> dao.add(newPayment));
        Task<Renter> updateRenter = TaskUtils.createTask(() -> renterDAO.get(id, EntityGraphUtils::RenterFULL));
        updateRenter.setOnSucceeded(e -> Platform.runLater(() -> {
            Session.getInstance().setCurrentUser(updateRenter.getValue());
            submit_btn.setDisable(false);
            clearAllFields();
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Purchased successfully");
        }));
        ViewCentral.getInstance().getStartViewFactory().standOnNotification(NOTIFICATION_TYPE.INFO, anchorPane, "Creating payment ....");
        paymentTask.setOnSucceeded(e -> Platform.runLater(() -> {
            if (paymentTask.getValue()) {
                clearLabels();
                TaskUtils.run(updateRenter);
            } else {
                ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Failed to purchase. Try again");
            }

        }));
        TaskUtils.run(paymentTask);
    }

    private void clearAllFields() {
//        mainRenter_input.clear();
        property_input.clear();
        amount_input.clear();
//        purchaseDate_datepicker.setValue(null);
        rentalAgreement_ComboBox.setValue(null);
        paymentMethod_comboBox.setValue(null);
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
