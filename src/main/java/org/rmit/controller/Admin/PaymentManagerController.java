package org.rmit.controller.Admin;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.rmit.database.PaymentDAO;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Agreement.PaymentMethod;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.Property;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

public class PaymentManagerController implements Initializable {
    public Label welcomeLabel;
    public TableView<Payment> payments_tableView;
    public ComboBox<RentalAgreement> agreementFilter_comboBox;
    public ComboBox<PaymentMethod> PaymentMethodFilter_comboBox;
    public Button addPayment_btn;
    public Button savePayment_btn;
    public TextField id_input;
    public TextField amount_input;
    public DatePicker paymentDate_datePicker;
    public ComboBox<PaymentMethod> paymentMethod_comboBox;
    public ComboBox<RentalAgreement> agreement_comboBox;
    public ComboBox<Property> property_comboBox;
    public ComboBox<Renter> mainRenter_comboBox;

    private ObservableList<Payment> paymentObservableList = FXCollections.observableArrayList();
    private ObjectProperty<Payment> selectedPayment = new SimpleObjectProperty<>();
    List<Payment> paymentList = ModelCentral.getInstance().getAdminViewFactory().getAllPayment();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableComboxBox();
        settingTableComboBox();
        setupActionButton();
    }
    
    private void setupActionButton() {
        addPayment_btn.setOnAction(e -> addPayment());
        savePayment_btn.setOnAction(e -> saveToDB());
    }

    private void setupTableComboxBox(){
        payments_tableView.getColumns().addAll(
                createColumn("ID", "paymentId"),
                createColumn("Agreement", "property", payment -> payment.getProperty().getAddress()),
                createColumn("Date", "date")
        );
        payments_tableView.setItems(paymentObservableList);
        payments_tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedPayment.set(newValue);
            showPaymentDetail(newValue);
        });

        loadData(paymentList);

        agreement_comboBox.setItems(FXCollections.observableArrayList(ModelCentral.getInstance().getAdminViewFactory().getAllRentalAgreement()));
        agreementFilter_comboBox.setItems(FXCollections.observableArrayList(ModelCentral.getInstance().getAdminViewFactory().getAllRentalAgreement()));
        PaymentMethodFilter_comboBox.getItems().addAll(
                PaymentMethod.CARD,
                PaymentMethod.CASH
        );
        paymentMethod_comboBox.getItems().addAll(
                PaymentMethod.CARD,
                PaymentMethod.CASH
        );

        agreement_comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                property_comboBox.setValue(newValue.getProperty());
                mainRenter_comboBox.setValue(newValue.getMainTenant());
            }
        });
    }

    private void settingTableComboBox(){
        mainRenter_comboBox.setButtonCell(new ListCell<Renter>() {
            @Override
            protected void updateItem(Renter item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select Renter");
                } else {
                    setText("ID: " + item.getId() + " - " + item.getName());
                }
            }
        });

        mainRenter_comboBox.setCellFactory(param -> new ListCell<Renter>() {
            @Override
            protected void updateItem(Renter item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select Renter");
                } else {
                    setText("ID: " + item.getId() + " - " + item.getName());
                }
            }
        });

        property_comboBox.setButtonCell(new ListCell<Property>() {
            @Override
            protected void updateItem(Property item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select Property");
                } else {
                    setText("ID: " + item.getId() + " - " + item.getAddress());
                }
            }
        });

        property_comboBox.setCellFactory(param -> new ListCell<Property>() {
            @Override
            protected void updateItem(Property item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select Property");
                } else {
                    setText("ID: " + item.getId() + " - " + item.getAddress());
                }
            }
        });


        agreement_comboBox.setButtonCell(new ListCell<RentalAgreement>() {
            @Override
            protected void updateItem(RentalAgreement item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select Agreement");
                } else {
                    setText(item.getAgreementId() + " - " + item.getProperty().addressPropertyProperty().get());
                }
            }
        });

        agreementFilter_comboBox.setButtonCell(new ListCell<RentalAgreement>() {
            @Override
            protected void updateItem(RentalAgreement item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select Agreement");
                } else {
                    setText(item.getAgreementId() + " - " + item.getProperty().addressPropertyProperty().get());
                }
            }
        });

        agreement_comboBox.setCellFactory(param -> new ListCell<RentalAgreement>() {
            @Override
            protected void updateItem(RentalAgreement item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select Agreement");
                } else {
                    setText(item.getAgreementId() + " - " + item.getProperty().addressPropertyProperty().get());
                }
            }
        });

        agreementFilter_comboBox.setCellFactory(param -> new ListCell<RentalAgreement>() {
            @Override
            protected void updateItem(RentalAgreement item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select Agreement");
                } else {
                    setText(item.getAgreementId() + " - " + item.getProperty().addressPropertyProperty().get());
                }
            }
        });

    }


    private void saveToDB() {
        if(!isChanged(new Payment())) return;
        Payment payment = new Payment();
        payment.setAmount(Double.parseDouble(amount_input.getText()));
        payment.setDate(paymentDate_datePicker.getValue());
        payment.setPaymentMethod(paymentMethod_comboBox.getValue());
        payment.setRentalAgreement(agreement_comboBox.getValue());
        RentalAgreement agreement = agreement_comboBox.getValue();
        payment.setProperty(agreement.getProperty());
        payment.setMainRenter(agreement.getMainTenant());

        if(!ModelCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to update this payment?")) return;

        PaymentDAO paymentDAO = new PaymentDAO();
        boolean isUpdated = paymentDAO.update(payment);

        if(isUpdated){
            paymentList.add(payment);
            paymentObservableList.setAll(paymentList);
            System.out.println("Payment updated");
        }
        else{
            System.out.println("Payment not updated");
        }


    }

    private void addPayment() {
        clearPaymentDetail();
        setEditable(true);
        savePayment_btn.setVisible(true);
    }

    private void showPaymentDetail(Payment payment) {
        if(payment == null) return;
        setEditable(false);
        id_input.setText(payment.getPaymentId() + "");
        amount_input.setText(payment.getAmount() + "");
        paymentDate_datePicker.setValue(payment.getDate());
        paymentMethod_comboBox.setValue(payment.getPaymentMethod());
        agreement_comboBox.setValue(payment.rentalAgreementPropertyProperty().get());
        property_comboBox.setValue((Property) payment.propertyPropertyProperty().get());
        mainRenter_comboBox.setValue(payment.mainRenterPropertyProperty().get());
    }

    private void clearPaymentDetail() {
        id_input.clear();
        amount_input.clear();
        paymentDate_datePicker.setValue(null);
        paymentMethod_comboBox.setValue(null);
        agreement_comboBox.setValue(null);
        property_comboBox.setValue(null);
        mainRenter_comboBox.setValue(null);
    }

    private boolean isChanged(Payment payment) {
        if(payment == null) return false;
        if(!id_input.getText().equals(payment.getPaymentId() + "")) return true;
        if(!amount_input.getText().equals(payment.getAmount() + "")) return true;
        if(!paymentDate_datePicker.getValue().equals(payment.getDate())) return true;
        if(!paymentMethod_comboBox.getValue().equals(payment.getPaymentMethod())) return true;
        if(!agreement_comboBox.getValue().equals(payment.getRentalAgreement())) return true;
        if(!property_comboBox.getValue().equals(payment.getProperty())) return true;
        if(!mainRenter_comboBox.getValue().equals(payment.getMainRenter())) return true;
        return false;
    }

    private void setEditable(boolean isEditable) {
        id_input.setEditable(false);
        amount_input.setEditable(isEditable);
        paymentDate_datePicker.setEditable(isEditable);
        paymentMethod_comboBox.setDisable(!isEditable);
        agreement_comboBox.setDisable(!isEditable);
        property_comboBox.setDisable(true);
        mainRenter_comboBox.setDisable(true);
    }

    private void loadData(List<Payment> list) {
        paymentObservableList.setAll(list);
    }


    private <S, T> TableColumn<S, T> createColumn(String columnName, String propertyName) {
        TableColumn<S, T> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    private <S, T> TableColumn<S, T> createColumn(String columnName, String propertyName, Function<S, T> extractor) {
        TableColumn<S, T> column = new TableColumn<>(columnName);

        column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(extractor.apply(cellData.getValue())));
        column.setCellFactory(col -> new TableCell<S, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle(""); // Reset cell style for empty cells
                    setOnMouseClicked(null); // Remove any click listeners when the cell is empty
                } else {
                    setText(item.toString());
                }
            }
        });

        return column;
    }
}
