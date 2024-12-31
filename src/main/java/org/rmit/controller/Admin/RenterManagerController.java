package org.rmit.controller.Admin;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.rmit.database.RenterDAO;
import org.rmit.model.Persons.Renter;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class RenterManagerController implements Initializable {
    public Label welcomeLabel;
    public ComboBox propertyTypeFilter_comboBox; //not used yet
    public ComboBox propertyStatusFilter_comboBox; //not used yet
    public Button addRenterButton;
    public Button updateRenterButton;
    public Button deleteRenterButton;
    public Button readRenterButton;
    public TableView<Renter> renters_tableView;
    private ObservableList<Renter> renterObservableList = FXCollections.observableArrayList();
    private ObjectProperty<Renter> selectedRenter = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        renters_tableView.getColumns().addAll(
            createColumn("Renter ID", "id"),
                createColumn("Renter Contact", "contact"),
                createColumn("Renter Name", "name")
        );
        renters_tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedRenter.set(newValue);
        });
        renters_tableView.setItems(renterObservableList);
        RenterDAO renterDAO = new RenterDAO();
        List<Renter> list = renterDAO.getAll();
        loadData(list);

        deleteRenterButton.setOnAction(e -> deleteRenter());
        // update chua lam
        // add chua lam
        readRenterButton.setOnAction(e-> readRenter());
    }

    private TableColumn<Renter, ?> createColumn(String columnName, String propertyName) {
        TableColumn<Renter, ?> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    private void loadData(List<Renter> renterList) {
        renterObservableList.setAll(renterList);
    }

    // Helper Method:
    private void deleteRenter() {
        RenterDAO renterDAO = new RenterDAO();
        int id = Integer.parseInt(selectedRenter.get().getId() + "");
        Renter renter = renterDAO.get(id);
        renterDAO.delete(renter);
        renterObservableList.remove(selectedRenter.get());
    }

    private void readRenter() {
        Renter currentSelectedRenter = selectedRenter.get();
        System.out.println(currentSelectedRenter);
    }
}
