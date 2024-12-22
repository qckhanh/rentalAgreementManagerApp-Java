package org.rmit.controller.Renter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.rmit.model.Agreement.*;
import org.rmit.model.Persons.*;
import org.rmit.model.Property.*;
import org.rmit.model.Session;
import java.net.URL;
import java.util.*;

public class Renter_PaymentManagerController implements Initializable {
    public TableView paymentList_tableView;
    public ComboBox<PaymentMethod> paymentFilter_comboBox;
    public ComboBox<Property> propertyFilter_comboBox;
    public ComboBox<RentalAgreement> agreementFilter_comboBox;

    ObjectProperty<PaymentMethod> selectedPaymentFilter = new SimpleObjectProperty<>();
    ObjectProperty<Property> selectedPropertyFilter = new SimpleObjectProperty<>();
    ObjectProperty<RentalAgreement> selectedAgreementFilter = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ((Renter) Session.getInstance().getCurrentUser()).paymentsPropertyProperty().addListener((observable, oldValue, newValue) -> {
            loadPayments(((Renter) Session.getInstance().getCurrentUser()).getPayments());
            System.out.println(" >> Payment list updated");
        });

        paymentFilter_comboBox.setOnAction(event -> {
            selectedPaymentFilter.set(paymentFilter_comboBox.getValue());
        });
        paymentFilter_comboBox.getItems().addAll(
                PaymentMethod.CARD,
                PaymentMethod.CASH,
                PaymentMethod.NONE
        );
        propertyFilter_comboBox.setOnAction(event -> {
            selectedPropertyFilter.set(propertyFilter_comboBox.getValue());
        });
        propertyFilter_comboBox.getItems().addAll(createPropertyList());
        agreementFilter_comboBox.setOnAction(event -> {
            selectedAgreementFilter.set(agreementFilter_comboBox.getValue());
        });
        agreementFilter_comboBox.getItems().addAll(createRentalAgreementList());

        selectedPropertyFilter.set(null);
        selectedAgreementFilter.set(null);
        selectedPaymentFilter.set(PaymentMethod.NONE);

        selectedPropertyFilter.addListener((observable, oldValue, newValue) -> {
            Set<Payment> filter = noFilter();
            filter = filterByProperty(filter, newValue);
            filter = filterByAgreement(filter, selectedAgreementFilter.get());
            filter = filterByPaymentMethod(filter, selectedPaymentFilter.get());
            loadPayments(filter);
        });

        selectedAgreementFilter.addListener((observable, oldValue, newValue) -> {
            Set<Payment> filter = noFilter();
            filter = filterByProperty(filter, selectedPropertyFilter.get());
            filter = filterByAgreement(filter, newValue);
            filter = filterByPaymentMethod(filter, selectedPaymentFilter.get());
            loadPayments(filter);
        });

        selectedPaymentFilter.addListener((observable, oldValue, newValue) -> {
            Set<Payment> filter = noFilter();
            filter = filterByProperty(filter, selectedPropertyFilter.get());
            filter = filterByAgreement(filter, selectedAgreementFilter.get());
            filter = filterByPaymentMethod(filter, newValue);
            loadPayments(filter);
        });


        paymentList_tableView.getColumns().addAll(
                createColumn("Payment ID", "paymentId"),
                createColumn(
                        "Rental Agreement ID",
                        "rentalAgreement",
                        payment -> payment.getRentalAgreement().getAgreementId(),
                        payment -> showAgreement(payment.getRentalAgreement())
                ),
                createColumn(
                        "Property ID",
                        "property",
                        payment -> payment.getProperty().getId(),
                        payment -> showProperty(payment.getProperty())
                ),
                createColumn("Amount", "amount"),
                createColumn("Date", "date"),
                createColumn("Method", "paymentMethod")
        );
        loadPayments(((Renter) Session.getInstance().getCurrentUser()).getPayments());
    }

    private <T> TableColumn<Payment, T> createColumn(String columnName, String propertyName, java.util.function.Function<Payment, T> extractor, java.util.function.Consumer<Payment> onClickAction) {
        TableColumn<Payment, T> column = new TableColumn<>(columnName);

        column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(extractor.apply(cellData.getValue())));

        // Set a custom cell factory to handle clicks on individual cells
        column.setCellFactory(col -> new TableCell<Payment, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle(""); // Reset cell style for empty cells
                    setOnMouseClicked(null); // Remove any click listeners when the cell is empty
                } else {
                    setText(item.toString());
                    setOnMouseClicked(event -> {
                        if (event.getClickCount() == 1 && onClickAction != null) {
                            Payment payment = getTableView().getItems().get(getIndex());
                            onClickAction.accept(payment);
                        }
                    });
                }
            }
        });

        return column;
    }
    private TableColumn<Payment, ?> createColumn(String columnName, String propertyName) {
        TableColumn<Payment, ?> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    ObservableList<Property> createPropertyList() {
        Set<Property> properties = new HashSet<>();
        properties.add(null);
        for (RentalAgreement ra : ((Renter) Session.getInstance().getCurrentUser()).getAgreementList()) {
            properties.add(ra.getProperty());
        }
        return FXCollections.observableArrayList(properties);
    }

    ObservableList<RentalAgreement> createRentalAgreementList() {
        Set<RentalAgreement> rentalAgreements = new HashSet<>();
        rentalAgreements.add(null);
        for (RentalAgreement ra : ((Renter) Session.getInstance().getCurrentUser()).getAgreementList()) {
            if(!ra.getMainTenant().equals(Session.getInstance().getCurrentUser())) continue;
            rentalAgreements.add(ra);
        }
        return FXCollections.observableArrayList(rentalAgreements);
    }

    private Set<Payment> noFilter(){
        return ((Renter) Session.getInstance().getCurrentUser()).getPayments();
    }

    private Set<Payment> filterByPaymentMethod(Set<Payment> payments, PaymentMethod paymentMethod){
        if(paymentMethod == PaymentMethod.NONE) return payments;
        if(paymentMethod == null) return payments;
        Set<Payment> filteredPayments = new HashSet<>();
        for(Payment payment : payments){
            if(payment.getPaymentMethod().equals(paymentMethod)){
                filteredPayments.add(payment);
            }
        }
        return filteredPayments;
    }

    private Set<Payment> filterByProperty(Set<Payment> payments, Property property){
        if(property == null) return payments;
        Set<Payment> filteredPayments = new HashSet<>();
        for(Payment payment : payments){
            if(payment.getProperty().equals(property)){
                filteredPayments.add(payment);
            }
        }
        return filteredPayments;
    }

    private Set<Payment> filterByAgreement(Set<Payment> payments, RentalAgreement agreement){
        if(agreement == null) return payments;
        Set<Payment> filteredPayments = new HashSet<>();
        for(Payment payment : payments){
            if(payment.getRentalAgreement().equals(agreement)){
                filteredPayments.add(payment);
            }
        }
        return filteredPayments;
    }

    private void loadPayments(Set<Payment> payments) {
        ObservableList<Payment> paymentObservableList = FXCollections.observableArrayList(payments);
        paymentList_tableView.setItems(paymentObservableList);
        System.out.println("Im done");
    }

    private void showProperty(Property property){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Property Details");
        alert.setHeaderText("Details of the selected property");
        alert.setContentText(
                "Property ID: " + ((property != null) ?  property.getId() : null) + "\n" +
                "Owner's Name: " + ((property.getOwner() != null) ? property.getOwner().getName(): null) + "\n" +
                "Property Address: " + property.getAddress() + "\n" +
                "Property Price: " + property.getPrice() + "\n" +
                "Property Status: " + property.getStatus().toString() + "\n"
        );
        alert.showAndWait();

    }

    private void showAgreement(RentalAgreement ra){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Property Details");
        alert.setHeaderText("Details of the selected property");
        alert.setContentText(
                "Agreement ID: " + ((ra != null) ?  ra.getAgreementId() : null) + "\n" +
                "Property Address: " + ((ra.getProperty() != null) ? ra.getProperty().getAddress() : null) + "\n" +
                "Main renter's Name: " + ((ra.getMainTenant() != null) ? ra.getMainTenant().getName() : null) + "\n" +
                "Agreement Status: " + ra.getStatus().toString() + "\n" +
                "Contract date: " + ra.getContractDate()
        );
        alert.showAndWait();

    }

}
