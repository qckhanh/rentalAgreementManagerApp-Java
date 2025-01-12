package org.rmit.controller.Admin;

import atlantafx.base.controls.PasswordTextField;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import net.synedra.validatorfx.Validator;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.Helper.ImageUtils;
import org.rmit.Helper.InputValidator;
import org.rmit.Helper.UIDecorator;
import org.rmit.database.HostDAO;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.view.ViewCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Property.Property;
import org.rmit.view.Start.NOTIFICATION_TYPE;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

public class HostManagerController implements Initializable {
    public TableView<Host> persons_TableView;
    public Button create_btn;
    public Button update_btn;
    public Button delete_btn;
    public ImageView avatarImageView;
    public TextField id_input;
    public TextField fullName_input;
    public TextField contact_input;
    public DatePicker dob_input;
    public TableView<Owner> cooperatingOwner_TableView;
    public TableView<Property> propertiesManaging_tableView;
    public TableView<RentalAgreement> RA_tableView;

    public TextField username_input;
    public Button addToDB_btn;
    public PasswordTextField password_PasswordTextField;
    public AnchorPane anchorPane;

    private ObservableList<Host> personObservableList = FXCollections.observableArrayList();
    private ObjectProperty<Host> selectedPerson = new SimpleObjectProperty<>();
    List<Host> persons = ViewCentral.getInstance().getAdminViewFactory().getAllHost();

    Label noneLabel = new Label();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        decor();
        revealPassword(password_PasswordTextField);
        setEditableTextField(true);
        create_btn.setOnAction(e -> createNewPerson());
        update_btn.setOnAction(e -> updatePerson());
        delete_btn.setOnAction(e -> deletePerson());
        addToDB_btn.setOnAction(e -> {
            addToDB();
            clearTextField();
        });

        addToDB_btn.setVisible(false);

        persons_TableView.getColumns().addAll(
                createColumn("ID", "id"),
                createColumn("Name", "name"),
                createColumn("PropertyNo", "propertiesOwned", person -> person.getPropertiesManaged().size()),
                createColumn("OwnerNo", "hosts", person -> person.getCooperatingOwners().size()),
                createColumn("AgreementNo", "rentalAgreements", person -> person.getRentalAgreements().size())
        );
        cooperatingOwner_TableView.getColumns().addAll(
                createColumn("ID", "id"),
                createColumn("Name", "name")
        );
        propertiesManaging_tableView.getColumns().addAll(
                createColumn("ID", "id"),
                createColumn("Address", "address"),
                createColumn("Type", "type")
        );
        RA_tableView.getColumns().addAll(
                createColumn("ID", "agreementId"),
                createColumn("Address", "property",
                        ra -> ra.getProperty().addressPropertyProperty().get()),
                createColumn("Main Renter", "mainTenant",
                        ra -> ra.getMainTenant().namePropertyProperty().get())
        );

        persons_TableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedPerson.set(newValue);
            showInformation(newValue);
        });
        persons_TableView.setItems(personObservableList);
        loadData(persons);

    }

    private void buildValidtor(Validator validator, boolean isForCreateNew) {
        validator.createCheck()
                .dependsOn("fullName", fullName_input.textProperty())
                .withMethod(context -> {
                    String input = context.get("fullName");
                    if (!InputValidator.NoCondition(input, noneLabel)) {
                        context.error("Full name must not be empty");
                        System.out.println("Full name must not be empty");
                    }
                })
                .decorates(fullName_input)
                .immediateClear();

        validator.createCheck()
                .dependsOn("contact", contact_input.textProperty())
                .withMethod(context -> {
                    String input = context.get("contact");
                    if (!InputValidator.isValidContact(input, noneLabel)) {
                        context.error("Contact must be a valid email or phone number");
                        System.out.println("Contact must be a valid email or phone number");
                    }
                })
                .decorates(contact_input)
                .immediateClear();

        validator.createCheck()
                .dependsOn("dob", dob_input.valueProperty())
                .withMethod(context -> {
                    LocalDate input = context.get("dob");
                    if (!InputValidator.isValidDateFormat(input, noneLabel)) {
                        context.error("Date of birth must be a valid date before today");
                        System.out.println("Date of birth must be a valid date before today");
                    }
                })
                .decorates(dob_input)
                .immediateClear();
            System.out.println("Creating new person");
            validator.createCheck()
                    .dependsOn("username", username_input.textProperty())
                    .withMethod(context -> {
                        String input = context.get("username");
                        if (!InputValidator.isValidNewUsername(input, noneLabel, isForCreateNew)) {
                            context.error("Username must be at least 6 characters");
                            System.out.println("Username must be at least 6 characters");
                        }
                    })
                    .decorates(username_input)
                    .immediateClear();

        validator.createCheck()
                .dependsOn("password", password_PasswordTextField.textProperty())
                .withMethod(context -> {
                    String input = context.get("password");
                    if (!InputValidator.isValidPassword(input, noneLabel)) {
                        context.error("Password must be at least 8 characters");
                        System.out.println("Password must be at least 8 characters");
                    }
                })
                .decorates(password_PasswordTextField)
                .immediateClear();
    }

    private void deletePerson() {
        Host person = selectedPerson.get();
        if(person == null){
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.WARNING, anchorPane, "Please select a host to delete");
            return;
        }
        if(person.getPropertiesManaged().size() > 0){
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Cannot delete host. Host is managing properties");
            return;
        }
        if(!ViewCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to delete this host?")) return;
        HostDAO dao = new HostDAO();
        DatabaseUtil.warmUp();
        boolean isDeleted = dao.delete(person);
        if(isDeleted){
            personObservableList.remove(person);
            clearTextField();
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Host deleted successfully");
        }
        else ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Cannot delete host. Try again");

    }

    private void updatePerson() {
        boolean isEditable = fullName_input.isEditable();
        setEditableTextField(!isEditable);

        Validator updateValidator = new Validator();
        buildValidtor(updateValidator, false);

        if(!isTextFieldChanged(selectedPerson.get())){
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.WARNING, anchorPane, "No changes detected");
            return;
        }
        if(!updateValidator.validate()){
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.WARNING, anchorPane, "Please fill in all fields correctly");
            return;
        }

        if(!ViewCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to update this host?")) return;
        Host person = selectedPerson.get();
        person.setName(fullName_input.getText());
        person.setContact(contact_input.getText());
        person.setDateOfBirth(dob_input.getValue());
        person.setPassword(password_PasswordTextField.getPassword());
        HostDAO dao = new HostDAO();
        DatabaseUtil.warmUp();
        boolean isUpdated = dao.update(person);
        if(isUpdated){
            personObservableList.set(personObservableList.indexOf(person), person);
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Host updated successfully");
        }
        else ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Cannot update host. Try again");
    }

    private void createNewPerson() {
        clearTextField();
        setEditableTextField(true);
        username_input.setEditable(true);
        addToDB_btn.setVisible(true);
        id_input.setText("AUTO");
        if(username_input.getText().isBlank()) return;
        if(fullName_input.getText().isBlank()) return;
        if(contact_input.getText().isBlank()) return;
        if(dob_input.getValue() == null) return;
    }

    private void addToDB() {
        Validator validatorCreate = new Validator();
        buildValidtor(validatorCreate, true);
        if (!validatorCreate.validate()){
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.WARNING, anchorPane, "Please fill in all fields correctly");
            return;
        }
        if(!ViewCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to create this host?")) return;

        HostDAO dao = new HostDAO();
        Host person = new Host();
        person.setUsername(username_input.getText());
        person.setName(fullName_input.getText());
        person.setContact(contact_input.getText());
        person.setDateOfBirth(dob_input.getValue());
        DatabaseUtil.warmUp();
        boolean isAdded = dao.add(person);
        if(isAdded){
            personObservableList.add(person);
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Host added successfully");
        }
        else ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Cannot add host. Try again");
    }

    private boolean isTextFieldChanged(Host person){
        if(person == null) return false;
        if(!fullName_input.getText().equals(person.namePropertyProperty().get())) return true;
        else if(!contact_input.getText().equals(person.contactPropertyProperty().get())) return true;
        else if(!dob_input.getValue().equals(person.dateOfBirthPropertyProperty().get())) return true;
        else if(!password_PasswordTextField.getPassword().equals(person.getPassword())) return true;
        return false;
    }

    private void decor(){
        UIDecorator.setDangerButton(delete_btn, UIDecorator.DELETE(), null);
        UIDecorator.setSuccessButton(create_btn, UIDecorator.ADD(), null);
        UIDecorator.setSuccessButton(addToDB_btn, UIDecorator.ADD(), "Save");
        UIDecorator.setNormalButton(update_btn, UIDecorator.EDIT(), null);
        UIDecorator.setSuccessButton(addToDB_btn, UIDecorator.SEND(), null);

    }

    private void clearTextField() {
        avatarImageView.setImage(ImageUtils.byteToImage(null));
        id_input.clear();
        username_input.clear();
        fullName_input.clear();
        contact_input.clear();
        dob_input.setValue(null);
        password_PasswordTextField.clear();
        cooperatingOwner_TableView.getItems().clear();
        propertiesManaging_tableView.getItems().clear();
    }

    private void setEditableTextField(boolean isDisable){
        id_input.setEditable(false);
        username_input.setEditable(false);
        fullName_input.setEditable(isDisable);
        contact_input.setEditable(isDisable);
        dob_input.setEditable(isDisable);
        password_PasswordTextField.setEditable(isDisable);
    }

    private void showInformation(Host person) {
        if(person == null) return;
        setEditableTextField(false);

        avatarImageView.setImage(ImageUtils.byteToImage(person.getProfileAvatar()));
        username_input.setText(person.getUsername());
        addToDB_btn.setVisible(false);
        id_input.setText(person.getId() + "");
        fullName_input.setText(person.getName());
        contact_input.setText(person.getContact());
        dob_input.setValue(person.getDateOfBirth());
        password_PasswordTextField.setText(person.getPassword());

        cooperatingOwner_TableView.getItems().clear();
        cooperatingOwner_TableView.getItems().addAll(FXCollections.observableArrayList(person.getCooperatingOwners()));

        propertiesManaging_tableView.getItems().clear();
        propertiesManaging_tableView.setItems(FXCollections.observableArrayList(person.getPropertiesManaged()));

        RA_tableView.getItems().clear();
        RA_tableView.setItems(FXCollections.observableArrayList(person.getRentalAgreements()));
    }

    private void revealPassword(PasswordTextField passwordTextField){
        FontIcon icon = new FontIcon(Feather.EYE_OFF);
        icon.setCursor(Cursor.HAND);
        icon.setOnMouseClicked(e -> {
            icon.setIconCode(passwordTextField.getRevealPassword()
                    ? Feather.EYE_OFF : Feather.EYE
            );
            passwordTextField.setRevealPassword(!passwordTextField.getRevealPassword());
        });
        passwordTextField.setRight(icon);
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

    private void loadData(List<Host> list) {
        personObservableList.setAll(list);
    }
}
