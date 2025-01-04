package org.rmit.controller.Admin;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.database.HostDAO;
import org.rmit.database.PaymentDAO;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Host;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.rmit.Helper.EntityGraphUtils.SimplePaymentFull;

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
    private ObjectProperty<Payment> selectedPayment = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        payments_tableView.getColumns().addAll(
                createColumn("ID", "paymentId"),
                createColumn("Date", "date"),
                createColumn("Rental Agreement", "rentalAgreement")
        );
        payments_tableView.setItems(paymentObservableList);
        payments_tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedPayment.set((Payment) newValue);
        });

        PaymentDAO paymentDAO = new PaymentDAO();
        List<Payment> list = ModelCentral.getInstance().getAdminViewFactory().getAllPayment();
        loadData(list);

//        deletePaymentButton.setOnAction(e -> deletePayment());
        // update chua lam
        // add chua lam
        readPaymentButton.setOnAction(e-> readPayment());
    }

    private TableColumn<Payment, ?> createColumn(String columnName, String propertyName) {
        TableColumn<Payment, ?> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    private void loadData(List<Payment> list) {
        paymentObservableList.setAll(list);
    }

    //    private void deletePayment() {
//        PaymentDAO paymentDAO = new PaymentDAO();
//        int id= Integer.parseInt(selectedPayment.get().getPaymentId() + "");
//        Payment payment = paymentDAO.get(id);
//        paymentDAO.delete(payment);
//        paymentObservableList.remove(selectedPayment.get());
//    }

    private void readPayment() {
        Payment currentSelectedPayment = selectedPayment.get();
        System.out.println(currentSelectedPayment);
    }
}
