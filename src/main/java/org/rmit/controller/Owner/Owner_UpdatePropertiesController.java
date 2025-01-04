package org.rmit.controller.Owner;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.database.CommercialPropertyDAO;
import org.rmit.database.ResidentialPropertyDAO;
import org.rmit.model.ModelCentral;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Property.Property;
import org.rmit.model.Property.PropertyStatus;
import org.rmit.model.Property.ResidentialProperty;
import org.rmit.view.Owner.OWNER_MENU_OPTION;

import java.net.URL;
import java.util.ResourceBundle;

import static org.rmit.Helper.EntityGraphUtils.SimpleCommercialProperty;
import static org.rmit.Helper.EntityGraphUtils.SimpleResidentialProperty;

public class Owner_UpdatePropertiesController implements Initializable {


    private static final ObjectProperty<Property> selectedProperty = new SimpleObjectProperty<>();
    public Button returnTableView_btn;
    public Label updateProperty_label;
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
    public Button updateProperty_btn;
    public Button btn;


    public Owner_UpdatePropertiesController() {
    }

    //Illegal attempt to associate a collection with two open sessions: FIXED
    public static void setSelectedProperty(Property property) {
        int id = Integer.parseInt(property.getId() + "");
        if(property instanceof CommercialProperty) {
            CommercialPropertyDAO cpDAO = new CommercialPropertyDAO();
            property = cpDAO.get(id, EntityGraphUtils::SimpleCommercialProperty);
        }
        else if(property instanceof ResidentialProperty) {
            ResidentialPropertyDAO rpDAO = new ResidentialPropertyDAO();
            property = rpDAO.get(id, EntityGraphUtils::SimpleResidentialProperty);

        }
        selectedProperty.set(property);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn.setOnAction(e-> {
            System.out.println(selectedProperty);
        });
        returnTableView_btn.setOnAction(e-> {
            ModelCentral.getInstance().getOwnerViewFactory().setOwnerSelectedMenuItem(OWNER_MENU_OPTION.PROPERTIES_MANAGER);
        });
        addSelectedPropertyChangeListener();
        propertyAddress_txtf.textProperty().addListener((observableValue, s, t1) -> checkChanges());
        propertyPrice_txtf.textProperty().addListener((observableValue, s, t1) -> checkChanges());
        propertyStatus_cbox.valueProperty().addListener(((observableValue, propertyStatus, t1) -> checkChanges()));
        if (selectedProperty.get() instanceof CommercialProperty) {
            propertyBtype_txtf.textProperty().addListener((observableValue, s, t1) -> checkChanges());
            propertySquareMeters_txtf.textProperty().addListener((observableValue, s, t1) -> checkChanges());
            propertyPSpaces_txtf.textProperty().addListener((observableValue, s, t1) -> checkChanges());
        }
        else if (selectedProperty.get() instanceof ResidentialProperty) {
            propertyGarden_chbox.selectedProperty().addListener((observableValue, aBoolean, t1) -> checkChanges());
            propertyPet_chBox.selectedProperty().addListener((observableValue, aBoolean, t1) -> checkChanges());
            propertyBedrooms_txtf.textProperty().addListener((observableValue, s, t1) -> checkChanges());
            propertyRooms_txtf.textProperty().addListener((observableValue, s, t1) -> checkChanges());
        }
        setDisable(true);
        updateProperty_btn.setText("Edit");
        updateProperty_btn.setDisable(false);
        updateProperty_btn.setOnAction(e-> updateProperty());
    }

    private void updateProperty() {
        if (updateProperty_btn.getText().equals("Save")) {
            boolean confirmed = ModelCentral.getInstance().getStartViewFactory().confirmMessage("Save changes?");
            if (confirmed) {
                saveChanges();
            }
        }
        else {
            setDisable(false);
        }
    }

    private void saveChanges() {
        selectedProperty.get().setAddress(propertyAddress_txtf.getText());
        selectedProperty.get().setPrice(Double.parseDouble(propertyPrice_txtf.getText()));
        selectedProperty.get().setStatus(propertyStatus_cbox.getValue());
        if (selectedProperty.get() instanceof CommercialProperty) {
            ((CommercialProperty) selectedProperty.get()).setBusinessType(propertyBtype_txtf.getText());
            ((CommercialProperty) selectedProperty.get()).setSquareMeters(Double.parseDouble(propertySquareMeters_txtf.getText()));
            ((CommercialProperty) selectedProperty.get()).setTotalParkingSpace(Integer.parseInt(propertyPSpaces_txtf.getText()));
        }
        else if (selectedProperty.get() instanceof ResidentialProperty) {
            ((ResidentialProperty) selectedProperty.get()).setHasGarden(propertyGarden_chbox.isSelected());
            ((ResidentialProperty) selectedProperty.get()).setPetAllowed(propertyPet_chBox.isSelected());
            ((ResidentialProperty) selectedProperty.get()).setTotalBedroom(Integer.parseInt(propertyBedrooms_txtf.getText()));
            ((ResidentialProperty) selectedProperty.get()).setTotalRoom(Integer.parseInt(propertyRooms_txtf.getText()));
        }
        System.out.println("Error here");
        if (selectedProperty.get() instanceof CommercialProperty) {
            CommercialPropertyDAO cpDAO = new CommercialPropertyDAO();
            cpDAO.update((CommercialProperty) selectedProperty.get());
        }
        else if (selectedProperty.get() instanceof ResidentialProperty) {
            ResidentialPropertyDAO rpDAO = new ResidentialPropertyDAO();
            rpDAO.update((ResidentialProperty) selectedProperty.get());
        }
        setDisable(true);
        updateProperty_btn.setText("Edit");
        updateProperty_btn.setDisable(false);
    }


    public void addSelectedPropertyChangeListener() {
        selectedProperty.addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                System.out.println("Selected property has changed.");
                clearData();
                setDisable(true);
                updateFormFields(newValue);
            }
        });
        // Update form fields initially if selectedProperty is not null
        if (selectedProperty.get() != null) {
            clearData();
            updateFormFields(selectedProperty.get());
        }
    }

    private void checkChanges() {
        boolean changed = false;
        if (!propertyAddress_txtf.getText().equals(selectedProperty.get().getAddress())) {
            changed = true;
        }
        if (!propertyPrice_txtf.getText().equals(String.valueOf(selectedProperty.get().getPrice()))) {
            changed = true;
        }
        if (!propertyStatus_cbox.getValue().equals(selectedProperty.get().getStatus())) {
            changed = true;
        }
        if (selectedProperty.get() instanceof CommercialProperty) {
            if (!propertyBtype_txtf.getText().equals(((CommercialProperty) selectedProperty.get()).getBusinessType())) {
                changed = true;
            }
            if (!propertySquareMeters_txtf.getText().equals((String.valueOf(((CommercialProperty) selectedProperty.get()).getSquareMeters())))) {
                changed = true;
            }
            if (!propertyPSpaces_txtf.getText().equals(String.valueOf(((CommercialProperty) selectedProperty.get()).getTotalParkingSpace()))) {
                changed = true;
            }
        }
        if (selectedProperty.get() instanceof ResidentialProperty) {
            if (!propertyGarden_chbox.isSelected() == ((ResidentialProperty) selectedProperty.get()).isHasGardenProperty()) {
                changed = true;
            }
            if (!propertyPet_chBox.isSelected() == ((ResidentialProperty) selectedProperty.get()).isIsPetAllowedProperty()) {
                changed = true;
            }
            if (!propertyBedrooms_txtf.getText().equals(String.valueOf(((ResidentialProperty) selectedProperty.get()).getTotalBedroom()))) {
                changed = true;
            }
            if (!propertyRooms_txtf.getText().equals(String.valueOf(((ResidentialProperty) selectedProperty.get()).getTotalRoom()))) {
                changed = true;
            }
        }

        if (changed) {
            updateProperty_btn.setText("Save");
            updateProperty_btn.setDisable(false);
        }
        else {
            updateProperty_btn.setText("Edit");
            setDisable(true);
            updateProperty_btn.setDisable(false);
        }
    }

    void setDisable(boolean status) {
        if (status == true) {
            propertyAddress_txtf.setDisable(true);
            propertyPrice_txtf.setDisable(true);
            propertyStatus_cbox.setDisable(true);
            disableCommercialFields();
            disableResidentialFields();
        }
        else {
            if (selectedProperty.get() instanceof CommercialProperty) {
                propertyAddress_txtf.setDisable(false);
                propertyPrice_txtf.setDisable(false);
                propertyStatus_cbox.setDisable(false);
                enableCommercialFields();
            }
            else if (selectedProperty.get() instanceof ResidentialProperty) {
                propertyAddress_txtf.setDisable(false);
                propertyPrice_txtf.setDisable(false);
                propertyStatus_cbox.setDisable(false);
                enableResidentialFields();
            }
        }
    }

    private void updateFormFields(Property property) {
        propertyAddress_txtf.setText(property.getAddress());
        propertyPrice_txtf.setText(String.valueOf(property.getPrice()));
        propertyStatus_cbox.setValue(property.getStatus());
        if (property instanceof CommercialProperty) {
            propertyBtype_txtf.setText(((CommercialProperty) property).getBusinessType());
            propertySquareMeters_txtf.setText(String.valueOf(((CommercialProperty) property).getSquareMeters()));
            propertyPSpaces_txtf.setText(String.valueOf(((CommercialProperty) property).getTotalParkingSpace()));
        }
        else if (property instanceof ResidentialProperty) {
            propertyGarden_chbox.setSelected(((ResidentialProperty) property).isHasGardenProperty());
            propertyPet_chBox.setSelected(((ResidentialProperty) property).isIsPetAllowedProperty());
            propertyBedrooms_txtf.setText(String.valueOf(((ResidentialProperty) property).getTotalBedroom()));
            propertyRooms_txtf.setText(String.valueOf(((ResidentialProperty) property).getTotalBedroom()));
        }

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
}
