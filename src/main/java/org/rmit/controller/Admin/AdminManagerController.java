package org.rmit.controller.Admin;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.rmit.database.AdminDAO;
import org.rmit.model.Persons.Admin;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminManagerController implements Initializable {
    public Label welcomeLabel;
    public TableView agreements_tableView;
    public ComboBox propertyTypeFilter_comboBox;
    public ComboBox propertyStatusFilter_comboBox;
    public Button addAdminButton;
    public Button updateAdminButton;
    public Button deleteAdminButton;
    public Button readAdminButton;

    public TableView<Admin> admin_tableView = new TableView<>();
    private ObservableList<Admin> adminObservableList = FXCollections.observableArrayList();
    private ObjectProperty<Admin> selectedAdmin = new SimpleObjectProperty<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        admin_tableView.getColumns().addAll(
                createColumn("Admin ID", "id"),
                createColumn("Admin Contact", "contact"),
                createColumn("Admin Name", "name")
        );

        admin_tableView.setItems(adminObservableList);
        admin_tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedAdmin.set(newValue);
        });

        AdminDAO adminDAO = new AdminDAO();
//        List<Admin> list = adminDAO.getAll();
//        loadData(list);

        deleteAdminButton.setOnAction(e -> deleteAdmin());
        // update chua lam
        // add chua lam
        readAdminButton.setOnAction(e-> readAdmin());
    }

    private TableColumn<Admin, ?> createColumn(String columnName, String propertyName) {
        TableColumn<Admin, ?> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    private void loadData(List<Admin> list) {
        adminObservableList.setAll(list);
    }

    private void deleteAdmin() {
        AdminDAO adminDAO = new AdminDAO();
        int id = Integer.parseInt(selectedAdmin.get().getId() + "");
        Admin admin = selectedAdmin.get();
        adminDAO.delete(admin);
        adminObservableList.remove(selectedAdmin.get());
    }

    private void readAdmin() {
        Admin currentSelectedAdmin = selectedAdmin.get();
        System.out.println(currentSelectedAdmin);
    }
}
