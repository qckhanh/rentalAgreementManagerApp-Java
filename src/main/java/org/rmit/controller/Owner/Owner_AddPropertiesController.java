package org.rmit.controller.Owner;

import atlantafx.base.layout.DeckPane;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import net.synedra.validatorfx.Validator;
import org.rmit.Helper.ImageUtils;
import org.rmit.Helper.InputValidator;
import org.rmit.Helper.TaskUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.database.CommercialPropertyDAO;
import org.rmit.database.DAOInterface;
import org.rmit.database.ResidentialPropertyDAO;
import org.rmit.view.ViewCentral;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Property.*;
import org.rmit.model.Session;
import org.rmit.view.Owner.OWNER_MENU_OPTION;
import org.rmit.view.Start.NOTIFICATION_TYPE;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    public Button clearSelectedImage;
    public AnchorPane anchorPane;

    Validator validatorCP = new Validator();
    Validator validatorRP = new Validator();

    private int totalNumberBedrooms = 0;
    private int totalNumberRooms = 0;

    private int currentImageIndex = 0;
    private int totalImages = 0;
    private List<byte[]> selectedImages = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeOfProperty_choiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            imageView_propertyImg.setImage(ImageUtils.byteToImage(null));
            selectedImages.clear();
            currentImageIndex = 0;
            totalImages = 0;

            if (newValue == PropertyType.COMMERCIAL) {
                System.out.println("DEBUG: Commercial");
                clearData();
                setDisableCommercialFields(false);
                setDisableResidentialFields(true);

                resetErrorLabels();
                validatorCP.clear();
                validatorRP.clear();
                validateInputCP();
            }
            else if (newValue == PropertyType.RESIDENTIAL) {
                System.out.println("DEBUG: Residential");
                clearData();
                setDisableResidentialFields(false);
                setDisableCommercialFields(true);

                resetErrorLabels();
                validatorCP.clear();
                validatorRP.clear();
                validateInputRP();
            }

            returnTableView_btn.setOnAction(e -> returnTableView());
            addProperty_btn.setOnAction(e -> addProperty());
            nextImg_btn.setOnAction(e -> nextImg_btn());
            prevImg_btn.setOnAction(e -> prevImg_btn());
            addImage_btn.setOnAction(e -> addImage());
            clearSelectedImage.setOnAction(e -> clearSelectedImage());
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

        // Add listeners to update total number of bedrooms and rooms
        propertyBedrooms_txtf.textProperty().addListener((observable, oldValue, newValue) -> updateTotalNumbers());
        propertyRooms_txtf.textProperty().addListener((observable, oldValue, newValue) -> updateTotalNumbers());

        // NEW: Add listeners to clear error labels
        propertyAddress_txtf.textProperty().addListener((observable, oldValue, newValue) -> {
            address_err.setText(""); // Clear error label
        });

        propertyPrice_txtf.textProperty().addListener((observable, oldValue, newValue) -> {
            price_err.setText(""); // Clear error label
        });

        propertyBtype_txtf.textProperty().addListener((observable, oldValue, newValue) -> {
            businessType_err.setText(""); // Clear error label
        });

        propertySquareMeters_txtf.textProperty().addListener((observable, oldValue, newValue) -> {
            squareMeters_err.setText(""); // Clear error label
        });

        propertyPSpaces_txtf.textProperty().addListener((observable, oldValue, newValue) -> {
            parkingSpace_err.setText(""); // Clear error label
        });

        propertyBedrooms_txtf.textProperty().addListener((observable, oldValue, newValue) -> {
            bedroom_err.setText(""); // Clear error label
        });

        propertyRooms_txtf.textProperty().addListener((observable, oldValue, newValue) -> {
            room_err.setText(""); // Clear error label
        });

        resetErrorLabels();
        decor();
    }

    private void decor(){
        UIDecorator.setNormalButton(prevImg_btn, UIDecorator.PREVIOUS(), null);
        UIDecorator.setNormalButton(nextImg_btn, UIDecorator.NEXT(), null);
        UIDecorator.setNormalButton(addImage_btn, UIDecorator.ADD(), null);
        UIDecorator.setNormalButton(clearSelectedImage, UIDecorator.DELETE(), null);
        UIDecorator.setNormalButton(returnTableView_btn, UIDecorator.BACK_PREVIOUS_PAGE(), null);
        UIDecorator.setNormalButton(addProperty_btn, UIDecorator.SEND(), "Add new roperty");
    }

    private void validateInputCP() {
        validatorCP.createCheck()
                .dependsOn("propertyAddress", propertyAddress_txtf.textProperty())
                .withMethod(context -> {
                    String input = context.get("propertyAddress");
                    if (!InputValidator.NoCondition(input, address_err)) {
                        context.error("Address must not be empty");
                        System.out.println("Validation Error: Address must not be empty");
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
                    else if (input.isEmpty()) {
                        price_err.setText("Field must not be empty");
                        System.out.println("Validation Error: Field must not be empty");
                    }
                })
                .decorates(propertyPrice_txtf)
                .immediateClear();

        validatorCP.createCheck()
                .dependsOn("propertyStatus", propertyStatus_cbox.valueProperty())
                .withMethod(context -> {
                    PropertyStatus input = context.get("propertyStatus");
                    if (input == null) {
                        status_err.setText("A Status must be selected");
                        context.error("A Status must be selected");
                        System.out.println("Validation Error: A Status must be selected");
                    }
                })
                .decorates(propertyStatus_cbox)
                .immediateClear();

        if (typeOfProperty_choiceBox.getValue() == PropertyType.COMMERCIAL) {
            validatorCP.createCheck()
                    .dependsOn("propertyBtype", propertyBtype_txtf.textProperty())
                    .withMethod(context -> {
                        String input = context.get("propertyBtype");
                        if (!InputValidator.NoCondition(input, businessType_err)) {
                            context.error("BusinessType must not be empty");
                            System.out.println("Validation Error: BusinessType must not be empty");
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
                            System.out.println("Validation Error: Square meters must be a valid number");
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
                            parkingSpace_err.setText("Parking spaces must be a valid number");
                            System.out.println("Validation Error: Parking spaces must be a valid number");
                        }
                    })
                    .decorates(propertyPSpaces_txtf)
                    .immediateClear();
        }
        addProperty_btn.disableProperty().bind(validatorCP.containsErrorsProperty());
    }

    private void validateInputRP() {
        validatorRP.createCheck()
                .dependsOn("propertyAddress", propertyAddress_txtf.textProperty())
                .withMethod(context -> {
                    String input = context.get("propertyAddress");
                    if (!InputValidator.NoCondition(input, address_err)) {
//                        address_err.setText("Address must not be empty");
                        context.error("Address must not be empty");
                        System.out.println("Validation Error: Address must not be empty");
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
                        System.out.println("Validation Error: Price must be a valid number");
                        if (input.isEmpty()) {
//                            price_err.setText("Field must not be empty");
                            System.out.println("Validation Error: Field must not be empty");
                        }
                    }
                })
                .decorates(propertyPrice_txtf)
                .immediateClear();

        validatorRP.createCheck()
                .dependsOn("propertyStatus", propertyStatus_cbox.valueProperty())
                .withMethod(context -> {
                    PropertyStatus input = context.get("propertyStatus");
                    if (input == null) {
                        status_err.setText("A Status must be selected");
                        context.error("A Status must be selected");
                        System.out.println("Validation Error: A Status must be selected");
                    }
                })
                .decorates(propertyStatus_cbox)
                .immediateClear();

        if (typeOfProperty_choiceBox.getValue() == PropertyType.RESIDENTIAL) {
            validatorRP.createCheck()
                    .dependsOn("propertyBedrooms", propertyBedrooms_txtf.textProperty())
                    .withMethod(context -> {
                        String input = context.get("propertyBedrooms");
                        if (!InputValidator.isValidBedrooms(input, bedroom_err, totalNumberRooms)) {
                            context.error("Bedrooms must be a valid number");
                            System.out.println("Validation Error: Bedrooms must be a valid number");
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
                            System.out.println("Validation Error: Rooms must be a valid number");
                        }
                    })
                    .decorates(propertyRooms_txtf)
                    .immediateClear();
        }

        addProperty_btn.disableProperty().bind(validatorRP.containsErrorsProperty());
    }

    private void setDisableCommercialFields(boolean status) {
        propertyBtype_txtf.setDisable(status);
        propertySquareMeters_txtf.setDisable(status);
        propertyPSpaces_txtf.setDisable(status);
    }

    private void setDisableResidentialFields(boolean status) {
        propertyGarden_chbox.setDisable(status);
        propertyPet_chBox.setDisable(status);
        propertyBedrooms_txtf.setDisable(status);
        propertyRooms_txtf.setDisable(status);
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
        ViewCentral.getInstance().getOwnerViewFactory().setOwnerSelectedMenuItem(OWNER_MENU_OPTION.PROPERTIES_MANAGER);
    }

    private void addProperty() {
            if (typeOfProperty_choiceBox.getValue() == PropertyType.COMMERCIAL) {
                if (!validatorCP.validate()){
                    ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.WARNING, anchorPane, "Invalid input. Please check again");
                    return;
                }
                if(!ViewCentral.getInstance().getStartViewFactory().confirmMessage("Save changes?")) return;
                CommercialPropertyDAO commercialPropertyDAO = new CommercialPropertyDAO();
                CommercialProperty cp = new CommercialProperty();
                CommercialPropertyFactory(cp);
                for(byte[] img : selectedImages) cp.addImages(img);

                Task<Boolean> addProperty = TaskUtils.createTask(() -> commercialPropertyDAO.add(cp));
                TaskUtils.run(addProperty);
                addProperty.setOnSucceeded(e -> Platform.runLater(() -> {
                    if (addProperty.getValue()){
                        ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Commercial property successfully created");
                    }
                    else{
                        ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Failed to create new property. Try again");
                    }
                }));

            } else if (typeOfProperty_choiceBox.getValue() == PropertyType.RESIDENTIAL) {
                if (!validatorRP.validate()){
                    ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.WARNING, anchorPane, "Invalid input. Please check again");
                    return;
                }
                if(!ViewCentral.getInstance().getStartViewFactory().confirmMessage("Save changes?")) return;
                ResidentialPropertyDAO residentialPropertyDAO = new ResidentialPropertyDAO();
                ResidentialProperty rp = new ResidentialProperty();
                for(byte[] img : selectedImages) rp.addImages(img);
                ResidentialPropertyFactory(rp);
                Task<Boolean> addProperty = TaskUtils.createTask(() -> residentialPropertyDAO.add(rp));
                TaskUtils.run(addProperty);
                addProperty.setOnSucceeded(e -> Platform.runLater(() -> {
                    if (addProperty.getValue()){
                        ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Residential property successfully created");
                    }
                    else{
                        ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Failed to create new property. Try again");
                    }
                }));
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

    private void addImage() {
        if(selectedImages.size() >= 3) {
            ViewCentral.getInstance().getStartViewFactory().pushNotification(
                    NOTIFICATION_TYPE.WARNING,
                    anchorPane,
                    "Maximum 3 images allowed"
            );
        }
        else {
            String path = ImageUtils.openFileChooseDialog();
            if (!path.equals(ImageUtils.DEFAULT_IMAGE)) {
                selectedImages.add(ImageUtils.getByte(path));
                totalImages = selectedImages.size();
                currentImageIndex = totalImages - 1;
                imageView_propertyImg.setImage(ImageUtils.byteToImage(selectedImages.get(currentImageIndex)));
                totalImages++;
                ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Image added successfully");

            }
            else {
                ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "No image selected. Image must < 1MB");
            }
        }
    }

    private void prevImg_btn() {
        if(selectedImages.size() == 0 || selectedImages == null){
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.WARNING, anchorPane, "No images to display");
            return;
        }
        int selectedImagesSize = selectedImages.size();
        int position = (currentImageIndex - 1 + selectedImagesSize) % selectedImagesSize;
        currentImageIndex = position;
        imageView_propertyImg.setImage(ImageUtils.byteToImage(selectedImages.get(position)));
    }

    private void nextImg_btn() {
        if(selectedImages.size() == 0 || selectedImages == null){
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.WARNING, anchorPane, "No images to display");
            return;
        }
        int selectedImagesSize = selectedImages.size();

        int position = (currentImageIndex  + 1) % selectedImagesSize;
        currentImageIndex = position;
        imageView_propertyImg.setImage(ImageUtils.byteToImage(selectedImages.get(position)));
    }

    private void clearSelectedImage() {
        selectedImages.clear();
        imageView_propertyImg.setImage(ImageUtils.byteToImage(null));
    }
}
