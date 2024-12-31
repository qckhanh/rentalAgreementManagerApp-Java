package org.rmit.controller.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.rmit.database.HostDAO;
import org.rmit.database.RenterDAO;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Renter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HostManagerController implements Initializable {
    public Label welcomeLabel;
    public TableView<Host> hosts_tableView;
    public ComboBox propertyTypeFilter_comboBox;
    public ComboBox propertyStatusFilter_comboBox;
    public Button updateHostButton;
    public Button addHostButton;
    public Button deleteHostButton;
    public Button readHostButton;
    private ObservableList<Host> hostObservableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hosts_tableView.getColumns().addAll(
                createColumn("Host ID", "id"),
                createColumn("Host Contact", "contact"),
                createColumn("Host Name", "name")
        );
        hosts_tableView.setItems(hostObservableList);
        HostDAO hostDAO = new HostDAO();
        List<Host> list = hostDAO.getAll();
        loadData(list);
    }

    private TableColumn<Host, ?> createColumn(String columnName, String propertyName) {
        TableColumn<Host, ?> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    private void loadData(List<Host> list) {
        hostObservableList.setAll(list);
    }
}
