package org.rmit.controller.Admin;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.rmit.Helper.ImageUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.database.DAOInterface;
import org.rmit.database.OwnerDAO;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Person;
import org.rmit.model.Session;

import javax.naming.spi.InitialContextFactory;
import java.net.URL;
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
            if (!newValue.equals(currentUser.getName())) {
                edit_btn.setDisable(false);
            }
        });
        newContact_input.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(currentUser.getContact())) {
                edit_btn.setDisable(false);
            }
        });
        newDOB_input.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(currentUser.getDateOfBirth())) {
                edit_btn.setDisable(false);
            }
        });
        newPassword_input.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(currentUser.getPassword())) {
                edit_btn.setDisable(false);
            }
        });
        newUsername_input.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(currentUser.getUsername())) {
                edit_btn.setDisable(false);
            }
        });
    }

    private void decorElement(){
        UIDecorator.setDangerButton(edit_btn, new FontIcon(Feather.EDIT), "Edit");
        UIDecorator.buttonIcon(avatarUpdate_btn, EDIT);
    }

    private void editProfile(){
        if (edit_btn.getText().equals("Save")) {
            // Invoke Confirm Message
            boolean confirmed = ModelCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to save changes?");
            if (confirmed) {
                saveChanges();
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
//        DAOInterface dao = new OwnerDAO();
//
//        currentUser.setName(newName_input.getText());
//        currentUser.setUsername(newUsername_input.getText());
//        currentUser.setContact(newContact_input.getText());
//        currentUser.setDateOfBirth(newDOB_input.getValue());
//        currentUser.setPassword(newPassword_input.getText());
//        if (!SELECTED_PATH.equals(ImageUtils.DEFAULT_IMAGE)) {
//            currentUser.setProfileAvatar(ImageUtils.imageToByte(SELECTED_PATH));
//        }
//
//        dao.update((Admin)currentUser);
//
//        // Reset fields and button:
//        setDisableAll(true);
//        edit_btn.setText("Edit");
//        edit_btn.setDisable(false);
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
}
