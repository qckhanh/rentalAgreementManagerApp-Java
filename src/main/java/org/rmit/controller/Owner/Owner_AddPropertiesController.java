package org.rmit.controller.Owner;

import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.rmit.database.CommercialPropertyDAO;
import org.rmit.database.ResidentialPropertyDAO;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Property.*;
import org.rmit.model.Session;
import org.rmit.view.Owner.OWNER_MENU_OPTION;

import java.net.URL;
import java.util.ResourceBundle;

public class Owner_AddPropertiesController implements Initializable {
    public Label addProperty_label;
    public Label selectProperty_label;
    public ChoiceBox<PropertyType> typeOfProperty_choiceBox;
    public TextField propertyAddress_txtf;
    public TextField propertyPrice_txtf;
    public ComboBox<PropertyStatus> propertyStatus_cbox;
    public TextField propertyBtype_txtf;
    public TextField propertySquareMeters_txtf;
    public TextField propertyPSpaces_txtf;
    public CheckBox propertyGarden_chbox;
    public CheckBox propertyPet_chBox;
    public TextField propertyBedrooms_txtf;
    public TextField propertyRooms_txtf;
    public Button addProperty_btn;
    public Button returnTableView_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeOfProperty_choiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == PropertyType.COMMERCIAL) {
                clearData();
                enableCommercialFields();
                disableResidentialFields();
            }
            else if (newValue == PropertyType.RESIDENTIAL) {
                clearData();
                enableResidentialFields();
                disableCommercialFields();
            }
            returnTableView_btn.setOnAction(e -> returnTableView());
            addProperty_btn.setOnAction(e -> addProperty());
        });
        typeOfProperty_choiceBox.setValue(PropertyType.COMMERCIAL);
        typeOfProperty_choiceBox.setItems(FXCollections.observableArrayList(
                PropertyType.COMMERCIAL,
                PropertyType.RESIDENTIAL
        ));
        propertyStatus_cbox.setValue(PropertyStatus.AVAILABLE);
        propertyStatus_cbox.setItems(FXCollections.observableArrayList(
                PropertyStatus.AVAILABLE,
                PropertyStatus.UNAVAILABLE,
                PropertyStatus.UNDER_MAINTENANCE,
                PropertyStatus.RENTED
        ));

    }

    private void enableCommercialFields() {
        propertyBtype_txtf.setDisable(false);
        propertySquareMeters_txtf.setDisable(false);
        propertyPSpaces_txtf.setDisable(false);
    }

    private void disableCommercialFields() {
        propertyBtype_txtf.setDisable(true);
        propertySquareMeters_txtf.setDisable(true);
        propertyPSpaces_txtf.setDisable(true);
    }

    private void enableResidentialFields() {
        propertyGarden_chbox.setDisable(false);
        propertyPet_chBox.setDisable(false);
        propertyBedrooms_txtf.setDisable(false);
        propertyRooms_txtf.setDisable(false);
    }

    private void disableResidentialFields() {
        propertyGarden_chbox.setDisable(true);
        propertyPet_chBox.setDisable(true);
        propertyBedrooms_txtf.setDisable(true);
        propertyRooms_txtf.setDisable(true);
    }

    private void clearData() {
        propertyAddress_txtf.clear();
        propertyPrice_txtf.clear();
        propertyBtype_txtf.clear();
        propertySquareMeters_txtf.clear();
        propertyPSpaces_txtf.clear();
        propertyGarden_chbox.setSelected(false);
        propertyPet_chBox.setSelected(false);
        propertyBedrooms_txtf.clear();
        propertyRooms_txtf.clear();
    }

    private void returnTableView() {
        ModelCentral.getInstance().getOwnerViewFactory().setOwnerSelectedMenuItem(OWNER_MENU_OPTION.PROPERTIES_MANAGER);
    }

    private void addProperty() {
        if (typeOfProperty_choiceBox.getValue() == PropertyType.COMMERCIAL) {
            CommercialPropertyDAO cpDAO = new CommercialPropertyDAO();
            CommercialProperty cp = new CommercialProperty();

            CommercialPropertyFactory(cp);
            if (cpDAO.add(cp)) System.out.println("added");
            else System.out.println("Nope!");
        }
        else if (typeOfProperty_choiceBox.getValue() == PropertyType.RESIDENTIAL) {
            ResidentialPropertyDAO rpDAO = new ResidentialPropertyDAO();
            ResidentialProperty rp = new ResidentialProperty();

            ResidentialPropertyFactory(rp);
            if (rpDAO.add(rp)) System.out.println("added");
            else System.out.println("Nope!!");
        }
    }

    private void CommercialPropertyFactory(CommercialProperty cp) {
        cp.setOwner((Owner) Session.getInstance().getCurrentUser());
        cp.setType(PropertyType.COMMERCIAL);
        cp.setAddress(propertyAddress_txtf.getText());
        cp.setPrice(Double.parseDouble(propertyPrice_txtf.getText()));
        cp.setStatus(propertyStatus_cbox.getValue());
        cp.setBusinessType(propertyBtype_txtf.getText());
        cp.setSquareMeters(Double.parseDouble(propertySquareMeters_txtf.getText()));
        cp.setTotalParkingSpace(Integer.parseInt(propertyPSpaces_txtf.getText()));
    }

    private void ResidentialPropertyFactory(ResidentialProperty rp) {
        rp.setOwner((Owner) Session.getInstance().getCurrentUser());
        rp.setType(PropertyType.RESIDENTIAL);
        rp.setAddress(propertyAddress_txtf.getText());
        rp.setPrice(Double.parseDouble(propertyPrice_txtf.getText()));
        rp.setStatus(propertyStatus_cbox.getValue());
        rp.setHasGarden(propertyGarden_chbox.isSelected());
        rp.setPetAllowed(propertyPet_chBox.isSelected());
        rp.setTotalBedroom(Integer.parseInt(propertyBedrooms_txtf.getText()));
        rp.setTotalRoom(Integer.parseInt(propertyRooms_txtf.getText()));
    }
}
