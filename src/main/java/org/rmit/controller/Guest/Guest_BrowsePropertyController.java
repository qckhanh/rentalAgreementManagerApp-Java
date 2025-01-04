package org.rmit.controller.Guest;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.database.CommercialPropertyDAO;
import org.rmit.database.DAOInterface;
import org.rmit.database.HostDAO;
import org.rmit.database.ResidentialPropertyDAO;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Property.Property;
import org.rmit.model.Property.ResidentialProperty;
import org.rmit.model.Session;

import java.net.URL;
import java.util.*;

import static org.rmit.Helper.EntityGraphUtils.SimpleCommercialProperty;

public class Guest_BrowsePropertyController implements Initializable {
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
//    public ObjectProperty<Set<Property>> managedProperties = new SimpleObjectProperty<>(currentUser.getPropertiesManaged());
    public ObjectProperty<Property> selectedProperty = new SimpleObjectProperty<>();
    public Map<Integer, Property> propertyMap = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UIDecorator.buttonIcon(search_btn, new FontIcon(Material2MZ.SEARCH));
        UIDecorator.setNormalButton(manageProperty_btn, UIDecorator.MANAGE, "Select a property");

        search_btn.setOnAction(event -> searchProperty());
        search_input.setOnAction(event -> searchProperty());
        search_input.textProperty().addListener((o, old, neww) ->{
            if(neww.isBlank()) property_listView.setItems(getPropertyList(new HashSet<>()));
        });

        selectedProperty.addListener((observable, oldValue, newValue) -> {
            showPropertyDetail(newValue);
        });
        manageProperty_btn.setOnAction(e -> requestManageProperty(selectedProperty.get()));

        property_listView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Property property, boolean empty) {
                super.updateItem(property, empty);
                if (empty || property == null) {
//                    manageProperty_btn.setText("Select a property");
                    UIDecorator.setNormalButton(manageProperty_btn, UIDecorator.MANAGE, "Select a property");
                    setText(null);
                    setOnMouseClicked(null); // Remove click handler for empty cells
                } else {
                    setText(property.getAddress()); // Display the address or another property field
                    setOnMouseClicked(event -> {
                        selectedProperty.set(property);
                        if (event.getClickCount() == 1) { // Single click
                            showPropertyDetail(property);
                        }
                    });
                }
            }
        });
    }


    private void searchProperty() {
        if(search_input.getText().isBlank()) return;
        Set<Property> res = new HashSet<>();
        CommercialPropertyDAO commercialPropertyDAO = new CommercialPropertyDAO();
        List<CommercialProperty> ans = (List<CommercialProperty>)commercialPropertyDAO.search(search_input.getText(), EntityGraphUtils::SimpleCommercialProperty);
        res.addAll(ans);
        ResidentialPropertyDAO residentialPropertyDAO = new ResidentialPropertyDAO();
        List<ResidentialProperty> ans2 = residentialPropertyDAO.search(search_input.getText(), EntityGraphUtils::SimpleResidentialProperty);
        res.addAll(ans2);
        property_listView.setItems(getPropertyList(res));
    }

    public ObservableList<Property> getPropertyList(Set<Property> pList){
        ObservableList<Property> list = FXCollections.observableArrayList();
        list.addAll(pList);
        return list;
    }


    private void requestManageProperty(Property property){
        if(!ModelCentral.getInstance().getStartViewFactory().confirmMessage("You must register first")) return;
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
        clearDataProperty();
        int id = Integer.parseInt(property.getId()+"");

        if(propertyMap.containsKey(id)){
            property = propertyMap.get(id);

        }else{

            if(property.getType().toString().equals("COMMERCIAL")){
                CommercialPropertyDAO commercialPropertyDAO = new CommercialPropertyDAO();
                property = commercialPropertyDAO.get(id, EntityGraphUtils::SimpleCommercialProperty);
            }
            else{
                ResidentialPropertyDAO residentialPropertyDAO = new ResidentialPropertyDAO();
                property = (ResidentialProperty)residentialPropertyDAO.get(id, EntityGraphUtils::SimpleResidentialProperty);
            }
        }

        System.out.println("showPropertyDetail: "+property);
        propertyMap.put(id, property);
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
