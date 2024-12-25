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
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Property.Property;
import org.rmit.model.Property.ResidentialProperty;
import org.rmit.model.Session;

import java.net.URL;
import java.util.*;

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
//    public Set<Property> managedProperties = currentUser.getPropertiesManaged();
    public Set<Property> searchResult = new HashSet<>();
    public ObjectProperty<Set<Property>> managedProperties = new SimpleObjectProperty<>(currentUser.getPropertiesManaged());
    public ObjectProperty<Property> selectedProperty = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        search_btn.setOnAction(event -> searchProperty());
        search_input.textProperty().addListener((o, old, neww) ->{
            if(neww.isBlank()) property_listView.setItems(getPropertyList(managedProperties.get()));
        });

        selectedProperty.addListener((observable, oldValue, newValue) -> {
            showPropertyDetail(newValue);
        });

        property_listView.setItems(getPropertyList(managedProperties.get()));

        // Create clickable rows with a custom cell factory
        property_listView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Property property, boolean empty) {
                super.updateItem(property, empty);
                if (empty || property == null) {
                    manageProperty_btn.setText("Select a property");
                    setText(null);
                    setOnMouseClicked(null); // Remove click handler for empty cells
                } else {
                    setText(property.getAddress()); // Display the address or another property field
                    setOnMouseClicked(event -> {
                        if (event.getClickCount() == 1) { // Single click
                            isManagingThisProperty(property);
                            showPropertyDetail(property);
                        }
                    });
                }
            }
        });

        System.out.println("Host_ManagePropertyController initialized");
    }

    private void searchProperty() {
        if(search_input.getText().isBlank()) return;
        Set<Property> res = new HashSet<>();
        DAOInterface dao = new CommercialPropertyDAO();
        List<CommercialProperty> ans = (List<CommercialProperty>)dao.search(search_input.getText());
        res.addAll(ans);
        dao = new ResidentialPropertyDAO();
        List<ResidentialProperty> ans2 = (List<ResidentialProperty>)dao.search(search_input.getText());
        res.addAll(ans2);
        property_listView.setItems(getPropertyList(res));
    }

    public ObservableList<Property> getPropertyList(Set<Property> pList){
        ObservableList<Property> list = FXCollections.observableArrayList();
        list.addAll(pList);
        return list;

    }

    private void unmanageProperty(Property property){
        if(!ModelCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to unmanage this property?")) return;
        ((Host)Session.getInstance().getCurrentUser()).removeProperty(property);
        DAOInterface dao = new HostDAO();
        dao.update(Session.getInstance().getCurrentUser());
        managedProperties.get().remove(property);
        property_listView.setItems(getPropertyList(managedProperties.get()));

        System.out.println("Successfully unmanaged property");

    }

    private void requestManageProperty(Property property){
        if(!ModelCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to request manage this property?")) return;
        ((Host)Session.getInstance().getCurrentUser()).addProperty(property);
        DAOInterface dao = new HostDAO();
        dao.update(Session.getInstance().getCurrentUser());
        System.out.println("Successfully requested manage property");
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

    private void isManagingThisProperty(Property property){
        for(Property p: managedProperties.get()){
            if(p.getId() == property.getId()){
                manageProperty_btn.setText("Unmanage");
                manageProperty_btn.setOnAction(event -> unmanageProperty(property));
                return;
            }
        }
        manageProperty_btn.setText("Request Manage");
        manageProperty_btn.setOnAction(event -> requestManageProperty(property));
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
