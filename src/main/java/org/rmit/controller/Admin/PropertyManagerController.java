package org.rmit.controller.Admin;

import atlantafx.base.layout.DeckPane;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import net.synedra.validatorfx.Validator;
import org.hibernate.Session;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.Helper.InputValidator;
import org.rmit.database.CommercialPropertyDAO;
import org.rmit.database.DAOInterface;
import org.rmit.database.ResidentialPropertyDAO;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Property.*;
import org.rmit.model.Property.Property;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

public class PropertyManagerController implements Initializable {
    public Button create_btn;
    public Button update_btn;
    public Button delete_btn;
    public Button prev_btn;
    public DeckPane deckPane;
    public Button next_btn;
    public TextField id_input;
    public TextField address_input;
    public TextField priceInput;
    public ComboBox<Owner> owner_comboBox;
    public ComboBox<PropertyStatus> status_comboBox;
    public Label infor1_label;
    public TextField infor1_input;
    public Label infor2_label;
    public TextField infor2_input;
    public Label infor3_label;
    public ComboBox infor3_comboBox;
    public Label infor4_label;
    public ComboBox infor4_comboBox;
    public TableView<Property> property_Tableview;
    public TableView<Host> Host_tableView;
    public TableView<RentalAgreement> RA_tableView;
    public Button addToDB_btn;

    public List<Property> propertyList = ModelCentral.getInstance().getAdminViewFactory().getAllProperty();
    public ObservableList<Property> propertiesObservableList = FXCollections.observableArrayList();
    public ObjectProperty<Property> selectedProperty = new SimpleObjectProperty<>();
    public TextField propertyType;
    public ComboBox<PropertyType> propertyType_comboBox;
    public ComboBox<PropertyStatus> propertyStatus_comboBox;
    public StringProperty squareMeters = new SimpleStringProperty();

    private Label noneLabel = new Label();
    private Validator validatorCP = new Validator();
    private Validator validatorRP = new Validator();

    private int totalNumberRooms = 0;
    private int totalNumberBedrooms = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpTableBehavior();
        setOnActionButton();
        setUpTableView();

        validateInputCP();
        validateInputRP();
        addToDB_btn.disableProperty().bind(validatorCP.containsErrorsProperty().or(validatorRP.containsErrorsProperty()));
    }

    private void validateInputCP() {
        validatorCP.createCheck()
                .dependsOn("address", address_input.textProperty())
                .withMethod(context -> {
                    String input = context.get("address");
                    if (!InputValidator.NoCondition(input, noneLabel)) {
                        context.error("Address must not be empty");
                    }
                })
                .decorates(address_input)
                .immediateClear();

        validatorCP.createCheck()
                .dependsOn("price", priceInput.textProperty())
                .withMethod(context -> {
                    String input = context.get("price");
                    if (!InputValidator.isValidPrice(input, noneLabel)) {
                        context.error("Price must be a valid number");
                    }
                })
                .decorates(priceInput)
                .immediateClear();

        validatorCP.createCheck()
                .dependsOn("owner", owner_comboBox.valueProperty())
                .withMethod(context -> {
                    Owner input = context.get("owner");
                    if (input == null) {
                        noneLabel.setText("An Owner must be selected");
                        context.error("An Owner must be selected");
                    }
                })
                .decorates(owner_comboBox)
                .immediateClear();

        validatorCP.createCheck()
                .dependsOn("status", status_comboBox.valueProperty())
                .withMethod(context -> {
                    PropertyStatus input = context.get("status");
                    if (input == null) {
                        noneLabel.setText("A Status must be selected");
                        context.error("A Status must be selected");
                    }
                })
                .decorates(status_comboBox)
                .immediateClear();

        validatorCP.createCheck()
                .dependsOn("infor1", infor1_input.textProperty())
                .withMethod(context -> {
                    String input = context.get("infor1");
                    if (!InputValidator.NoCondition(input, noneLabel)) {
                        context.error("Business Type must not be empty");
                    }
                })
                .decorates(infor1_input)
                .immediateClear();

        validatorCP.createCheck()
                .dependsOn("infor2", infor2_input.textProperty())
                .withMethod(context -> {
                    String input = context.get("infor2");
                    if (!InputValidator.isValidParkingSpaces(input, noneLabel)) {
                        context.error("Parking Space must be a valid number");
                    }
                })
                .decorates(infor2_input)
                .immediateClear();


//        validatorCP.createCheck()
//                .dependsOn("infor3", squareMeters)
//                .withMethod(context -> {
//                    String input = context.get("infor3");
//                    if (!InputValidator.isValidSquareMeters(input, noneLabel)) {
//                        context.error("Square Meters must be a valid number");
//                    }
//                })
//                .decorates(infor3_comboBox)
//                .immediateClear();
    }

    private void validateInputRP() {
        // Add listeners to update total number of bedrooms and rooms
        infor1_input.textProperty().addListener((observable, oldValue, newValue) -> updateTotalNumbers());
        infor2_input.textProperty().addListener((observable, oldValue, newValue) -> updateTotalNumbers());

        validatorRP.createCheck()
                .dependsOn("address", address_input.textProperty())
                .withMethod(context -> {
                    String input = context.get("address");
                    if (!InputValidator.NoCondition(input, noneLabel)) {
                        context.error("Address must not be empty");
                    }
                })
                .decorates(address_input)
                .immediateClear();

        validatorRP.createCheck()
                .dependsOn("price", priceInput.textProperty())
                .withMethod(context -> {
                    String input = context.get("price");
                    if (!InputValidator.isValidPrice(input, noneLabel)) {
                        context.error("Price must be a valid number");
                    }
                })
                .decorates(priceInput)
                .immediateClear();

        validatorRP.createCheck()
                .dependsOn("owner", owner_comboBox.valueProperty())
                .withMethod(context -> {
                    Owner input = context.get("owner");
                    if (input == null) {
                        noneLabel.setText("An Owner must be selected");
                        context.error("An Owner must be selected");
                    }
                })
                .decorates(owner_comboBox)
                .immediateClear();

        validatorRP.createCheck()
                .dependsOn("status", status_comboBox.valueProperty())
                .withMethod(context -> {
                    PropertyStatus input = context.get("status");
                    if (input == null) {
                        noneLabel.setText("A Status must be selected");
                        context.error("A Status must be selected");
                    }
                })
                .decorates(status_comboBox)
                .immediateClear();

        validatorRP.createCheck()
                .dependsOn("infor1", infor1_input.textProperty())
                .withMethod(context -> {
                    String input = context.get("infor1");
                    if (!InputValidator.isValidRooms(input, noneLabel, totalNumberBedrooms)) {
                        context.error("Rooms must be a valid number");
                    }
                })
                .decorates(infor1_input)
                .immediateClear();

        validatorRP.createCheck()
                .dependsOn("infor2", infor2_input.textProperty())
                .withMethod(context -> {
                    String input = context.get("infor2");
                    if (!InputValidator.isValidBedrooms(input, noneLabel, totalNumberRooms)) {
                        context.error("Bedrooms must be a valid number");
                    }
                })
                .decorates(infor2_input)
                .immediateClear();

        validatorRP.createCheck()
                .dependsOn("infor3", infor3_comboBox.valueProperty())
                .withMethod(context -> {
                    String input = context.get("infor3");
                    if (input == null) {
                        noneLabel.setText("Pets must be selected");
                        context.error("Pets must be selected");
                    }
                })
                .decorates(infor3_comboBox)
                .immediateClear();

        validatorRP.createCheck().dependsOn("infor4", infor4_comboBox.valueProperty())
                .withMethod(context -> {
                    String input = context.get("infor4");
                    if (input == null) {
                        noneLabel.setText("Garden must be selected");
                        context.error("Garden must be selected");
                    }
                })
                .decorates(infor4_comboBox)
                .immediateClear();
    }

    private void setOnActionButton(){
        create_btn.setOnAction(e -> createProperty());
        update_btn.setOnAction(e -> updateProperty());
        delete_btn.setOnAction(e -> deleteProperty());
        prev_btn.setOnAction(e -> prevImage());
        next_btn.setOnAction(e -> nextImage());
        addToDB_btn.setOnAction(e -> {
            if ((validatorCP.validate() && isValidSquareMeters()) || validatorRP.validate()) addToDB();
            clearTextFilled();
        });

        addToDB_btn.setVisible(false);
    }

    private boolean isValidSquareMeters() {
        try {
            double value = Double.parseDouble(infor3_comboBox.getValue().toString());
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void setUpTableBehavior(){
//        infor3_comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
//            squareMeters.set(newValue.toString());
//        });
        property_Tableview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedProperty.set(newValue);
            showProperty();
        });
        List<Owner> list = ModelCentral.getInstance().getAdminViewFactory().getAllOwner();
        owner_comboBox.setItems(FXCollections.observableArrayList(list));
        owner_comboBox.setCellFactory(param -> new ListCell<Owner>(){
            @Override
            protected void updateItem(Owner item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select Owner");
                } else {
                    setText("ID: " + item.getId() + " - " + item.getName());
                }
            }
        });
        owner_comboBox.setButtonCell(new ListCell<Owner>(){
            @Override
            protected void updateItem(Owner item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select Owner");
                } else {
                    setText("ID: " + item.getId() + " - " + item.getName());
                }
            }
        });



        selectedProperty.addListener((observable, oldValue, newValue) -> {
            setUpInfor(newValue);
        });
        propertyType_comboBox.getSelectionModel().selectedItemProperty().addListener((o, old, neww) -> {
            if(neww.equals(PropertyType.COMMERCIAL)){
                setUpInfor(new CommercialProperty());
                propertyType.setText("Commercial");
                create_btn.setVisible(true);
            }
            else if(neww.equals(PropertyType.RESIDENTIAL)){
                setUpInfor(new ResidentialProperty());
                propertyType.setText("Residential");
                create_btn.setVisible(true);
            }
            else{
                addToDB_btn.setVisible(false);
                create_btn.setVisible(false);
            }
        });



    }

    private void setUpInfor(Property property){
        if(property instanceof ResidentialProperty){
            infor1_label.setText("Rooms");
            infor2_label.setText("Bedrooms");
            infor3_label.setText("Pets");
            infor4_label.setText("Garden");

            infor3_comboBox.getItems().clear();
            infor4_comboBox.getItems().clear();
            infor3_comboBox.setEditable(false);
            infor4_label.setVisible(true);
            infor4_comboBox.setVisible(true);

            infor3_comboBox.getItems().addAll(true, false);
            infor4_comboBox.getItems().addAll(true, false);
        }
        else{
            infor1_label.setText("Business Type");
            infor2_label.setText("Parking Space");
            infor3_label.setText("Square Meters");
            infor4_label.setVisible(false);
            infor4_comboBox.setVisible(false);

            infor3_comboBox.getItems().clear();
            infor3_comboBox.setEditable(true);
        }
    }

    private void showProperty() {
        if(selectedProperty.get() == null) return;
        setEditable(false);
        id_input.setText(selectedProperty.get().getId() + "");
        address_input.setText(selectedProperty.get().addressPropertyProperty().get());
        priceInput.setText(selectedProperty.get().pricePropertyProperty().get() + "");
        owner_comboBox.setValue(selectedProperty.get().ownerPropertyProperty().get());
        status_comboBox.setValue(selectedProperty.get().statusPropertyProperty().get());

        Host_tableView.getItems().clear();
        Host_tableView.getItems().addAll(selectedProperty.get().getHosts());

        RA_tableView.getItems().clear();
        RA_tableView.getItems().addAll(selectedProperty.get().getAgreementList());


        if(selectedProperty.get() instanceof CommercialProperty){
            propertyType.setText("Commercial");
            CommercialProperty commercialProperty = (CommercialProperty) selectedProperty.get();
            infor1_label.setText("Business Type");
            infor1_input.setText(commercialProperty.businessTypePropertyProperty().get() + "");
            infor2_label.setText("Parking Space");
            infor2_input.setText(commercialProperty.totalParkingSpacePropertyProperty().get() + "");
            infor3_label.setText("Square Meters");
            infor3_comboBox.setValue(commercialProperty.squareMetersPropertyProperty().get() + "");
            infor4_label.setVisible(false);
            infor4_comboBox.setVisible(false);
        }
        else{
            propertyType.setText("Residential");
            ResidentialProperty residentialProperty = (ResidentialProperty) selectedProperty.get();
            infor1_label.setText("Rooms");
            infor1_input.setText(residentialProperty.totalRoomPropertyProperty().get() + "");
            infor2_label.setText("Bedrooms");
            infor2_input.setText(residentialProperty.totalBedroomPropertyProperty().get() + "");
            infor3_label.setText("Pets");
            infor3_comboBox.setValue(residentialProperty.isPetAllowedPropertyProperty().get());
            infor4_label.setVisible(true);
            infor4_comboBox.setVisible(true);
            infor4_label.setText("Garden");
            infor4_comboBox.setValue(residentialProperty.hasGardenPropertyProperty().get());
        }
    }

    private void setUpTableView(){
        property_Tableview.getColumns().addAll(
                createColumn("Type", "type"),
                createColumn("ID", "id"),
                createColumn("Address", "address"),
                createColumn("Price", "price")
        );
        property_Tableview.setItems(propertiesObservableList);
        propertiesObservableList.setAll(propertyList);

        Host_tableView.getColumns().addAll(
                createColumn("ID", "id"),
                createColumn("Name", "name")
        );

        RA_tableView.getColumns().addAll(
                createColumn("ID", "agreementId"),
                createColumn("Main Renter", "mainTenant",
                        ra -> ra.getMainTenant().namePropertyProperty().get())
        );

        status_comboBox.getItems().addAll(
                PropertyStatus.AVAILABLE,
                PropertyStatus.RENTED,
                PropertyStatus.UNAVAILABLE,
                PropertyStatus.UNDER_MAINTENANCE
        );

        propertyType_comboBox.getItems().addAll(
                PropertyType.COMMERCIAL,
                PropertyType.RESIDENTIAL,
                PropertyType.NONE
        );

        propertyStatus_comboBox.getItems().addAll(
                PropertyStatus.AVAILABLE,
                PropertyStatus.RENTED,
                PropertyStatus.UNAVAILABLE,
                PropertyStatus.UNDER_MAINTENANCE,
                PropertyStatus.NONE
        );
    }

    private void setEditable(boolean isEditable){
        id_input.setEditable(false);
        propertyType.setEditable(false);

        address_input.setEditable(isEditable);
        priceInput.setEditable(isEditable);
        owner_comboBox.setDisable(!isEditable);
        status_comboBox.setDisable(!isEditable);
        infor1_input.setEditable(isEditable);
        infor2_input.setEditable(isEditable);
        infor3_comboBox.setDisable(!isEditable);
        infor4_comboBox.setDisable(!isEditable);

    }

    private boolean isTexfieldChanged(Property property){
        if(property == null) return false;
        if(!property.addressPropertyProperty().get().equals(address_input.getText())) return true;
        if(property.pricePropertyProperty().get() != Double.parseDouble(priceInput.getText())) return true;
        if(property.ownerPropertyProperty().get() != owner_comboBox.getValue()) return true;
        if(property.statusPropertyProperty().get() != status_comboBox.getValue()) return true;
        if(property instanceof CommercialProperty){
            CommercialProperty commercialProperty = (CommercialProperty) property;
            if(!commercialProperty.businessTypePropertyProperty().get().equals(infor1_input.getText())) return true;
            if(commercialProperty.totalParkingSpacePropertyProperty().get() != Integer.parseInt(infor2_input.getText())) return true;
            if(commercialProperty.squareMetersPropertyProperty().get() != Double.parseDouble(infor3_comboBox.getValue().toString())) return true;
        }
        else{
            ResidentialProperty residentialProperty = (ResidentialProperty) property;
            if(residentialProperty.totalRoomPropertyProperty().get() != Integer.parseInt(infor1_input.getText())) return true;
            if(residentialProperty.totalBedroomPropertyProperty().get() != Integer.parseInt(infor2_input.getText())) return true;
            if(residentialProperty.isPetAllowedPropertyProperty().get() != Boolean.parseBoolean(infor3_comboBox.getValue().toString())) return true;
            if(residentialProperty.hasGardenPropertyProperty().get() != Boolean.parseBoolean(infor4_comboBox.getValue().toString())) return true;
        }
        return false;
    }

    private void nextImage() {
    }

    private void prevImage() {
    }

    private void deleteProperty() {
        Property property = selectedProperty.get();
        if(property == null) return;
        if(property.getAgreementList().size() > 0){
            System.out.println("Cannot delete property with agreement");
            return;
        }
        if(property.getHosts().size() > 0){
            System.out.println("Cannot delete property with host");
            return;
        }
        if(!ModelCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to delete this property?")) return;
        DAOInterface dao;
        if(property instanceof CommercialProperty){
            dao = new CommercialPropertyDAO();
        }
        else{
            dao = new ResidentialPropertyDAO();
        }
        boolean isDeleted = dao.delete(property);
        if(isDeleted){
            propertyList.remove(property);
            propertiesObservableList.setAll(propertyList);
            System.out.println("Deleted");
        }
        else System.out.println("Failed to delete");

    }

    private void updateProperty() {
        boolean isEditable = address_input.isEditable();
        setEditable(!isEditable);

        Property property = selectedProperty.get();
        if(property == null) return;
        if(!isTexfieldChanged(property) && !(validatorRP.validate() || validatorCP.validate())) return;
        if(!ModelCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to update this property?")) return;

        DAOInterface dao;
        if(property instanceof CommercialProperty){
            dao = new CommercialPropertyDAO();
        }
        else{
            dao = new ResidentialPropertyDAO();
        }
        property.setAddress(address_input.getText());
        property.setPrice(Double.parseDouble(priceInput.getText()));
        property.setOwner(owner_comboBox.getValue());
//        property.getOwner().addProperty(property);
        property.setStatus(status_comboBox.getValue());
        if(property instanceof CommercialProperty){
            CommercialProperty commercialProperty = (CommercialProperty) property;
            commercialProperty.setBusinessType(infor1_input.getText());
            commercialProperty.setTotalParkingSpace(Integer.parseInt(infor2_input.getText()));
            commercialProperty.setSquareMeters(Double.parseDouble(infor3_comboBox.getValue().toString()));
        }
        else{
            ResidentialProperty residentialProperty = (ResidentialProperty) property;
            residentialProperty.setTotalRoom(Integer.parseInt(infor1_input.getText()));
            residentialProperty.setTotalBedroom(Integer.parseInt(infor2_input.getText()));
            residentialProperty.setPetAllowed(Boolean.parseBoolean(infor3_comboBox.getValue().toString()));
            residentialProperty.setHasGarden(Boolean.parseBoolean(infor4_comboBox.getValue().toString()));
        }
        boolean isUpdated = dao.update(property);
        if(isUpdated){
            propertyList.set(propertyList.indexOf(property), property);
            propertiesObservableList.setAll(propertyList);
            System.out.println("Updated");
            List<Owner> owners = ModelCentral.getInstance().getAdminViewFactory().getAllOwner();
            int index = owners.indexOf(property.getOwner());
            //Update on owner
//            Owner owner = owners.get(index);
//            owner.addProperty(property);

        }
        else System.out.println("Failed to update");
    }

    private void createProperty() {
        clearTextFilled();
        setEditable(true);
        id_input.setText("Auto");
        addToDB_btn.setVisible(true);

        RA_tableView.getItems().clear();
        Host_tableView.getItems().clear();
    }

    private void warmUp() throws InterruptedException {
        DatabaseUtil.getSession();
        System.out.println("Session created");
        Thread.sleep(1000);
        try (Session session = DatabaseUtil.getSession()) {
            session.createNativeQuery("SELECT 1").getSingleResult();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addToDB() {
        if(propertyType_comboBox.getSelectionModel().getSelectedItem().equals(PropertyType.NONE)) System.out.println("Cannot add");
        if(propertyType_comboBox.getSelectionModel().getSelectedItem().equals(PropertyType.COMMERCIAL)){
            CommercialProperty newProperty = new CommercialProperty();
            newProperty.setAddress(address_input.getText());
            newProperty.setPrice(Double.parseDouble(priceInput.getText()));
            newProperty.setOwner(owner_comboBox.getValue());
//        property.getOwner().addProperty(property);
            newProperty.setStatus(status_comboBox.getValue());
            newProperty.setBusinessType(infor1_input.getText());
            newProperty.setTotalParkingSpace(Integer.parseInt(infor2_input.getText()));
            newProperty.setSquareMeters(Double.parseDouble(infor3_comboBox.getValue().toString()));

            if(!ModelCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to add this property?")) return;
            CommercialPropertyDAO dao = new CommercialPropertyDAO();
            try {
                warmUp();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            boolean isAdded = dao.add(newProperty);
            if(isAdded){
                propertyList.add(newProperty);
                propertiesObservableList.setAll(propertyList);
                System.out.println("Added");
            }
            else System.out.println("Failed to add");
        }
        else{
            ResidentialProperty newProperty = new ResidentialProperty();
            newProperty.setAddress(address_input.getText());
            newProperty.setPrice(Double.parseDouble(priceInput.getText()));
            newProperty.setOwner(owner_comboBox.getValue());
//        property.getOwner().addProperty(property);
            newProperty.setStatus(status_comboBox.getValue());
            newProperty.setTotalRoom(Integer.parseInt(infor1_input.getText()));
            newProperty.setTotalBedroom(Integer.parseInt(infor2_input.getText()));
            newProperty.setPetAllowed(Boolean.parseBoolean(infor3_comboBox.getValue().toString()));
            newProperty.setHasGarden(Boolean.parseBoolean(infor4_comboBox.getValue().toString()));
            if(!ModelCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to add this property?")) return;
            ResidentialPropertyDAO dao = new ResidentialPropertyDAO();
            boolean isAdded = dao.add(newProperty);
            if(isAdded){
                propertyList.add(newProperty);
                propertiesObservableList.setAll(propertyList);
                System.out.println("Added");
            }
            else System.out.println("Failed to add");
        }
    }

    private void clearTextFilled(){
        id_input.clear();
        address_input.clear();
        priceInput.clear();
        owner_comboBox.setValue(null);
        status_comboBox.setValue(null);
        infor1_input.clear();
        infor2_input.clear();
        infor3_comboBox.setValue(null);
        infor4_comboBox.setValue(null);
    }

    private <S, T> TableColumn<S, T> createColumn(String columnName, String propertyName) {
        TableColumn<S, T> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    private <S, T> TableColumn<S, T> createColumn(String columnName, String propertyName, Function<S, T> extractor) {
        TableColumn<S, T> column = new TableColumn<>(columnName);

        column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(extractor.apply(cellData.getValue())));
        column.setCellFactory(col -> new TableCell<S, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle(""); // Reset cell style for empty cells
                    setOnMouseClicked(null); // Remove any click listeners when the cell is empty
                } else {
                    setText(item.toString());
                }
            }
        });
        return column;
    }

    private void updateTotalNumbers() {
        try {
            totalNumberBedrooms = Integer.parseInt(infor2_input.getText());
        } catch (NumberFormatException e) {
            totalNumberBedrooms = 0; // Default to 0 if input is invalid
        }

        try {
            totalNumberRooms = Integer.parseInt(infor1_input.getText());
        } catch (NumberFormatException e) {
            totalNumberRooms = 0; // Default to 0 if input is invalid
        }

        // Optionally, you can update some UI elements or perform other actions based on the new totals
        System.out.println("Total Bedrooms: " + totalNumberBedrooms);
        System.out.println("Total Rooms: " + totalNumberRooms);
    }
}
