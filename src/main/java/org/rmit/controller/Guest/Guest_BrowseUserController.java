package org.rmit.controller.Guest;

import javafx.beans.property.ObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import org.rmit.Helper.ImageUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.database.HostDAO;
import org.rmit.database.OwnerDAO;
import org.rmit.model.Persons.Person;
import org.rmit.model.Session;

import java.net.URL;
import java.util.*;

public class Guest_BrowseUserController implements Initializable {
    public TextField D_input;
    public TextField username_input;
    public TextField fullName_input;
    public TextField contact_input;
    public TextField dob_input;
//    public ListView<Property> managingProperty_listView;
    public TableView<Person> host_tableView;
    public TextField search_input;
    public Button search_btn;
    public Map<Integer, Person> personMap = new HashMap<>();

    public ObjectProperty<Person> currentPerson = Session.getInstance().currentUserProperty();
    public ImageView avatar;
//    public ObjectProperty<Set<Owner>> owners = ((Host)currentPerson.get()).cooperatingOwnersPropertyProperty();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        search_btn.setOnAction(e -> searchOwner());
        search_input.setOnAction(e -> searchOwner());
        search_input.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isBlank()) loadOwner(new HashSet<>());
        });
        host_tableView.setOnMouseClicked(e -> showDetails(host_tableView.getSelectionModel().getSelectedItem()));
        host_tableView.getColumns().addAll(
                newColumn("ID", "id"),
                newColumn("Full Name", "name")
        );
        loadOwner(new HashSet<>());
        decor();
    }

    private void decor(){
        UIDecorator.buttonIcon(search_btn, UIDecorator.SEARCH);
    }

    private void searchOwner() {
        if(search_input.getText().isBlank()) return;
        OwnerDAO ownerDAO = new OwnerDAO();
        HostDAO hostDAO = new HostDAO();
        Set<Person> lists = new HashSet<>();
        lists.addAll(ownerDAO.search(search_input.getText()));
        lists.addAll(hostDAO.search(search_input.getText()));
        loadOwner(lists);
    }

    private void loadOwner(Set<Person> set){
        host_tableView.getItems().clear();
        host_tableView.getItems().addAll(set);
    }

    private TableColumn<Person, ?> newColumn(String colName, String propertyName){
        TableColumn<Person, ?> col = new TableColumn<>(colName);
        col.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return col;
    }

    private void showDetails(Person person){
        if(person == null) return;
        int id = Integer.parseInt(person.getId()+"");
        if(personMap.containsKey(id)) person = personMap.get(id);
        else{
            OwnerDAO ownerDAO = new OwnerDAO();
            person = ownerDAO.get(id);
            if(person == null){
                HostDAO hostDAO = new HostDAO();
                person = hostDAO.get(id);
            }
            personMap.put(id, person);
        }
        if(person == null) return;

        D_input.setText(person.getId()+"");
        username_input.setText(person.getUsername());
        fullName_input.setText(person.getName());
        contact_input.setText(person.getContact());
        dob_input.setText(person.getDateOfBirth().toString());
        avatar.setImage(ImageUtils.byteToImage(person.getProfileAvatar()));
//        managingProperty_listView.getItems().clear();
//        managingProperty_listView.getItems().addAll(owner.getPropertiesOwned());
    }

}
