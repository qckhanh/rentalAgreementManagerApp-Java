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
import net.synedra.validatorfx.Validator;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.rmit.Helper.ImageUtils;
import org.rmit.Helper.InputValidator;
import org.rmit.Helper.UIDecorator;
import org.rmit.database.OwnerDAO;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Property.Property;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

public class OwnerManagerController implements Initializable {

    public TableView<Owner> persons_TableView;
    public Button create_btn;
    public Button update_btn;
    public Button delete_btn;
    public ImageView avatarImageView;
    public TextField id_input;
    public TextField fullName_input;
    public TextField contact_input;
    public DatePicker dob_input;
    public TableView<Host> cooperatingHost_TableView;
    public TableView<Property> propertiesOwned;
    public TextField username_input;
    public Button addToDB_btn;
    public PasswordTextField password_PasswordTextField;

    private ObservableList<Owner> personObservableList = FXCollections.observableArrayList();
    private ObjectProperty<Owner> selectedPerson = new SimpleObjectProperty<>();
    List<Owner> owners = ModelCentral.getInstance().getAdminViewFactory().getAllOwner();

    Label noneLabel = new Label();
    Validator validator = new Validator();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        decor();
        revealPassword(password_PasswordTextField);
        setEditableTextField(true);
//        avatarImageView.getStyleClass().addAll(
//                Styles.ROUNDED
//        );
        create_btn.setOnAction(e -> createNewPerson());
        update_btn.setOnAction(e -> {
            updatePerson();
        });
        delete_btn.setOnAction(e -> deletePerson());
        addToDB_btn.setOnAction(e -> {
            if(validator.validate()) addToDB();
            clearTextField();
        });
        addToDB_btn.setVisible(false);

        persons_TableView.getColumns().addAll(
                createColumn("ID", "id"),
                createColumn("Name", "name"),
                createColumn("PropertyNo", "propertiesOwned", person -> person.getPropertiesOwned().size()),
                createColumn("HostNo", "hosts", person -> person.getHosts().size())
        );
        cooperatingHost_TableView.getColumns().addAll(
                createColumn("ID", "id"),
                createColumn("Name", "name")
        );
        propertiesOwned.getColumns().addAll(
                createColumn("ID", "id"),
                createColumn("Address", "address"),
                createColumn("Type", "type")
        );

        persons_TableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedPerson.set(newValue);
            showInformation(newValue);
        });
        persons_TableView.setItems(personObservableList);
        loadData(owners);

        validateInput();
        addToDB_btn.disableProperty().bind(validator.containsErrorsProperty());
    }

    private void validateInput() {
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

        validator.createCheck()
                .dependsOn("username", username_input.textProperty())
                .withMethod(context -> {
                    String input = context.get("username");
                    if (!InputValidator.isValidUsername(input, noneLabel)) {
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
        if(!ModelCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to delete this owner?")) return;
        Owner person = selectedPerson.get();
        if(person.getPropertiesOwned().size() != 0){
            System.out.println("Cannot delete owner who have > 0 properties");
        }
        else {
            OwnerDAO dao = new OwnerDAO();
            boolean isDeleted = dao.delete(person);
            if(isDeleted){
                personObservableList.remove(person);
                clearTextField();
                System.out.println("Deleted owner");
            }
            else System.out.println("Cannot delete owner");
        }
    }

    private void updatePerson() {
        boolean isEditable = fullName_input.isEditable();
        setEditableTextField(!isEditable);
        if(isTextFieldChanged(selectedPerson.get()) && validator.validate()){
            if (!ModelCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to update this owner?")) return;
            Owner person = selectedPerson.get();
            person.setName(fullName_input.getText());
            person.setContact(contact_input.getText());
            person.setDateOfBirth(dob_input.getValue());
            person.setPassword(password_PasswordTextField.getPassword());
            OwnerDAO dao = new OwnerDAO();
            boolean isUpdated = dao.update(person);
            if (isUpdated) {
                    personObservableList.set(personObservableList.indexOf(person), person);
                    System.out.println("Updated owner");
                } else System.out.println("Cannot update owner");
            clearTextField();
        }


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
            OwnerDAO ownerDAO = new OwnerDAO();
            Owner person = new Owner();
            person.setUsername(username_input.getText());
            person.setName(fullName_input.getText());
            person.setContact(contact_input.getText());
            person.setDateOfBirth(dob_input.getValue());
            if (!ModelCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to create this owner?"))
                return;
            boolean isAdded = ownerDAO.add(person);
            if (isAdded) {
                personObservableList.add(person);
                System.out.println("Added Owner");
            } else System.out.println("Cannot add Owner");
    }

    private boolean isTextFieldChanged(Owner person){
        if(person == null) return false;
        if(!fullName_input.getText().equals(person.namePropertyProperty().get())) return true;
        else if(!contact_input.getText().equals(person.contactPropertyProperty().get())) return true;
        else if(!dob_input.getValue().equals(person.dateOfBirthPropertyProperty().get())) return true;
        else if(!password_PasswordTextField.getPassword().equals(person.getPassword())) return true;
        return false;
    }

    private void decor(){
        UIDecorator.setDangerButton(delete_btn, UIDecorator.DELETE, null);
        UIDecorator.setSuccessButton(create_btn, new FontIcon(Feather.PLUS_CIRCLE), null);
        UIDecorator.setSuccessButton(addToDB_btn, new FontIcon(Feather.PLUS_CIRCLE), "Save");
        UIDecorator.setNormalButton(update_btn, UIDecorator.EDIT, null);
    }

    private void clearTextField() {
        avatarImageView.setImage(ImageUtils.byteToImage(null));
        id_input.clear();
        username_input.clear();
        fullName_input.clear();
        contact_input.clear();
        dob_input.setValue(null);
        password_PasswordTextField.clear();
        cooperatingHost_TableView.getItems().clear();
        propertiesOwned.getItems().clear();
    }

    private void setEditableTextField(boolean isDisable){
        id_input.setEditable(false);
        username_input.setEditable(false);
        fullName_input.setEditable(isDisable);
        contact_input.setEditable(isDisable);
        dob_input.setEditable(isDisable);
        password_PasswordTextField.setEditable(isDisable);
    }

    private void showInformation(Owner person) {
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

        cooperatingHost_TableView.getItems().clear();
        cooperatingHost_TableView.getItems().addAll(FXCollections.observableArrayList(person.getHosts()));

        propertiesOwned.getItems().clear();
        propertiesOwned.setItems(FXCollections.observableArrayList(person.getPropertiesOwned()));
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

    private void loadData(List<Owner> list) {
        personObservableList.setAll(list);
    }

}
