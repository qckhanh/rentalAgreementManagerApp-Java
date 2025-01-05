package org.rmit.controller.Owner;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import net.synedra.validatorfx.Validator;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.Helper.InputValidator;
import org.rmit.database.CommercialPropertyDAO;
import org.rmit.database.ResidentialPropertyDAO;
import org.rmit.model.ModelCentral;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Property.Property;
import org.rmit.model.Property.PropertyStatus;
import org.rmit.model.Property.ResidentialProperty;
import org.rmit.view.Owner.OWNER_MENU_OPTION;

import java.net.URL;
import java.util.Objects;
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
    public Label address_err;
    public Label price_err;
    public Label status_err;
    public Label businessType_err;
    public Label squareMeters_err;
    public Label parkingSpace_err;
    public Label bedroom_err;
    public Label room_err;
    Validator validator = new Validator();

    private int totalNumberBedrooms = 0;
    private int totalNumberRooms = 0;


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
            // Add listeners to update total number of bedrooms and rooms
            propertyBedrooms_txtf.textProperty().addListener((observable, oldValue, newValue) -> updateTotalNumbers());
            propertyRooms_txtf.textProperty().addListener((observable, oldValue, newValue) -> updateTotalNumbers());
        }
        setDisable(true);
        updateProperty_btn.setText("Edit");
        updateProperty_btn.setDisable(false);
        updateProperty_btn.setOnAction(e-> updateProperty());


        addListener();
        resetErrorLabels();
        validateInput();
    }

    private void validateInput() {
        validator.createCheck()
                .dependsOn("propertyAddress", propertyAddress_txtf.textProperty())
                .withMethod(context -> {
                    String input = context.get("propertyAddress");
                    if (!InputValidator.NoCondition(input, address_err)) {
                        context.error("Address must not be empty");
                    }
                })
                .decorates(propertyAddress_txtf)
                .immediateClear();

        validator.createCheck()
                .dependsOn("propertyPrice", propertyPrice_txtf.textProperty())
                .withMethod(context -> {
                    String input = context.get("propertyPrice");
                    if (!InputValidator.isValidPrice(input, price_err)) {
                        context.error("Price must be a valid number");
                    }
                })
                .decorates(propertyPrice_txtf)
                .immediateClear();

        validator.createCheck()
                .dependsOn("propertyStatus", propertyStatus_cbox.valueProperty())
                .withMethod(context -> {
                    PropertyStatus input = context.get("propertyStatus");
                    if (input == null) {
                        context.error("A Status must be selected");
                        status_err.setText("A Status must be selected");
                    }
                })
                .decorates(propertyStatus_cbox)
                .immediateClear();

        if (selectedProperty.get() instanceof CommercialProperty) {
            validator.createCheck()
                    .dependsOn("propertyBtype", propertyBtype_txtf.textProperty())
                    .withMethod(context -> {
                        String input = context.get("propertyBtype");
                        if (!InputValidator.NoCondition(input, businessType_err)) {
                            context.error("BusinessType must not be empty");
                        }
                    })
                    .decorates(propertyBtype_txtf)
                    .immediateClear();

            validator.createCheck()
                    .dependsOn("propertySquareMeters", propertySquareMeters_txtf.textProperty())
                    .withMethod(context -> {
                        String input = context.get("propertySquareMeters");
                        if (!InputValidator.isValidSquareMeters(input, squareMeters_err)) {
                            context.error("Square meters must be a valid number");
                        }
                    })
                    .decorates(propertySquareMeters_txtf)
                    .immediateClear();

            validator.createCheck()
                    .dependsOn("propertyPSpaces", propertyPSpaces_txtf.textProperty())
                    .withMethod(context -> {
                        String input = context.get("propertyPSpaces");
                        if (!InputValidator.isValidParkingSpaces(input, parkingSpace_err)) {
                            context.error("Parking spaces must be a valid number");
                        }
                    })
                    .decorates(propertyPSpaces_txtf)
                    .immediateClear();
        } else if (selectedProperty.get() instanceof ResidentialProperty) {
            validator.createCheck()
                    .dependsOn("propertyBedrooms", propertyBedrooms_txtf.textProperty())
                    .withMethod(context -> {
                        String input = context.get("propertyBedrooms");
                        if (!InputValidator.isValidBedrooms(input, bedroom_err, totalNumberRooms)) {
                            context.error("Bedrooms must be a valid number");
                        }
                    })
                    .decorates(propertyBedrooms_txtf)
                    .immediateClear();

            validator.createCheck()
                    .dependsOn("propertyRooms", propertyRooms_txtf.textProperty())
                    .withMethod(context -> {
                        String input = context.get("propertyRooms");
                        if (!InputValidator.isValidRooms(input, room_err, totalNumberBedrooms)) {
                            context.error("Rooms must be a valid number");
                        }
                    })
                    .decorates(propertyRooms_txtf)
                    .immediateClear();
        }

        updateProperty_btn.disableProperty().bind(validator.containsErrorsProperty());
    }

    private void addListener() {
        propertyAddress_txtf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) address_err.setText("");
            checkChanges();
        });
        propertyPrice_txtf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) price_err.setText("");
            checkChanges();
        });
        propertyStatus_cbox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!Objects.equals(oldValue, newValue)) status_err.setText("");
            checkChanges();
        });
        if (selectedProperty.get() instanceof CommercialProperty) {
            propertyBtype_txtf.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.equals(oldValue)) businessType_err.setText("");
                checkChanges();
            });
            propertySquareMeters_txtf.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.equals(oldValue)) squareMeters_err.setText("");
                checkChanges();
            });
            propertyPSpaces_txtf.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.equals(oldValue)) parkingSpace_err.setText("");
                checkChanges();
            });
        } else if (selectedProperty.get() instanceof ResidentialProperty) {
            propertyGarden_chbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (!Objects.equals(oldValue, newValue)) checkChanges();
            });
            propertyPet_chBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (!Objects.equals(oldValue, newValue)) checkChanges();
            });
            propertyBedrooms_txtf.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.equals(oldValue)) bedroom_err.setText("");
                checkChanges();
            });
            propertyRooms_txtf.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.equals(oldValue)) room_err.setText("");
                checkChanges();
            });
        }
    }

    private void updateProperty() {
        if (updateProperty_btn.getText().equals("Save")) {
            if (validator.validate()) {
                boolean confirmed = ModelCentral.getInstance().getStartViewFactory().confirmMessage("Save changes?");
                if (confirmed) {
                    saveChanges();
                }
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

    private void updateTotalNumbers() {
        try {
            totalNumberBedrooms = Integer.parseInt(propertyBedrooms_txtf.getText());
        } catch (NumberFormatException e) {
            totalNumberBedrooms = 0; // Default to 0 if input is invalid
        }

        try {
            totalNumberRooms = Integer.parseInt(propertyRooms_txtf.getText());
        } catch (NumberFormatException e) {
            totalNumberRooms = 0; // Default to 0 if input is invalid
        }

        // Optionally, you can update some UI elements or perform other actions based on the new totals
        System.out.println("Total Bedrooms: " + totalNumberBedrooms);
        System.out.println("Total Rooms: " + totalNumberRooms);
    }

    private void resetErrorLabels() {
        address_err.setText("");
        price_err.setText("");
        status_err.setText("");
        businessType_err.setText("");
        squareMeters_err.setText("");
        parkingSpace_err.setText("");
        bedroom_err.setText("");
        room_err.setText("");
    }
}
