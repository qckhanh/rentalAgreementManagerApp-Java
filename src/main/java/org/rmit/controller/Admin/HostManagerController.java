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
import org.rmit.database.RenterDAO;
import org.rmit.model.ModelCentral;
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
    private ObjectProperty<Host> selectedHost = new SimpleObjectProperty<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hosts_tableView.getColumns().addAll(
                createColumn("Host ID", "id"),
                createColumn("Host Contact", "contact"),
                createColumn("Host Name", "name")
        );
        hosts_tableView.setItems(hostObservableList);
        hosts_tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedHost.set(newValue);
        });

        HostDAO hostDAO = new HostDAO();
        List<Host> list = ModelCentral.getInstance().getAdminViewFactory().getAllHost();
        loadData(list);

        deleteHostButton.setOnAction(e -> deleteHost());
        // update chua lam
        // add chua lam
        readHostButton.setOnAction(e-> readHost());
    }

    private TableColumn<Host, ?> createColumn(String columnName, String propertyName) {
        TableColumn<Host, ?> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    private void loadData(List<Host> list) {
        hostObservableList.setAll(list);
    }

    // Helper Methods:
    private void deleteHost() {
//        HostDAO hostDAO = new HostDAO();
//        int id = Integer.parseInt(selectedHost.get().getId() + "");
//        Host host = hostDAO.get(id);
//        hostDAO.delete(host);
//        hostObservableList.remove(selectedHost.get());
    }

    private void readHost() {
        Host currentSelectedHost = selectedHost.get();
        System.out.println(currentSelectedHost);
    }
}
