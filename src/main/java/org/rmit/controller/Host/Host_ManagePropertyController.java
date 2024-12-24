package org.rmit.controller.Host;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.Persons.Host;
import org.rmit.model.Property.Property;
import org.rmit.model.Session;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

public class Host_ManagePropertyController implements Initializable {
    //preview
    public TextField propertyID_input;
    public TextField address_input;
    public TextField price_input;
    public TextField ownerName_input;
    public TextField statusProperty_input;
    public TextField propertyType_input;
    public TextField totalAgremeent_input;
    public TextField bussinessType_input;
    public TextField parkingSlot_input;
    public TextField squareArea_input;
    public TextField totalRoom_input;
    public TextField totalBedroom_input;
    public TextField petAllow_input;
    public TextField gadern_input;

    //manage
    public ListView<Property> property_listView;
    public Button manageProperty_btn;

    //search
    public TextField search_input;
    public Button search_btn;
    public Host currentUser = (Host) Session.getInstance().getCurrentUser();
    public Set<Property> managedProperties = currentUser.getPropertiesManaged();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        property_listView.setItems(getPropertyList());

        // Create clickable rows with a custom cell factory
        property_listView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Property property, boolean empty) {
                super.updateItem(property, empty);
                if (empty || property == null) {
                    setText(null);
                    setOnMouseClicked(null); // Remove click handler for empty cells
                } else {
                    setText(property.getAddress()); // Display the address or another property field
                    setOnMouseClicked(event -> {
                        if (event.getClickCount() == 1) { // Single click
                            showPropertyDetail(property);
                        }
                    });
                }
            }
        });

        System.out.println("Host_ManagePropertyController initialized");
    }

    public ObservableList<Property> getPropertyList(){
        ObservableList<Property> list = FXCollections.observableArrayList();
        list.addAll(managedProperties);
        return list;

    }

    private void clearDataCommercial(){
        bussinessType_input.setText("");
        parkingSlot_input.setText("");
        squareArea_input.setText("");
    }

    private void clearDataResidential(){
        totalRoom_input.setText("");
        totalBedroom_input.setText("");
        petAllow_input.setText("");
        gadern_input.setText("");
    }

    private void clearDataProperty(){
        propertyID_input.setText("");
        address_input.setText("");
        price_input.setText("");
        ownerName_input.setText("");
        statusProperty_input.setText("");
        propertyType_input.setText("");
        totalAgremeent_input.setText("");
        clearDataCommercial();
        clearDataResidential();
    }

    private void showPropertyDetail(Property property){
        System.out.println("showPropertyDetail");
    }
}
