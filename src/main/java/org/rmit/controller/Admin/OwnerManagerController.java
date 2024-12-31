package org.rmit.controller.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.rmit.database.HostDAO;
import org.rmit.database.OwnerDAO;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OwnerManagerController implements Initializable {
    public Label welcomeLabel;
    public ComboBox propertyTypeFilter_comboBox;
    public ComboBox propertyStatusFilter_comboBox;
    public Button addOwnerButton;
    public Button updateOwnerButton;
    public Button deleteOwnerButton;
    public Button readOwnerButton;
    public TableView owners_tableView;
    private ObservableList<Owner> ownerObservableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        owners_tableView.getColumns().addAll(
                createColumn("Owner ID", "id"),
                createColumn("Owner Contact", "contact"),
                createColumn("Owner Name", "name")
        );
        owners_tableView.setItems(ownerObservableList);
        OwnerDAO ownerDAO = new OwnerDAO();
        List<Owner> list = ownerDAO.getAll();
        loadData(list);
    }

    private TableColumn<Owner, ?> createColumn(String columnName, String propertyName) {
        TableColumn<Owner, ?> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    private void loadData(List<Owner> list) {
        ownerObservableList.setAll(list);
    }
}
