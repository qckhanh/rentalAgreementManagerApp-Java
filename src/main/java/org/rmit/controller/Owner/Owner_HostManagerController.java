package org.rmit.controller.Owner;

import javafx.beans.property.ObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.database.HostDAO;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Person;
import org.rmit.model.Property.Property;
import org.rmit.model.Session;

import java.net.URL;
import java.util.*;

import static org.rmit.Helper.EntityGraphUtils.SimpleHost;

public class Owner_HostManagerController implements Initializable {
    public Label welcomeLabel;
    public TextField D_input;
    public TextField username_input;
    public TextField fullName_input;
    public TextField contact_input;
    public TextField dob_input;
    public ListView<Property> managingProperty_listView;
    public TableView<Host> host_tableView;
    public TextField search_input;
    public Button search_btn;
    public Map<Integer, Host> hostMap = new HashMap<>();

    public ObjectProperty<Person> currentPerson = Session.getInstance().currentUserProperty();
    public ObjectProperty<Set<Host>> hosts = ((Owner) currentPerson.get()).hostsPropertyProperty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UIDecorator.buttonIcon(search_btn, UIDecorator.SEARCH);
        search_btn.setOnAction(e->searchHost());
        search_input.setOnAction(e->searchHost());
        search_input.textProperty().addListener((observableValue, s, t1) -> {
            if (t1.isBlank()) {
                loadHost(hosts.get());
            }
        });
        host_tableView.setOnMouseClicked(e -> showDetails(host_tableView.getSelectionModel().getSelectedItem()));
        host_tableView.getColumns().addAll(
                newColumn("ID", "id"),
                newColumn("Full Name", "name")
        );
        loadHost(hosts.get());
        managingProperty_listView.setCellFactory(param -> new ListCell<Property>(){
            @Override
            protected void updateItem(Property item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null){
                    setText(null);
                    setOnMouseClicked(null); // Remove click handler for empty cells
                } else {
                    setText("ID: " + item.getId() + " - " + item.getAddress());
                }
            }
        });
    }

    private void searchHost() {
        if (search_input.getText().isBlank()) return;
        HostDAO hostDAO = new HostDAO();
        List<Host> lists = hostDAO.search(search_input.getText(), EntityGraphUtils::SimpleHost);
        Set<Host> hostSet = new HashSet<>(lists);
        loadHost(hostSet);
    }

    private void loadHost(Set<Host> h) {
        host_tableView.getItems().clear();
        host_tableView.getItems().addAll(h);
    }

    private TableColumn<Host, ?> newColumn(String colName, String propertyName) {
        TableColumn<Host, ?> col = new TableColumn<>(colName);
        col.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return col;
    }


    private void showDetails(Host h) {
        if (h == null)  return;
        int id = Integer.parseInt(h.getId()+"");
        if (hostMap.containsKey(id)) {
            h = hostMap.get(id);
        }
        else{
            HostDAO hostDAO = new HostDAO();
            h = hostDAO.get(id, EntityGraphUtils::hostForSearching);
            hostMap.put(id, h);
        }

        D_input.setText(h.getId()+"");
        username_input.setText(h.getUsername());
        fullName_input.setText(h.getName());
        contact_input.setText(h.getContact());
        dob_input.setText(h.getDateOfBirth().toString());
        managingProperty_listView.getItems().clear();
        managingProperty_listView.getItems().addAll(h.getPropertiesManaged());
    }
}
