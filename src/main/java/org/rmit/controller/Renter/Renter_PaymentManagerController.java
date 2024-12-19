package org.rmit.controller.Renter;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.rmit.database.CommercialPropertyDAO;
import org.rmit.database.DAOInterface;
import org.rmit.database.PaymentDAO;
import org.rmit.model.Agreement.AgreementStatus;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Agreement.PaymentMethod;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.Persons.Person;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Property.Property;
import org.rmit.model.Session;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;

public class Renter_PaymentManagerController implements Initializable {
    public TableView paymentList_tableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initializing Renter's Payment Manager");

        // Create columns for the TableView
        TableColumn<Payment, Integer> paymentIdColumn = new TableColumn<>("Payment ID");
        paymentIdColumn.setCellValueFactory(new PropertyValueFactory<>("paymentId"));

//        TableColumn<Payment, Integer> rentalAgreement = new TableColumn<>("RentalAgreement");
//        paymentIdColumn.setCellValueFactory(new PropertyValueFactory<>("rentalAgreement"));
//
//        TableColumn<Payment, Renter> rentalAgreement = new TableColumn<>("RentalAgreement");
//        paymentIdColumn.setCellValueFactory(new PropertyValueFactory<>("rentalAgreement"));



        // Add the columns to the TableView
        paymentList_tableView.getColumns().addAll(
                createColumn("Payment ID", "paymentId"),
                createCustomClickableColumn(
                        "Rental Agreement ID",
                        "rentalAgreement",
                        payment -> payment.getRentalAgreement().getAgreementId(),
                        payment -> showAgreement(payment.getRentalAgreement())
                ),
                createCustomClickableColumn(
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

    private <T> TableColumn<Payment, T> createCustomClickableColumn(String columnName, String propertyName, java.util.function.Function<Payment, T> extractor, java.util.function.Consumer<Payment> onClickAction) {
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

    private void loadPayments() {
        // Example: Retrieve data from the database using DAO
//        DAOInterface<Payment> paymentDAO = new PaymentDAO();
        Renter renter = (Renter)Session.getInstance().getCurrentUser();
        System.out.println(renter.getName());
        List<Payment> payments = new ArrayList<>();

        try {
            for(Payment x: renter.getPayments()){
                payments.add(x);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Convert the list of payments to an ObservableList
        ObservableList<Payment> paymentObservableList = FXCollections.observableArrayList(payments);

        // Set the data to the TableView
        paymentList_tableView.setItems(paymentObservableList);
    }

    private <T> TableColumn<Payment, T> createCustomColumn(String columnName, Function<Payment, T> extractor) {
        TableColumn<Payment, T> column = new TableColumn<>(columnName);
        column.setCellValueFactory(cellData -> {
            Payment payment = cellData.getValue();
            if (payment != null) {
                return new SimpleObjectProperty<>(extractor.apply(payment));
            }
            return null;
        });
        return column;
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
