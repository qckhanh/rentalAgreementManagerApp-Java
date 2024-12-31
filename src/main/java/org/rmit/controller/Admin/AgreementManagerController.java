package org.rmit.controller.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.rmit.database.RentalAgreementDAO;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.Persons.Renter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AgreementManagerController implements Initializable {
    public Label welcomeLabel;

    public ComboBox propertyTypeFilter_comboBox;
    public ComboBox propertyStatusFilter_comboBox;
    public Button addAgreementButton;
    public Button updateAgreeementButton;
    public Button deleteAgreementButton;
    public Button readAgreementButton;
    public TableView agreements_tableView;

    private ObservableList<RentalAgreement> rentalAgreementsObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        agreements_tableView.getColumns().addAll(
          createColumn("Agreement Id", "agreementId"),
          createColumn("Contract date", "contractDate"),
                createColumn("Property Id", "property")
        );
        agreements_tableView.setItems(rentalAgreementsObservableList);
        RentalAgreementDAO rentalAgreementDAO = new RentalAgreementDAO();
        List<RentalAgreement> list = rentalAgreementDAO.getAll();
        loadData(list);
    }

    private TableColumn<RentalAgreement, ?> createColumn(String columnName, String propertyName) {
        TableColumn<RentalAgreement, ?> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    private void loadData(List<RentalAgreement> List) {
        rentalAgreementsObservableList.setAll(List);
    }
}
