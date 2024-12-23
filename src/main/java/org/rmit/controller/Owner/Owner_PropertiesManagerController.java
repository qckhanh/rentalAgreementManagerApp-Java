package org.rmit.controller.Owner;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Person;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Property.PropertyStatus;
import org.rmit.model.Property.PropertyType;
import org.rmit.model.Session;
import org.rmit.model.Property.Property;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class Owner_PropertiesManagerController implements Initializable {

    public ComboBox propertyTypeFilter_comboBox;
    public ComboBox propertyStatusFilter_comboBox;
    public TableView properties_tableView;
    public ObjectProperty<Person> currentUser = Session.getInstance().currentUserProperty();

//    public ObjectProperty<Property> property = new SimpleObjectProperty<>();
    public ObjectProperty<PropertyStatus> selectedPropertyStatus = new SimpleObjectProperty<>();
    public ObjectProperty<PropertyType> selectedPropertyType = new SimpleObjectProperty<>();
    public Label welcomeLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
                createColumn("ID", "id"),
                createColumn("Status", "status"),
                createColumn("Type", "type")
        );
        loadData(((Owner)currentUser.get()).getPropertiesOwned());

        selectedPropertyType.addListener((observableValue, oldValue, newValue) -> {
            Set<Property> filteredList = noFilter();
            filteredList = filteredByPropertyType(filteredList, newValue);
            filteredList = filteredByPropertyStatus(filteredList, selectedPropertyStatus.get());
            loadData(filteredList);
            System.out.println("filtered by ptype");
        });

        selectedPropertyStatus.addListener((observableValue, oldValue, newValue) -> {
            Set<Property> filteredList = noFilter();
            filteredList = filteredByPropertyStatus(filteredList, newValue);
            filteredList = filteredByPropertyType(filteredList, selectedPropertyType.get());
            loadData(filteredList);
            System.out.println("filtered by pstatus");
        });



    }

    private Set<Property> noFilter() {
        return ((Owner) currentUser.get()).getPropertiesOwned();
    }

    private void loadData(Set<Property> propertySet) {
        ObservableList<Property> propertiesTableView = FXCollections.observableArrayList();
        propertiesTableView.addAll(propertySet);
        properties_tableView.setItems(propertiesTableView);
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
