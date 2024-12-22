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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class Renter_PaymentManagerController implements Initializable {
    public TableView paymentList_tableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ((Renter) Session.getInstance().getCurrentUser()).paymentsPropertyProperty().addListener((observable, oldValue, newValue) -> {
            loadPayments();
            System.out.println(" >> Payment list updated");
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
        loadPayments();
    }

    // for clickable cell
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

    //for primitive cell, no clickable
    private TableColumn<Payment, ?> createColumn(String columnName, String propertyName) {
        TableColumn<Payment, ?> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    private void loadPayments() {
        ObservableList<Payment> paymentObservableList = FXCollections.observableArrayList(((Renter) Session.getInstance().getCurrentUser()).getPayments());
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
