package org.rmit.controller.Host;

import javafx.beans.property.ObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.Helper.ImageUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.database.OwnerDAO;
import org.rmit.view.ViewCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Person;
import org.rmit.model.Property.Property;
import org.rmit.model.Session;
import org.rmit.view.Start.NOTIFICATION_TYPE;

import java.net.URL;
import java.util.*;

public class Host_ManageOwnerController implements Initializable {
    public TextField D_input;
    public TextField username_input;
    public TextField fullName_input;
    public TextField contact_input;
    public TextField dob_input;
    public ListView<Property> managingProperty_listView;
    public TableView<Owner> host_tableView;
    public TextField search_input;
    public Button search_btn;
    public Map<Integer, Owner> ownerMap = new HashMap<>();
    public ImageView avatarOwner_imageView;

    public ObjectProperty<Person> currentPerson = Session.getInstance().currentUserProperty();
    public ObjectProperty<Set<Owner>> owners = ((Host)currentPerson.get()).cooperatingOwnersPropertyProperty();
    public AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        search_btn.setOnAction(e ->{
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.WARNING, anchorPane, "Loading data...");
            searchOwner();
        });
        search_input.setOnAction(e -> searchOwner());
        search_input.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isBlank()) loadOwner(owners.get());
        });
        host_tableView.setOnMouseClicked(e ->{
            avatarOwner_imageView.setImage(ImageUtils.byteToImage(null));
            showDetails(host_tableView.getSelectionModel().getSelectedItem());
        });
        host_tableView.getColumns().addAll(
                newColumn("ID", "id"),
                newColumn("Full Name", "name")
        );
        loadOwner(owners.get());
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
        decor();
    }

    private void decor(){
        UIDecorator.buttonIcon(search_btn, UIDecorator.SEARCH());
    }

    private void searchOwner() {
        if(search_input.getText().isBlank()) return;
        search_btn.setDisable(true);
        OwnerDAO ownerDAO = new OwnerDAO();
        List<Owner> lists = ownerDAO.search(search_input.getText(), EntityGraphUtils::SimpleOwner);
        loadOwner(new HashSet<>(lists));
        ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Found " + lists.size() + " owner(s)");
        search_btn.setDisable(false);
//        search_input.clear();
    }

    private void loadOwner(Set<Owner> set){
        host_tableView.getItems().clear();
        host_tableView.getItems().addAll(set);
    }

    private TableColumn<Owner, ?> newColumn(String colName, String propertyName){
        TableColumn<Owner, ?> col = new TableColumn<>(colName);
        col.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return col;
    }

    private void showDetails(Owner owner){
        if(owner == null) return;
        int id = Integer.parseInt(owner.getId()+"");
        if(ownerMap.containsKey(id)) owner = ownerMap.get(id);
        else{
            OwnerDAO ownerDAO = new OwnerDAO();
            owner = ownerDAO.get(id, EntityGraphUtils::ownerForSearching);
            ownerMap.put(id, owner);
        }
        if(owner == null) return;

        D_input.setText(owner.getId()+"");
        username_input.setText(owner.getUsername());
        fullName_input.setText(owner.getName());
        contact_input.setText(owner.getContact());
        dob_input.setText(owner.getDateOfBirth().toString());
        managingProperty_listView.getItems().clear();
        managingProperty_listView.getItems().addAll(owner.getPropertiesOwned());
        avatarOwner_imageView.setImage(ImageUtils.byteToImage(owner.getProfileAvatar()));
    }

}
