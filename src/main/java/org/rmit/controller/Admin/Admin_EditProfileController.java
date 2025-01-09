package org.rmit.controller.Admin;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import net.synedra.validatorfx.Validator;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.Helper.ImageUtils;
import org.rmit.Helper.InputValidator;
import org.rmit.Helper.UIDecorator;
import org.rmit.database.AdminDAO;
import org.rmit.database.DAOInterface;
import org.rmit.database.RenterDAO;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Admin;
import org.rmit.model.Persons.Person;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Session;
import org.rmit.view.Start.NOTIFICATION_TYPE;

import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class Admin_EditProfileController implements Initializable {
    public TextField newName_input;
    public TextField newContact_input;
    public DatePicker newDOB_input;
    public PasswordField newPassword_input;
    public Button edit_btn;
    public TextField newUsername_input;
    public ImageView avatar_ImageView;
    public Button avatarUpdate_btn;
    public String SELECTED_PATH = ImageUtils.DEFAULT_IMAGE;

    public Label name_err;
    public Label username_err;
    public Label contact_err;
    public Label dob_err;
    public Label password_err;
    public boolean isAvatarChange = false;
    public AnchorPane anchorPane;

    Person currentUser = Session.getInstance().getCurrentUser();

    Validator validator = new Validator();

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
        newName_input.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) name_err.setText("");
            checkForChanges();
        });
        newContact_input.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) contact_err.setText("");
            checkForChanges();
        });
        newDOB_input.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!Objects.equals(oldValue, newValue)) dob_err.setText("");
            checkForChanges();
        });
        newPassword_input.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) password_err.setText("");
            checkForChanges();
        });
        newUsername_input.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) username_err.setText("");
            checkForChanges();
        });
        currentUser.profileAvatarPropertyProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(ImageUtils.getByte(ImageUtils.DEFAULT_IMAGE))) return;
            avatar_ImageView.setImage(ImageUtils.byteToImage(newValue));
            checkForChanges();
        });
    }

    private void decorElement(){
        UIDecorator.setDangerButton(edit_btn, new FontIcon(Feather.EDIT), "Edit");
        UIDecorator.buttonIcon(avatarUpdate_btn, UIDecorator.EDIT());
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
                    if (!InputValidator.isValidNewUsername(input, username_err, newUsername_input.equals(currentUser.getUsername()))) {
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
            // Validate before saving
            if (validator.validate()) {
                // Invoke confirm message
                boolean confirmed = ModelCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to save changes?");
                if (confirmed) {
                    saveChanges();
                }
            }
            else{
                ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.WARNING, anchorPane, "Please fill in all fields correctly");
            }
        } else {
            ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "You can now edit your profile");
            setDisableAll(false);
        }
    }

    private void updateAvatar() {
        SELECTED_PATH = ImageUtils.openFileChooseDialog();
        if(SELECTED_PATH == ImageUtils.DEFAULT_IMAGE){
            ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "No image selected. Image must be less than 1MB");
            return;
        }
        isAvatarChange = true;
        currentUser.setProfileAvatar(ImageUtils.getByte(SELECTED_PATH));
    }

    private void saveChanges() {
        // Save the changes to the currentUser object
        DAOInterface dao = new AdminDAO();

        currentUser.setName(newName_input.getText());
        currentUser.setUsername(newUsername_input.getText());
        currentUser.setContact(newContact_input.getText());
        currentUser.setDateOfBirth(newDOB_input.getValue());
        currentUser.setPassword(newPassword_input.getText());
        if(SELECTED_PATH != ImageUtils.DEFAULT_IMAGE) currentUser.setProfileAvatar(ImageUtils.getByte(SELECTED_PATH));

        boolean isUpdated =  dao.update((Admin)currentUser);
        if(isUpdated){
            ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Profile updated successfully");
        } else {
            ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Profile update failed. Try again");
        }
        setDisableAll(true);
        edit_btn.setText("Edit");
        edit_btn.setDisable(false);
        isAvatarChange = false;
    }

    private void checkForChanges() {
        boolean isChanged =
                !newName_input.getText().equals(currentUser.getName()) ||
                        !newContact_input.getText().equals(currentUser.getContact()) ||
                        !Objects.equals(newDOB_input.getValue(), currentUser.getDateOfBirth()) || // Null-safe comparison
                        !newPassword_input.getText().equals(currentUser.getPassword()) ||
                        !newUsername_input.getText().equals(currentUser.getUsername()) ||
                        !(avatar_ImageView.getImage().equals(currentUser.getProfileAvatar()) && !isAvatarChange);

        if (isChanged) {
            edit_btn.setText("Save");
            edit_btn.setDisable(false);
        } else {
            edit_btn.setText("Edit");
            setDisableAll(true);
            edit_btn.setDisable(false);
        }
    }

    void setDisableAll(boolean status){
        newName_input.setDisable(status);
        newContact_input.setDisable(status);
        newDOB_input.setDisable(status);
        newPassword_input.setDisable(status);
        newUsername_input.setDisable(status);
        avatarUpdate_btn.setDisable(status);
        if (edit_btn.disableProperty().isBound()) {
            edit_btn.disableProperty().unbind();
        }
        edit_btn.setDisable(status);
    }

    private void resetErrorLabels(){
        name_err.setText("");
        username_err.setText("");
        contact_err.setText("");
        dob_err.setText("");
        password_err.setText("");
    }
}
