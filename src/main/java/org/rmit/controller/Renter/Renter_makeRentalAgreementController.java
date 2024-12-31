package org.rmit.controller.Renter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import org.rmit.database.CommercialPropertyDAO;
import org.rmit.database.ResidentialPropertyDAO;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.Property;
import org.rmit.model.Property.RentalPeriod;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Renter_makeRentalAgreementController implements Initializable {
    public TextField propertySearch_input;
    public Button searchProperty_btn;
    public ComboBox<Property> property_ComboBox;
    public TextField owner_input;
    public ComboBox<Host> host_comboBox;
    public ComboBox<RentalPeriod> rentalPeriod_comboBox;
    public TextField subRenterSearch_input;
    public Button searchRenter_btn;
    public ComboBox<Renter> subRenter_comboBox;
    public Button submit_btn;

    public ObjectProperty<Property> selectedProperty = new SimpleObjectProperty<>();
    public ObjectProperty<Owner> selectedOwner = new SimpleObjectProperty<>();
    public ObjectProperty<Host> selectedHost = new SimpleObjectProperty<>();
    public ObjectProperty<RentalPeriod> selectedRentalPeriod = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        submit_btn.setOnAction(e -> submitRA());
        searchProperty_btn.setOnAction(e -> searchProperty());
        property_ComboBox.setOnAction(e -> {
            selectedProperty.set(property_ComboBox.getSelectionModel().getSelectedItem());
            selectedOwner.set(property_ComboBox.getSelectionModel().getSelectedItem().getOwner());
        });
        host_comboBox.setOnAction(e -> {
            selectedHost.set(host_comboBox.getSelectionModel().getSelectedItem());
        });
        selectedOwner.addListener((observable, oldValue, newValue) -> {
            owner_input.setText(newValue.getName());
        });
        selectedProperty.addListener((observable, oldValue, newValue) -> {
            host_comboBox.getItems().clear();
            host_comboBox.getItems().addAll(newValue.getHosts());
        });
        customCellFactory();
        rentalPeriod_comboBox.getItems().addAll(
                RentalPeriod.WEEKLY,
                RentalPeriod.FORTNIGHTLY,
                RentalPeriod.FORTNIGHTLY
        );
        rentalPeriod_comboBox.setOnAction(e -> {
            selectedRentalPeriod.set(rentalPeriod_comboBox.getSelectionModel().getSelectedItem());
        });
        owner_input.setEditable(false);


    }

    private void customCellFactory(){
        property_ComboBox.setCellFactory(p -> new ListCell<>(){
            @Override
            protected void updateItem(Property item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getId() + " | " + item.getAddress());
                }
            }

        });
        property_ComboBox.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(Property item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getId() + " | " + item.getAddress());
                }
            }
        });

        host_comboBox.setCellFactory(p -> new ListCell<>(){
            @Override
            protected void updateItem(Host item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }

        });
        host_comboBox.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(Host item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

    }

    private List<Property> loadProperty() {
        return null;
    }

    private void searchProperty() {
        if(propertySearch_input.getText().isBlank()) return;
        property_ComboBox.getItems().clear();
        List<Property> list = new ArrayList<>();
        CommercialPropertyDAO commercialPropertyDAO = new CommercialPropertyDAO();
        list.addAll(commercialPropertyDAO.search(propertySearch_input.getText()));
        ResidentialPropertyDAO residentialPropertyDAO = new ResidentialPropertyDAO();
        list.addAll(residentialPropertyDAO.search(propertySearch_input.getText()));
        property_ComboBox.getItems().addAll(list);
    }

    private void submitRA() {
    }
}
