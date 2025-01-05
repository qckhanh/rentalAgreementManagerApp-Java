package org.rmit.controller.Admin;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import net.synedra.validatorfx.Validator;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.rmit.Helper.ImageUtils;
import org.rmit.Helper.InputValidator;
import org.rmit.Helper.UIDecorator;
import org.rmit.database.AdminDAO;
import org.rmit.database.DAOInterface;
import org.rmit.database.OwnerDAO;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Admin;
import org.rmit.model.Persons.Person;
import org.rmit.model.Session;

import javax.naming.spi.InitialContextFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static org.rmit.Helper.UIDecorator.EDIT;

public class Admin_EditProfileController implements Initializable {
    public Label welcomeLabel;
    public TextField newName_input;
    public TextField newUsername_input;
    public TextField newContact_input;
    public DatePicker newDOB_input;
    public PasswordField newPassword_input;
    public Button edit_btn;
    public ImageView avatar_ImageView;
    public Button avatarUpdate_btn;
    public String SELECTED_PATH = ImageUtils.DEFAULT_IMAGE;
    public Label name_err;
    public Label username_err;
    public Label contact_err;
    public Label dob_err;
    public Label password_err;
    Validator validator = new Validator();
    Person currentUser = Session.getInstance().getCurrentUser();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        decorElement();
        innitText();
        addListener();
        setDisableAll(true);
        edit_btn.setDisable(false);
        edit_btn.setOnAction(event -> editProfile());
        avatarUpdate_btn.setOnAction(event -> updateAvatar());
        resetErrorLabels();
        validateInput();
    }

    private void innitText(){
        newName_input.setText(currentUser.getName());
        newContact_input.setText(currentUser.getContact());
        newDOB_input.setValue(currentUser.getDateOfBirth());
        newPassword_input.setText(currentUser.getPassword());
        newUsername_input.setText(currentUser.getUsername());
        avatar_ImageView.setImage(ImageUtils.byteToImage(currentUser.getProfileAvatar()));
        edit_btn.setText("Edit");
    }

    private void addListener(){
        newName_input.textProperty().addListener((observable, oldValue, newValue) -> checkForChanges());
        newContact_input.textProperty().addListener((observable, oldValue, newValue) -> checkForChanges());
        newDOB_input.valueProperty().addListener((observable, oldValue, newValue) -> checkForChanges());
        newPassword_input.textProperty().addListener((observable, oldValue, newValue) -> checkForChanges());
        newUsername_input.textProperty().addListener((observable, oldValue, newValue) -> checkForChanges());
    }

    private void decorElement(){
        UIDecorator.setDangerButton(edit_btn, new FontIcon(Feather.EDIT), "Edit");
        UIDecorator.buttonIcon(avatarUpdate_btn, EDIT);
    }

    private void validateInput() {
        validator.createCheck()
                .dependsOn("newName", newName_input.textProperty())
                .withMethod(context -> {
                    String input = context.get("newName");
                    if (!InputValidator.NoCondition(input, name_err)) {
                        context.error("Name must not be empty");
                    }
                })
                .decorates(newName_input)
                .immediateClear();

        validator.createCheck()
                .dependsOn("newUsername", newUsername_input.textProperty())
                .withMethod(context -> {
                    String input = context.get("newUsername");
                    if (!InputValidator.isValidUsername(input, username_err)) {
                        context.error("Username must be at least 6 characters");
                    }
                })
                .decorates(newUsername_input)
                .immediateClear();

        validator.createCheck()
                .dependsOn("newContact", newContact_input.textProperty())
                .withMethod(context -> {
                    String input = context.get("newContact");
                    if (!InputValidator.isValidContact(input, contact_err)) {
                        context.error("Invalid Phone Number");
                    }
                })
                .decorates(newContact_input)
                .immediateClear();

        validator.createCheck()
                .dependsOn("newDOB", newDOB_input.valueProperty())
                .withMethod(context -> {
                    LocalDate input = context.get("newDOB");
                    if (!InputValidator.isValidDateFormat(input, dob_err)) {
                        context.error("Invalid Date");
                    }
                })
                .decorates(newDOB_input)
                .immediateClear();

        validator.createCheck()
                .dependsOn("newPassword", newPassword_input.textProperty())
                .withMethod(context -> {
                    String input = context.get("newPassword");
                    if (!InputValidator.isValidPassword(input, password_err)) {
                        context.error("Password must be at least 8 characters");
                    }
                })
                .decorates(newPassword_input)
                .immediateClear();

        edit_btn.disableProperty().bind(validator.containsErrorsProperty());
    }

    private void editProfile(){
        if (edit_btn.getText().equals("Save")) {
            if (validator.validate()){
                // Invoke Confirm Message
                boolean confirmed = ModelCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to save changes?");
                if (confirmed) {
                    saveChanges();
                }
            }
        } else {
            setDisableAll(false);
        }
    }

    private void updateAvatar() {
        String SELECTED_PATH = ImageUtils.openFileChooseDialog();
        avatar_ImageView.setImage(ImageUtils.imageFromPath(SELECTED_PATH));
        edit_btn.setDisable(false);
    }

    private void saveChanges() {
//        // Save the changes to the currentUser object
        DAOInterface dao = new AdminDAO();
        currentUser.setName(newName_input.getText());
        currentUser.setUsername(newUsername_input.getText());
        currentUser.setContact(newContact_input.getText());
        currentUser.setDateOfBirth(newDOB_input.getValue());
        currentUser.setPassword(newPassword_input.getText());
        if(SELECTED_PATH != ImageUtils.DEFAULT_IMAGE) currentUser.setProfileAvatar(ImageUtils.getByte(SELECTED_PATH));
        dao.update((Admin) currentUser);

        // Reset fields and button:
        setDisableAll(true);
        edit_btn.setText("Edit");
        edit_btn.setDisable(false);
    }

    private void checkForChanges(){
        boolean isChanged = !newName_input.getText().equals(currentUser.getName()) ||
                !newContact_input.getText().equals(currentUser.getContact()) ||
                !newDOB_input.getValue().equals(currentUser.getDateOfBirth()) ||
                !newPassword_input.getText().equals(currentUser.getPassword()) ||
                !newUsername_input.getText().equals(currentUser.getUsername());

        if (isChanged){
            edit_btn.setText("Save");
            edit_btn.setDisable(false);
        }
        else {
            edit_btn.setText("Edit");
            setDisableAll(true);
            edit_btn.setDisable(false);
        }
    }

    private void setDisableAll(boolean value){
        newName_input.setDisable(value);
        newContact_input.setDisable(value);
        newDOB_input.setDisable(value);
        newPassword_input.setDisable(value);
        newUsername_input.setDisable(value);
        avatarUpdate_btn.setDisable(value);
    }

    private void resetErrorLabels(){
        name_err.setText("");
        username_err.setText("");
        contact_err.setText("");
        dob_err.setText("");
        password_err.setText("");
    }
}
