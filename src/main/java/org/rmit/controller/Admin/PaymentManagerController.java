package org.rmit.controller.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.rmit.database.HostDAO;
import org.rmit.database.PaymentDAO;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Persons.Host;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentManagerController implements Initializable {
    public Label welcomeLabel;
    public ComboBox propertyTypeFilter_comboBox;
    public ComboBox propertyStatusFilter_comboBox;
    public Button addPaymentButton;
    public Button updatePaymentButton;
    public Button deletePaymentButton;
    public Button readPaymentButton;
    public TableView payments_tableView;
    private ObservableList<Payment> paymentObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        payments_tableView.getColumns().addAll(
                createColumn("Payment ID", "paymentId"),
                createColumn("Date", "date"),
                createColumn("Rental Agreement", "rentalAgreement")
        );
        payments_tableView.setItems(paymentObservableList);
        PaymentDAO paymentDAO = new PaymentDAO();
        List<Payment> list = paymentDAO.getAll();
        loadData(list);
    }

    private TableColumn<Payment, ?> createColumn(String columnName, String propertyName) {
        TableColumn<Payment, ?> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    private void loadData(List<Payment> list) {
        paymentObservableList.setAll(list);
    }
}
