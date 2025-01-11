package org.rmit.controller.Owner;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.Helper.ImageUtils;
import org.rmit.Helper.TaskUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.database.HostDAO;
import org.rmit.view.ViewCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Person;
import org.rmit.model.Property.Property;
import org.rmit.model.Session;
import org.rmit.view.Start.NOTIFICATION_TYPE;

import java.net.URL;
import java.util.*;

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
    public AnchorPane anchorPane;
    public ImageView avatar_imageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UIDecorator.buttonIcon(search_btn, UIDecorator.SEARCH());
        search_btn.setOnAction(e->searchHost());
        search_input.setOnAction(e->searchHost());
        search_input.textProperty().addListener((observableValue, s, t1) -> {
            if (t1.isBlank()) {
                loadHost(hosts.get());
            }
        });
        host_tableView.setItems(FXCollections.observableArrayList(hosts.get()));
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
        avatar_imageView.setImage(ImageUtils.byteToImage(null));

    }

    private void searchHost() {
        if (search_input.getText().isBlank()) return;
        ViewCentral.getInstance().getStartViewFactory().standOnNotification(NOTIFICATION_TYPE.INFO, anchorPane, "Searching for host(s) ...");
        search_btn.setDisable(true);

        HostDAO hostDAO = new HostDAO();
        Task<List<Host>> loadHostTask = TaskUtils.createTask(() -> hostDAO.search(search_input.getText(), EntityGraphUtils::SimpleHost));
        TaskUtils.run(loadHostTask);

        loadHostTask.setOnSucceeded(e -> Platform.runLater(() -> {
            Set<Host> hostSet = new HashSet<>(loadHostTask.getValue());
            loadHost(hostSet);
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, hostSet.size() + " host(s) found");
            search_btn.setDisable(false);
        }));
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
        ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.INFO, anchorPane, "Loading data...");
        int id = Integer.parseInt(h.getId()+"");
        HostDAO hostDAO = new HostDAO();

        Task<Host> loadHostTask = TaskUtils.createTask(() -> {
            if (hostMap.containsKey(id)) return hostMap.get(id);
            else return hostDAO.get(id, EntityGraphUtils::hostForSearching);
        });
        TaskUtils.run(loadHostTask);
        loadHostTask.setOnSucceeded(e -> Platform.runLater(() -> {
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Host details loaded");

            Host host = loadHostTask.getValue();
            hostMap.put(id, host);
            avatar_imageView.setImage(ImageUtils.byteToImage(host.getProfileAvatar()));
            D_input.setText(host.getId()+"");
            username_input.setText(host.getUsername());
            fullName_input.setText(host.getName());
            contact_input.setText(host.getContact());
            dob_input.setText(host.getDateOfBirth().toString());
            managingProperty_listView.getItems().clear();
            managingProperty_listView.getItems().addAll(host.getPropertiesManaged());
        }));
    }
}
