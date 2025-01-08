package org.rmit.controller.Owner;

import atlantafx.base.layout.DeckPane;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.synedra.validatorfx.Validator;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.Helper.ImageUtils;
import org.rmit.Helper.InputValidator;
import org.rmit.Helper.UIDecorator;
import org.rmit.database.CommercialPropertyDAO;
import org.rmit.database.ResidentialPropertyDAO;
import org.rmit.model.ModelCentral;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Property.Property;
import org.rmit.model.Property.PropertyStatus;
import org.rmit.model.Property.ResidentialProperty;
import org.rmit.view.Owner.OWNER_MENU_OPTION;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

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
    public Button prevImg_btn;

    public DeckPane imageShow_deckPane;
    public ImageView imageView_propertyImg;
    public Button nextImg_btn;
    public Button addImage_btn;
    public Button clearImage_btn;
    Validator validatorCP = new Validator();
    Validator validatorRP = new Validator();

    private int totalNumberBedrooms = 0;
    private int totalNumberRooms = 0;
    private List<byte[]> images = new ArrayList<>();
    private int currentImageIndex = 0;


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
        returnTableView_btn.setOnAction(e-> {
            ModelCentral.getInstance().getOwnerViewFactory().setOwnerSelectedMenuItem(OWNER_MENU_OPTION.PROPERTIES_MANAGER);
        });

        addSelectedPropertyChangeListener();
        Image firstImg = (images.size() == 0) ? ImageUtils.byteToImage(null) : ImageUtils.byteToImage(images.get(0));
        imageView_propertyImg.setImage(firstImg);
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
            propertyBedrooms_txtf.textProperty().addListener((observableValue, s, t1) -> {
                checkChanges();
                updateTotalNumbersOfRooms();
            });
            propertyRooms_txtf.textProperty().addListener((observableValue, s, t1) -> {
                checkChanges();
                updateTotalNumbersOfRooms();
            });
        }

        setDisable(true);

        updateProperty_btn.setText("Edit");
        updateProperty_btn.setDisable(false);
        updateProperty_btn.setOnAction(e-> {
            if (selectedProperty.get() instanceof CommercialProperty && validatorCP.validate()) updateProperty();
            else if (selectedProperty.get() instanceof ResidentialProperty && validatorRP.validate()) updateProperty();
        });

        nextImg_btn.setOnAction(e -> nextImg_btn());
        prevImg_btn.setOnAction(e -> prevImg_btn());
        clearImage_btn.setOnAction(e -> clearSelectedImage());
        addImage_btn.setOnAction(e -> addImage());

        addListener();
        resetErrorLabels();
        validateInputCP();
        validateInputRP();
        decor();

//        if (selectedProperty.get() instanceof CommercialProperty) {
//            updateProperty_btn.disableProperty().bind(validatorCP.containsErrorsProperty());
//        }
//
//        else if (selectedProperty.get() instanceof ResidentialProperty) {
//            updateProperty_btn.disableProperty().bind(validatorRP.containsErrorsProperty());
//        }
    }

    private void decor(){
        UIDecorator.setNormalButton(prevImg_btn, UIDecorator.PREVIOUS(), null);
        UIDecorator.setNormalButton(nextImg_btn, UIDecorator.NEXT(), null);
        UIDecorator.setNormalButton(addImage_btn, UIDecorator.ADD(), null);
        UIDecorator.setNormalButton(clearImage_btn, UIDecorator.DELETE(), null);
        UIDecorator.setNormalButton(returnTableView_btn, UIDecorator.PREVIOUS(), "Back");
        UIDecorator.setNormalButton(updateProperty_btn, UIDecorator.SEND(), "Update");
    }

//    private void validateInput() {
//        validator.createCheck()
//                .dependsOn("propertyAddress", propertyAddress_txtf.textProperty())
//                .withMethod(context -> {
//                    String input = context.get("propertyAddress");
//                    if (!InputValidator.NoCondition(input, address_err)) {
//                        context.error("Address must not be empty");
//                    }
//                })
//                .decorates(propertyAddress_txtf)
//                .immediateClear();
//
//        validator.createCheck()
//                .dependsOn("propertyPrice", propertyPrice_txtf.textProperty())
//                .withMethod(context -> {
//                    String input = context.get("propertyPrice");
//                    if (!InputValidator.isValidPrice(input, price_err)) {
//                        context.error("Price must be a valid number");
//                    }
//                })
//                .decorates(propertyPrice_txtf)
//                .immediateClear();
//
//        validator.createCheck()
//                .dependsOn("propertyStatus", propertyStatus_cbox.valueProperty())
//                .withMethod(context -> {
//                    PropertyStatus input = context.get("propertyStatus");
//                    if (input == null) {
//                        context.error("A Status must be selected");
//                        status_err.setText("A Status must be selected");
//                    }
//                })
//                .decorates(propertyStatus_cbox)
//                .immediateClear();
//
//        if (selectedProperty.get() instanceof CommercialProperty) {
//            validator.createCheck()
//                    .dependsOn("propertyBtype", propertyBtype_txtf.textProperty())
//                    .withMethod(context -> {
//                        String input = context.get("propertyBtype");
//                        if (!InputValidator.NoCondition(input, businessType_err)) {
//                            context.error("BusinessType must not be empty");
//                        }
//                    })
//                    .decorates(propertyBtype_txtf)
//                    .immediateClear();
//
//            validator.createCheck()
//                    .dependsOn("propertySquareMeters", propertySquareMeters_txtf.textProperty())
//                    .withMethod(context -> {
//                        String input = context.get("propertySquareMeters");
//                        if (!InputValidator.isValidSquareMeters(input, squareMeters_err)) {
//                            context.error("Square meters must be a valid number");
//                        }
//                    })
//                    .decorates(propertySquareMeters_txtf)
//                    .immediateClear();
//
//            validator.createCheck()
//                    .dependsOn("propertyPSpaces", propertyPSpaces_txtf.textProperty())
//                    .withMethod(context -> {
//                        String input = context.get("propertyPSpaces");
//                        if (!InputValidator.isValidParkingSpaces(input, parkingSpace_err)) {
//                            context.error("Parking spaces must be a valid number");
//                        }
//                    })
//                    .decorates(propertyPSpaces_txtf)
//                    .immediateClear();
//        } else if (selectedProperty.get() instanceof ResidentialProperty) {
//            validator.createCheck()
//                    .dependsOn("propertyBedrooms", propertyBedrooms_txtf.textProperty())
//                    .withMethod(context -> {
//                        String input = context.get("propertyBedrooms");
//                        if (!InputValidator.isValidBedrooms(input, bedroom_err, totalNumberRooms)) {
//                            context.error("Bedrooms must be a valid number");
//                        }
//                    })
//                    .decorates(propertyBedrooms_txtf)
//                    .immediateClear();
//
//            validator.createCheck()
//                    .dependsOn("propertyRooms", propertyRooms_txtf.textProperty())
//                    .withMethod(context -> {
//                        String input = context.get("propertyRooms");
//                        if (!InputValidator.isValidRooms(input, room_err, totalNumberBedrooms)) {
//                            context.error("Rooms must be a valid number");
//                        }
//                    })
//                    .decorates(propertyRooms_txtf)
//                    .immediateClear();
//        }
//
////        updateProperty_btn.disableProperty().bind(validator.containsErrorsProperty());
//    }

    private void validateInputCP(){
        validatorCP.createCheck()
                .dependsOn("propertyAddress", propertyAddress_txtf.textProperty())
                .withMethod(context -> {
                    String input = context.get("propertyAddress");
                    if (!InputValidator.NoCondition(input, address_err)) {
                        context.error("Address must not be empty");
                    }
                })
                .decorates(propertyAddress_txtf)
                .immediateClear();

        validatorCP.createCheck()
                .dependsOn("propertyPrice", propertyPrice_txtf.textProperty())
                .withMethod(context -> {
                    String input = context.get("propertyPrice");
                    if (!InputValidator.isValidPrice(input, price_err)) {
                        context.error("Price must be a valid number");
                    }
                })
                .decorates(propertyPrice_txtf)
                .immediateClear();

        validatorCP.createCheck()
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
            validatorCP.createCheck()
                    .dependsOn("propertyBtype", propertyBtype_txtf.textProperty())
                    .withMethod(context -> {
                        String input = context.get("propertyBtype");
                        if (!InputValidator.NoCondition(input, businessType_err)) {
                            context.error("BusinessType must not be empty");
                        }
                    })
                    .decorates(propertyBtype_txtf)
                    .immediateClear();

            validatorCP.createCheck()
                    .dependsOn("propertySquareMeters", propertySquareMeters_txtf.textProperty())
                    .withMethod(context -> {
                        String input = context.get("propertySquareMeters");
                        if (!InputValidator.isValidSquareMeters(input, squareMeters_err)) {
                            context.error("Square meters must be a valid number");
                        }
                    })
                    .decorates(propertySquareMeters_txtf)
                    .immediateClear();

            validatorCP.createCheck()
                    .dependsOn("propertyPSpaces", propertyPSpaces_txtf.textProperty())
                    .withMethod(context -> {
                        String input = context.get("propertyPSpaces");
                        if (!InputValidator.isValidParkingSpaces(input, parkingSpace_err)) {
                            context.error("Parking spaces must be a valid number");
                        }
                    })
                    .decorates(propertyPSpaces_txtf)
                    .immediateClear();
        }
        updateProperty_btn.disableProperty().bind(validatorCP.containsErrorsProperty());
    }

    private void validateInputRP(){
        validatorRP.createCheck()
                .dependsOn("propertyAddress", propertyAddress_txtf.textProperty())
                .withMethod(context -> {
                    String input = context.get("propertyAddress");
                    if (!InputValidator.NoCondition(input, address_err)) {
                        context.error("Address must not be empty");
                    }
                })
                .decorates(propertyAddress_txtf)
                .immediateClear();

        validatorRP.createCheck()
                .dependsOn("propertyPrice", propertyPrice_txtf.textProperty())
                .withMethod(context -> {
                    String input = context.get("propertyPrice");
                    if (!InputValidator.isValidPrice(input, price_err)) {
                        context.error("Price must be a valid number");
                    }
                })
                .decorates(propertyPrice_txtf)
                .immediateClear();

        validatorRP.createCheck()
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

        if (selectedProperty.get() instanceof ResidentialProperty) {

            validatorRP.createCheck()
                    .dependsOn("propertyBedrooms", propertyBedrooms_txtf.textProperty())
                    .withMethod(context -> {
                        String input = context.get("propertyBedrooms");
                        if (!InputValidator.isValidBedrooms(input, bedroom_err, totalNumberRooms)) {
                            context.error("Bedrooms must be a valid number");
                        }
                    })
                    .decorates(propertyBedrooms_txtf)
                    .immediateClear();

            validatorRP.createCheck()
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
        updateProperty_btn.disableProperty().bind(validatorRP.containsErrorsProperty());
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
        }
        else if (selectedProperty.get() instanceof ResidentialProperty) {
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
            boolean confirmed = ModelCentral.getInstance().getStartViewFactory().confirmMessage("Save changes?");
            if (confirmed) saveChanges();
        }
        else {
            setDisable(false);
        }
    }

    private void saveChanges() {
        selectedProperty.get().setAddress(propertyAddress_txtf.getText());
        selectedProperty.get().setPrice(Double.parseDouble(propertyPrice_txtf.getText()));
        selectedProperty.get().setStatus(propertyStatus_cbox.getValue());
        for(byte[] bytes: images){
            selectedProperty.get().addImages(bytes);
        }
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


    // IT MIGHT BE HERE
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

        if(isDifferent(images, selectedProperty.get().getImages())) {
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
//            if (!propertyBedrooms_txtf.getText().equals(String.valueOf(((ResidentialProperty) selectedProperty.get()).getTotalBedroom()))) {
//                changed = true;
//            }
//            if (!propertyRooms_txtf.getText().equals(String.valueOf(((ResidentialProperty) selectedProperty.get()).getTotalRoom()))) {
//                changed = true;
//            }

            // Check if the total number of bedrooms and rooms have changed
            if (totalNumberBedrooms != ((ResidentialProperty) selectedProperty.get()).getTotalBedroom()) {
                System.out.println("Total Bedrooms: " + totalNumberBedrooms);
                System.out.println("Property Bedrooms: " + ((ResidentialProperty) selectedProperty.get()).getTotalBedroom());
                changed = true;
            }
            if (totalNumberRooms != ((ResidentialProperty) selectedProperty.get()).getTotalRoom()) {
                System.out.println("Total Rooms: " + totalNumberRooms);
                System.out.println("Property Rooms: " + ((ResidentialProperty) selectedProperty.get()).getTotalRoom());
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
        images.clear();
        images.addAll(property.getImages());
        propertyAddress_txtf.setText(property.getAddress());
        propertyPrice_txtf.setText(String.valueOf(property.getPrice()));
        propertyStatus_cbox.getItems().addAll(PropertyStatus.values());
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
            propertyRooms_txtf.setText(String.valueOf(((ResidentialProperty) property).getTotalRoom()));
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
        images.clear();
        currentImageIndex = 0;
    }

    private void updateTotalNumbersOfRooms() {
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

    private void addImage() {
        if(images.size() >= 3) System.out.println("Exception: Image limit reached");
        else {
            String path = ImageUtils.openFileChooseDialog();
            if (path != ImageUtils.DEFAULT_IMAGE) {
                images.add(ImageUtils.getByte(path));
                currentImageIndex = images.size() - 1;
                imageView_propertyImg.setImage(ImageUtils.byteToImage(images.get(currentImageIndex)));
            }
        }
    }

    private void prevImg_btn() {
        if(images.size() == 0){
            System.out.println("Exception: No images to display");
            return;
        }
        int selectedImagesSize = images.size();
        int position = (currentImageIndex - 1 + selectedImagesSize) % selectedImagesSize;
        currentImageIndex = position;
        imageView_propertyImg.setImage(ImageUtils.byteToImage(images.get(position)));
    }

    private void nextImg_btn() {
        if(images.size() == 0){
            System.out.println("Exception: No images to display");
            return;
        }
        int selectedImagesSize = images.size();

        int position = (currentImageIndex  + 1) % selectedImagesSize;
        currentImageIndex = position;
        imageView_propertyImg.setImage(ImageUtils.byteToImage(images.get(position)));
    }

    private void clearSelectedImage() {
        images.clear();
        imageView_propertyImg.setImage(null);
    }

    boolean isDifferent(List<byte[] > a, List<byte[]> b) {
        if (a.size() != b.size()) return true;
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) return true;
        }
        return false;
    }
}
