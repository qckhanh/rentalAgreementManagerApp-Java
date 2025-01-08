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
import org.hibernate.Session;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.Helper.ImageUtils;
import org.rmit.Helper.InputValidator;
import org.rmit.Helper.UIDecorator;
import org.rmit.database.RenterDAO;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Renter;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

public class RenterManagerController implements Initializable {
    public TableView<Renter> renters_tableView;
    public Button create_btn;
    public Button update_btn;
    public Button delete_btn;
    public ImageView avatarImageView;
    public TextField id_input;
    public TextField fullName_input;
    public TextField contact_input;
    public DatePicker dob_input;
    public TableView<RentalAgreement> RA_tableView;
    public TableView<Payment> payment_tableView;
    public TextField username_input;
    public Button addToDB_btn;
    public PasswordTextField password_PasswordTextField;

    private ObservableList<Renter> renterObservableList = FXCollections.observableArrayList();
    private ObjectProperty<Renter> selectedRenter = new SimpleObjectProperty<>();
    List<Renter> renters = ModelCentral.getInstance().getAdminViewFactory().getAllRenter();

    Validator validator = new Validator();
    Label noneLabel = new Label();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        decor();
        revealPassword(password_PasswordTextField);
        setEditableTextField(true);
//        avatarImageView.getStyleClass().addAll(
//                Styles.ROUNDED
//        );
        create_btn.setOnAction(e -> createNewPerson());
        update_btn.setOnAction(e -> updatePerson());
        delete_btn.setOnAction(e -> deletePerson());
        addToDB_btn.setOnAction(e -> {
            if (validator.validate()) addToDB();
            clearTextField();
        });
        addToDB_btn.setVisible(false);

        renters_tableView.getColumns().addAll(
                createColumn("ID", "id"),
                createColumn("Name", "name"),
                createColumn("AgreementNo", "agreementList",
                        r -> r.getAgreementList().size() + r.getSubAgreements().size()
                ),
                createColumn("PaymentsNo", "payments",
                        r -> r.getPayments().size()
                )
        );
        RA_tableView.getColumns().addAll(
                createColumn("ID", "agreementId"),
                createColumn("Address", "property",
                        ra -> ra.getProperty().addressPropertyProperty().get()),
                createColumn("Main Renter", "mainTenant",
                        ra -> ra.getMainTenant().namePropertyProperty().get())
        );
        payment_tableView.getColumns().addAll(
                createColumn("ID", "paymentId"),
                createColumn("AgreID", "rentalAgreement",
                        p -> p.getRentalAgreement().getAgreementId()),
                createColumn("Date", "date")
        );

        renters_tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedRenter.set(newValue);
            showInformation(newValue);
        });
        renters_tableView.setItems(renterObservableList);
        loadData(renters);

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
                    }
                })
                .decorates(password_PasswordTextField)
                .immediateClear();
    }

    private void deletePerson() {
        if(!ModelCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to delete this renter?")) return;
        Renter renter = selectedRenter.get();
        if(renter.getAgreementList().size() != 0 || renter.getSubAgreements().size() != 0){
            System.out.println("Cannot delete renter with (sub)agreement");
        }
        else {
            RenterDAO renterDAO = new RenterDAO();
            DatabaseUtil.warmUp();
            boolean isDeleted = renterDAO.delete(renter);
            if(isDeleted){
                renterObservableList.remove(renter);
                clearTextField();
                System.out.println("Deleted renter");
            }
            else System.out.println("Cannot delete renter");
        }
    }

    private void updatePerson() {
        boolean isEditable = fullName_input.isEditable();
        setEditableTextField(!isEditable);
        if(isTextFieldChanged(selectedRenter.get()) && validator.validate()){
            if(!ModelCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to update this renter?")) return;
            Renter renter = selectedRenter.get();
            renter.setName(fullName_input.getText());
            renter.setContact(contact_input.getText());
            renter.setDateOfBirth(dob_input.getValue());
            renter.setPassword(password_PasswordTextField.getPassword());
            RenterDAO renterDAO = new RenterDAO();
            DatabaseUtil.warmUp();
            boolean isUpdated = renterDAO.update(renter);
            if(isUpdated){
                renterObservableList.set(renterObservableList.indexOf(renter), renter);
                System.out.println("Updated renter");
            }
            else System.out.println("Cannot update renter");
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

    private void warmUp(){
        try (Session session = DatabaseUtil.getSession()){
            session.createNativeQuery("SELECT 1").getSingleResult();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addToDB() {
        RenterDAO renterDAO = new RenterDAO();
        Renter renter = new Renter();
        renter.setUsername(username_input.getText());
        renter.setName(fullName_input.getText());
        renter.setContact(contact_input.getText());
        renter.setDateOfBirth(dob_input.getValue());
        if(!ModelCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to create this renter?")) return;
        warmUp();
        DatabaseUtil.warmUp();
        boolean isAdded = renterDAO.add(renter);
        if(isAdded){
            renterObservableList.add(renter);
            System.out.println("Added renter");
        }
        else System.out.println("Cannot add renter");
    }

    private boolean isTextFieldChanged(Renter renter){
        if(renter == null) return false;
        if(!fullName_input.getText().equals(renter.namePropertyProperty().get())) return true;
        else if(!contact_input.getText().equals(renter.contactPropertyProperty().get())) return true;
        else if(!dob_input.getValue().equals(renter.dateOfBirthPropertyProperty().get())) return true;
        else if(!password_PasswordTextField.getPassword().equals(renter.getPassword())) return true;
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
        RA_tableView.getItems().clear();
        payment_tableView.getItems().clear();
        password_PasswordTextField.clear();
    }

    private void setEditableTextField(boolean isDisable){
        id_input.setEditable(false);
        username_input.setEditable(false);
        fullName_input.setEditable(isDisable);
        contact_input.setEditable(isDisable);
        dob_input.setEditable(isDisable);
        password_PasswordTextField.setEditable(isDisable);
    }

    private void showInformation(Renter newValue) {
        if(newValue == null) return;
        setEditableTextField(false);

        avatarImageView.setImage(ImageUtils.byteToImage(newValue.getProfileAvatar()));
        username_input.setText(newValue.getUsername());
        addToDB_btn.setVisible(false);
        id_input.setText(newValue.getId() + "");
        fullName_input.setText(newValue.getName());
        contact_input.setText(newValue.getContact());
        dob_input.setValue(newValue.getDateOfBirth());
        password_PasswordTextField.setText(newValue.getPassword());

        RA_tableView.getItems().clear();
        RA_tableView.getItems().addAll(FXCollections.observableArrayList(newValue.getAgreementList()));
        RA_tableView.getItems().addAll(FXCollections.observableArrayList(newValue.getSubAgreements()));

        payment_tableView.getItems().clear();
        payment_tableView.setItems(FXCollections.observableArrayList(newValue.getPayments()));
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

    private void loadData(List<Renter> renterList) {
        renterObservableList.setAll(renterList);
    }
}
