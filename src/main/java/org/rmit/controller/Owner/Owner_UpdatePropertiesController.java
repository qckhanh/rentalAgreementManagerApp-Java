package org.rmit.controller.Owner;

import atlantafx.base.layout.DeckPane;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
import org.rmit.view.Start.NOTIFICATION_TYPE;

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
    public Button editProperty;
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
    public AnchorPane anchorPane;
    public Button saveChange;
    Validator validatorCP = new Validator();
    Validator validatorRP = new Validator();


    private int totalNumberBedrooms = 0;
    private int totalNumberRooms = 0;
    private List<byte[]> images = new ArrayList<>();
    private int currentImageIndex = 0;

    public Owner_UpdatePropertiesController() {
    }

    public static void setSelectedProperty(Property property) {       // need optimization
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

        loadPropertyData();
        setDisableTextField(true);
        editProperty.setOnAction(e-> editProperty());

        nextImg_btn.setOnAction(e -> nextImg_btn());
        prevImg_btn.setOnAction(e -> prevImg_btn());
        clearImage_btn.setOnAction(e -> clearSelectedImage());
        addImage_btn.setOnAction(e -> addImage());
        saveChange.setOnAction(e -> saveChanges());

//        addListener();
        resetErrorLabels();
        validateCP();
        validateRP();

        decor();
        returnTableView_btn.setOnAction(e-> {
            ModelCentral.getInstance().getOwnerViewFactory().setOwnerSelectedMenuItem(OWNER_MENU_OPTION.PROPERTIES_MANAGER);
        });
    }

    private void decor(){
        UIDecorator.setNormalButton(prevImg_btn, UIDecorator.PREVIOUS(), null);
        UIDecorator.setNormalButton(nextImg_btn, UIDecorator.NEXT(), null);
        UIDecorator.setNormalButton(addImage_btn, UIDecorator.ADD(), null);
        UIDecorator.setNormalButton(clearImage_btn, UIDecorator.DELETE(), null);
        UIDecorator.setNormalButton(returnTableView_btn, UIDecorator.BACK_PREVIOUS_PAGE(), null);
        UIDecorator.setNormalButton(editProperty, UIDecorator.SEND(), "Update");
    }


    private boolean isChange(Property property){
        if(!property.getAddress().equals(propertyAddress_txtf.getText())) return true;
        if(property.getPrice() != Double.parseDouble(propertyPrice_txtf.getText())) return true;
        if(property.getStatus() != propertyStatus_cbox.getValue()) return true;
        if(isDifferent(images, property.getImages())) return true;
        if(property instanceof CommercialProperty){
            if(!((CommercialProperty) property).getBusinessType().equals(propertyBtype_txtf.getText())) return true;
            if(((CommercialProperty) property).getSquareMeters() != Double.parseDouble(propertySquareMeters_txtf.getText())) return true;
            if(((CommercialProperty) property).getTotalParkingSpace() != Integer.parseInt(propertyPSpaces_txtf.getText())) return true;
        }
        else if(property instanceof ResidentialProperty){
            if(((ResidentialProperty) property).isHasGardenProperty() != propertyGarden_chbox.isSelected()) return true;
            if(((ResidentialProperty) property).isIsPetAllowedProperty() != propertyPet_chBox.isSelected()) return true;
            if(((ResidentialProperty) property).getTotalBedroom() != Integer.parseInt(propertyBedrooms_txtf.getText())) return true;
            if(((ResidentialProperty) property).getTotalRoom() != Integer.parseInt(propertyRooms_txtf.getText())) return true;
        }
        return false;
    }

    private void validateCP() {
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
    }

    private void validateRP() {
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

        validatorRP.createCheck()
                .dependsOn("propertyBedrooms", propertyBedrooms_txtf.textProperty())
                .withMethod(context -> {
                    String input = context.get("propertyBedrooms");
                    if (!InputValidator.isValidRoom(input, bedroom_err)) {
                        context.error("Bedrooms must be a valid number");
                    }
                })
                .decorates(propertyBedrooms_txtf)
                .immediateClear();

        validatorRP.createCheck()
                .dependsOn("propertyRooms", propertyRooms_txtf.textProperty())
                .withMethod(context -> {
                    String input = context.get("propertyRooms");
                    if (!InputValidator.isValidRoom(input, room_err)) {
                        context.error("Rooms must be a valid number");
                    }
                })
                .decorates(propertyRooms_txtf)
                .immediateClear();

        validatorRP.createCheck()
                .dependsOn("propertyBedrooms", propertyBedrooms_txtf.textProperty())
                .dependsOn("propertyRooms", propertyRooms_txtf.textProperty())
                .withMethod(context -> {
                    if(!InputValidator.isValidRoomsAndBedroom(context.get("propertyRooms"), context.get("propertyBedrooms"))){
                        context.error("Rooms must be a valid number");
                    }
                })
                .decorates(propertyRooms_txtf)
                .immediateClear();

    }

//    private void addListener() {
//        propertyAddress_txtf.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue.equals(oldValue)) address_err.setText("");
//            checkChanges();
//        });
//        propertyPrice_txtf.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue.equals(oldValue)) price_err.setText("");
//            checkChanges();
//        });
//        propertyStatus_cbox.valueProperty().addListener((observable, oldValue, newValue) -> {
//            if (!Objects.equals(oldValue, newValue)) status_err.setText("");
//            checkChanges();
//        });
//        if (selectedProperty.get() instanceof CommercialProperty) {
//            propertyBtype_txtf.textProperty().addListener((observable, oldValue, newValue) -> {
//                if (!newValue.equals(oldValue)) businessType_err.setText("");
//                checkChanges();
//            });
//            propertySquareMeters_txtf.textProperty().addListener((observable, oldValue, newValue) -> {
//                if (!newValue.equals(oldValue)) squareMeters_err.setText("");
//                checkChanges();
//            });
//            propertyPSpaces_txtf.textProperty().addListener((observable, oldValue, newValue) -> {
//                if (!newValue.equals(oldValue)) parkingSpace_err.setText("");
//                checkChanges();
//            });
//        } else if (selectedProperty.get() instanceof ResidentialProperty) {
//            propertyGarden_chbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
//                if (!Objects.equals(oldValue, newValue)) checkChanges();
//            });
//            propertyPet_chBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
//                if (!Objects.equals(oldValue, newValue)) checkChanges();
//            });
//            propertyBedrooms_txtf.textProperty().addListener((observable, oldValue, newValue) -> {
//                if (!newValue.equals(oldValue)) bedroom_err.setText("");
//                checkChanges();
//            });
//            propertyRooms_txtf.textProperty().addListener((observable, oldValue, newValue) -> {
//                if (!newValue.equals(oldValue)) room_err.setText("");
//                checkChanges();
//            });
//        }
//    }

    private void editProperty() {
        boolean isDisable = propertyAddress_txtf.isDisable();
        setDisableTextField(!isDisable);
        saveChange.setVisible(isDisable);


    }

    private void saveChanges() {
//        validatorRP.clear();
//        validatorCP.clear();
        validateCP();
        validateRP();

        if(selectedProperty.get() instanceof CommercialProperty) {
            if (!validatorCP.validate()) {
                ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.WARNING, anchorPane, "Invalid input. Please check again");
                return;
            }
        }
        else if(selectedProperty.get() instanceof ResidentialProperty) {
            if (!validatorRP.validate()) {
                ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.WARNING, anchorPane, "Invalid input. Please check again");
                return;
            }
        }
        if(!isChange(selectedProperty.get())){
            ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.WARNING, anchorPane, "No changes detected");
            return;
        }
        if(!ModelCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to save changes?")){
            return;
        }

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
        boolean isUpdated = false;
        if (selectedProperty.get() instanceof CommercialProperty) {
            CommercialPropertyDAO cpDAO = new CommercialPropertyDAO();
            isUpdated = cpDAO.update((CommercialProperty) selectedProperty.get());
        }
        else if (selectedProperty.get() instanceof ResidentialProperty) {
            ResidentialPropertyDAO rpDAO = new ResidentialPropertyDAO();
            isUpdated = rpDAO.update((ResidentialProperty) selectedProperty.get());
        }
        if(isUpdated) {
            ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Property updated successfully");
        }
        else {
            ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Failed to update property. Try again");
        }
        setDisableTextField(true);
        saveChange.setVisible(false);

        validatorRP.clear();
        validatorCP.clear();
    }

    public void loadPropertyData() {
        selectedProperty.addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                System.out.println("Selected property has changed.");
                clearData();
                setDisableTextField(true);
                updateFormFields(newValue);
                saveChange.setVisible(false);
            }
        });

        if (selectedProperty.get() != null) {
            clearData();
            updateFormFields(selectedProperty.get());
        }
    }



    void setDisableTextField(boolean status) {
            propertyAddress_txtf.setDisable(status);
            propertyPrice_txtf.setDisable(status);
            propertyStatus_cbox.setDisable(status);
            if(selectedProperty.get() instanceof CommercialProperty){
                propertyBtype_txtf.setDisable(status);
                propertySquareMeters_txtf.setDisable(status);
                propertyPSpaces_txtf.setDisable(status);
            }
            else if(selectedProperty.get() instanceof ResidentialProperty){
                propertyGarden_chbox.setDisable(status);
                propertyPet_chBox.setDisable(status);
                propertyBedrooms_txtf.setDisable(status);
                propertyRooms_txtf.setDisable(status);
            }
    }

    private void updateFormFields(Property property) {
        images.clear();
        if(images.size() > 0) {
            currentImageIndex = 0;
            imageView_propertyImg.setImage(ImageUtils.byteToImage(images.get(0)));
        }
        else{
            imageView_propertyImg.setImage(ImageUtils.byteToImage(null));
        }
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


        ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.INFO, anchorPane, "Information loaded");

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
        if(images.size() >= 3) {
            ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.WARNING, anchorPane, "Maximum 3 images allowed");
        }
        else {
            String path = ImageUtils.openFileChooseDialog();
            if (path != ImageUtils.DEFAULT_IMAGE) {
                images.add(ImageUtils.getByte(path));
                currentImageIndex = images.size() - 1;
                imageView_propertyImg.setImage(ImageUtils.byteToImage(images.get(currentImageIndex)));
                ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Image added successfully");
            }
            else{
                ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.WARNING, anchorPane, "No image selected. Image size must be less than 1MB");
            }
        }
    }

    private void prevImg_btn() {
        if(images.size() == 0){
            ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.WARNING, anchorPane, "No images to display");
            return;
        }
        int selectedImagesSize = images.size();
        int position = (currentImageIndex - 1 + selectedImagesSize) % selectedImagesSize;
        currentImageIndex = position;
        imageView_propertyImg.setImage(ImageUtils.byteToImage(images.get(position)));
    }

    private void nextImg_btn() {
        if(images.size() == 0){
            ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.WARNING, anchorPane, "No images to display");
            return;
        }
        int selectedImagesSize = images.size();

        int position = (currentImageIndex  + 1) % selectedImagesSize;
        currentImageIndex = position;
        imageView_propertyImg.setImage(ImageUtils.byteToImage(images.get(position)));
    }

    private void clearSelectedImage() {
        images.clear();
        imageView_propertyImg.setImage(ImageUtils.byteToImage(null));
    }

    boolean isDifferent(List<byte[] > a, List<byte[]> b) {
        if (a.size() != b.size()) return true;
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) return true;
        }
        return false;
    }
}
