package org.rmit.controller.Host;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.rmit.database.*;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Property.Property;
import org.rmit.model.Property.ResidentialProperty;
import org.rmit.model.Session;

import java.net.URL;
import java.util.HashSet;
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
    public Set<Property> searchResult = new HashSet<>();
    public ObjectProperty<Property> selectedProperty = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//
//        selectedProperty.get().addressPropertyProperty().addListener((observable, oldValue, newValue) -> {
//            propertyID_input.setText(newValue);
//        });
//        selectedProperty.get().pricePropertyProperty().addListener((observable, oldValue, newValue) -> {
//            price_input.setText(newValue +"");
//        });
//        selectedProperty.get().ownerPropertyProperty().get().namePropertyProperty().addListener((observable, oldValue, newValue) -> {
//            ownerName_input.setText(newValue);
//        });
//        selectedProperty.get().statusPropertyProperty().addListener((observable, oldValue, newValue) -> {
//            statusProperty_input.setText(newValue.toString());
//        });

        search_btn.setOnAction(event -> searchProperty());

        selectedProperty.addListener((observable, oldValue, newValue) -> {
            showPropertyDetail(newValue);
        });

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
//                            selectedProperty.setValue(property);
                            showPropertyDetail(property);
                        }
                    });
                }
            }
        });

        System.out.println("Host_ManagePropertyController initialized");
    }

    private void searchProperty() {
        DAOInterface dao = new CommercialPropertyDAO();
        Property property = (CommercialProperty)dao.search(search_input.getText());
        System.out.println("searchProperty: "+property.getAddress() + " "+property.getId());
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
        DAOInterface dao = null;
        int id = Integer.parseInt(property.getId()+"");
        clearDataProperty();

        if(property.getType().toString().equals("COMMERCIAL")){
            dao = new CommercialPropertyDAO();
            property = (CommercialProperty)dao.get(id);
        }
        else{
            dao = new ResidentialPropertyDAO();
            property = (ResidentialProperty)dao.get(id);
        }

        System.out.println("showPropertyDetail: "+property);

        propertyID_input.setText(property.getId()+"");
        address_input.setText(property.getAddress());
        price_input.setText(property.getPrice()+"");
        ownerName_input.setText(property.getOwner().getName());
        statusProperty_input.setText(property.getStatus().toString());
        propertyType_input.setText(property.getType().toString());
        totalAgremeent_input.setText(property.getAgreementList().size()+"");
        if(property.getType().toString().equals("COMMERCIAL")){
            CommercialProperty commercialProperty = (CommercialProperty) property;
            bussinessType_input.setText(commercialProperty.getBusinessType());
            parkingSlot_input.setText(commercialProperty.getTotalParkingSpace()+"");
            squareArea_input.setText(commercialProperty.getSquareMeters()+"");
        }
        else if(property.getType().toString().equals("RESIDENTIAL")){
            ResidentialProperty residentialProperty = (ResidentialProperty) property;
            totalRoom_input.setText(residentialProperty.getTotalRoom()+"");
            totalBedroom_input.setText(residentialProperty.getTotalBedroom()+"");
            petAllow_input.setText(residentialProperty.isPetAllowed()+"");
            gadern_input.setText(residentialProperty.isHasGarden()+"");
        }

    }
}
