package org.rmit.controller.Owner;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.database.CommercialPropertyDAO;
import org.rmit.database.OwnerDAO;
import org.rmit.database.ResidentialPropertyDAO;
import org.rmit.view.ViewCentral;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Person;
import org.rmit.model.Property.*;
import org.rmit.model.Session;
import org.rmit.view.Owner.OWNER_MENU_OPTION;
import org.rmit.view.Start.NOTIFICATION_TYPE;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;

public class Owner_PropertiesManagerController implements Initializable {
    public ComboBox propertyTypeFilter_comboBox;
    public ComboBox propertyStatusFilter_comboBox;
    public TableView<Property> properties_tableView;
    public Button refresh;
    public AnchorPane anchorPane;
    private ObservableList<Property> propertiesObservableList = FXCollections.observableArrayList();
    public ObjectProperty<Person> currentUser = Session.getInstance().currentUserProperty();

    //    public ObjectProperty<Property> property = new SimpleObjectProperty<>();
    public ObjectProperty<PropertyStatus> selectedPropertyStatus = new SimpleObjectProperty<>();
    public ObjectProperty<PropertyType> selectedPropertyType = new SimpleObjectProperty<>();
    public Label welcomeLabel;
    public Button addPropertyButton;
    public Button updatePropertyButton;
    public Button deletePropertyButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        decor();
        propertyTypeFilter_comboBox.setPromptText("Property type");
        propertyStatusFilter_comboBox.setPromptText("Property status");

        selectedPropertyStatus.set(PropertyStatus.NONE);
        propertyStatusFilter_comboBox.setOnAction(e -> {
            selectedPropertyStatus.set((PropertyStatus) propertyStatusFilter_comboBox.getValue());
        });
        propertyStatusFilter_comboBox.getItems().addAll(
                PropertyStatus.AVAILABLE,
                PropertyStatus.RENTED,
                PropertyStatus.UNAVAILABLE,
                PropertyStatus.UNDER_MAINTENANCE,
                PropertyStatus.NONE
        );

        selectedPropertyType.set(PropertyType.NONE);
        propertyTypeFilter_comboBox.setOnAction(e-> {
            selectedPropertyType.set((PropertyType) propertyTypeFilter_comboBox.getValue());
        });
        propertyTypeFilter_comboBox.getItems().addAll(
                PropertyType.COMMERCIAL,
                PropertyType.RESIDENTIAL,
                PropertyType.NONE
        );

        properties_tableView.getColumns().addAll(
                createColumn("Property ID", "id"),
                createColumn("Status", "status"),
                createColumn("Type", "type"),
                createColumn("Price", "price"),
                createColumn("Address", "address"),
                createColumn("Total Rooms", property -> {
                    if (property instanceof ResidentialProperty) {
                        return ((ResidentialProperty) property).getTotalRoom();
                    }
                    return null;
                }),
                createColumn("Total Bedrooms", property -> {
                    if (property instanceof ResidentialProperty) {
                        return ((ResidentialProperty) property).getTotalBedroom();
                    }
                    return null;
                }),
                createColumn("Pet Allowed", property -> {
                    if (property instanceof ResidentialProperty) {
                        return ((ResidentialProperty) property).isPetAllowed();
                    }
                    return null;
                }),
                createColumn("Has Garden", property -> {
                    if (property instanceof ResidentialProperty) {
                        return ((ResidentialProperty) property).isHasGarden();
                    }
                    return null;
                }),
                createColumn("Total Parking Space", property -> {
                    if (property instanceof CommercialProperty) {
                        return ((CommercialProperty) property).getTotalParkingSpace();
                    }
                    return null;
                }),
                createColumn("Square Meters", property -> {
                    if (property instanceof CommercialProperty) {
                        return ((CommercialProperty) property).getSquareMeters();
                    }
                    return null;
                }),
                createColumn("Business Type", property -> {
                    if (property instanceof CommercialProperty) {
                        return ((CommercialProperty) property).getBusinessTypeProperty();
                    }
                    return null;
                })
        );
        properties_tableView.setItems(propertiesObservableList);
        loadData(((Owner)currentUser.get()).getPropertiesOwned());

        selectedPropertyType.addListener((observableValue, oldValue, newValue) -> {
            Set<Property> filteredList = noFilter();
            filteredList = filteredByPropertyType(filteredList, newValue);
            filteredList = filteredByPropertyStatus(filteredList, selectedPropertyStatus.get());
            loadData(filteredList);
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Filter: Property type");
        });

        selectedPropertyStatus.addListener((observableValue, oldValue, newValue) -> {
            Set<Property> filteredList = noFilter();
            filteredList = filteredByPropertyStatus(filteredList, newValue);
            filteredList = filteredByPropertyType(filteredList, selectedPropertyType.get());
            loadData(filteredList);
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Filter: Property Status");

        });

        properties_tableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            deletePropertyButton.setDisable(newValue == null);
            updatePropertyButton.setDisable(newValue == null);

        });

        deletePropertyButton.setOnAction(e-> {
            deleteProperty();
            properties_tableView.refresh();
        });

        addPropertyButton.setOnAction(e -> {
            addProperty();
            properties_tableView.refresh();
        });

        updatePropertyButton.setOnAction(e-> {
            updateProperty();
            refreshData();
            properties_tableView.refresh();
        });

        refresh.setOnAction(e -> {
            refreshData();
        });

    }

    private void refreshData() {
        OwnerDAO ownerDAO = new OwnerDAO();
        int id = Integer.parseInt(currentUser.get().getId()+"");
        Owner owner = ownerDAO.get(id, EntityGraphUtils::SimpleOwnerFull);
        Set<Property> updatedProperties = owner.getPropertiesOwned();
        properties_tableView.setItems(FXCollections.observableArrayList(updatedProperties));
        loadData(updatedProperties);
        properties_tableView.refresh();
        ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Data refreshed");
    }

    private void decor(){
        UIDecorator.setSuccessButton(addPropertyButton, UIDecorator.ADD(), null);
        UIDecorator.setNormalButton(updatePropertyButton, new FontIcon(Feather.EDIT), null);
        UIDecorator.setDangerButton(deletePropertyButton, UIDecorator.DELETE(), null);
        UIDecorator.setNormalButton(refresh, UIDecorator.REFRESH(), null);
    }

    private void deleteProperty(){
        Property selectedProperty = (Property) properties_tableView.getSelectionModel().getSelectedItem();
        if(selectedProperty == null){
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Please select a property to delete");
            return;
        }
        if(selectedProperty.getHosts().size() != 0 || selectedProperty.getAgreementList().size() != 0){
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Cannot delete property have rental agreements or hosts");
            return;
        }

        if(!ViewCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to save changes?")) return;
        boolean success = false;
        int id = Integer.parseInt(selectedProperty.getId()+"");

        if (selectedProperty instanceof ResidentialProperty) {
            ResidentialPropertyDAO rpDAO = new ResidentialPropertyDAO();
            ResidentialProperty rp = rpDAO.get(id, EntityGraphUtils::SimpleResidentialProperty);
            if (rpDAO.delete(rp)) success = true;
        } else if (selectedProperty instanceof CommercialProperty) {
            CommercialPropertyDAO cpDAO = new CommercialPropertyDAO();
            CommercialProperty cp = cpDAO.get(id, EntityGraphUtils::SimpleCommercialProperty);
            if (cpDAO.delete(cp)) success = true;
        }

        if (success) {
            properties_tableView.getItems().remove(selectedProperty);
            properties_tableView.refresh();
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Property deleted successfully");
        }
        else{
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Failed to delete property. Try again later");
        }
    }

    private void addProperty() {
        ViewCentral.getInstance().getOwnerViewFactory().setOwnerSelectedMenuItem(OWNER_MENU_OPTION.ADD_PROPERTY);
        loadData(((Owner) currentUser.get()).getPropertiesOwned());
        properties_tableView.refresh();
    }

    private void updateProperty() {
        Property selectedProperty = (Property) properties_tableView.getSelectionModel().getSelectedItem();
        if(selectedProperty == null){
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Please select a property to update");
            return;
        }
        Owner_UpdatePropertiesController.setSelectedProperty(selectedProperty);
        ViewCentral.getInstance().getOwnerViewFactory().setOwnerSelectedMenuItem(OWNER_MENU_OPTION.UPDATE_PROPERTY);
        Set<Property> updatedProperties = ((Owner) currentUser.get()).getPropertiesOwned();
//        properties_tableView.setItems(FXCollections.observableArrayList(updatedProperties));
        loadData(updatedProperties);

        properties_tableView.refresh();
    }

    private Set<Property> noFilter() {
        return ((Owner) currentUser.get()).getPropertiesOwned();
    }

    private void loadData(Set<Property> propertySet) {
        propertiesObservableList.setAll(propertySet);
    }

    private <T> TableColumn<Property, String> createColumn(String columnName, Function<Property, T> propertyExtractor) {
        TableColumn<Property, String> column = new TableColumn<>(columnName);
        column.setCellValueFactory(cellData -> {
            T value = propertyExtractor.apply(cellData.getValue());
            return new SimpleStringProperty(value != null ? value.toString() : "N/A");
        });
        return column;
    }

    private TableColumn<Property, ?> createColumn(String columnName, String propertyName) {
        TableColumn<Property, ?> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    private Set<Property> filteredByPropertyType(Set<Property> propertySet, PropertyType type) {
        if (type.equals(PropertyType.NONE)) return propertySet;
        Set<Property> filteredSet = new HashSet<>();
        for (Property p : propertySet) {
            if (p.getType() != null) {
                if (p.getType().equals(type)) {
                    filteredSet.add(p);
                }
            }
        }
        return filteredSet;
    }

    private Set<Property> filteredByPropertyStatus(Set<Property> propertySet, PropertyStatus status) {
        if (status.equals(PropertyStatus.NONE)) return propertySet;
        Set<Property> filteredSet = new HashSet<>();
        for (Property p : propertySet) {
            if (p.getStatus().equals(status)) {
                filteredSet.add(p);
            }
        }
        return filteredSet;
    }
}


