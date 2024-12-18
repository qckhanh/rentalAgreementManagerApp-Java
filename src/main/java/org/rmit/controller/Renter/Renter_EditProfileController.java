package org.rmit.controller.Renter;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.rmit.model.Persons.Person;
import org.rmit.model.Session;

import javax.swing.event.ChangeListener;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Renter_EditProfileController implements Initializable {
    public TextField newName_input;
    public TextField newContact_input;
    public DatePicker newDOB_input;
    public PasswordField newPassword_input;
    public Button edit_btn;
    Person currentUser = Session.getInstance().getCurrentUser();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newName_input.setText(currentUser.getName());
        newContact_input.setText(currentUser.getContact());
        newDOB_input.setValue(currentUser.getDateOfBirth());
        newPassword_input.setText(currentUser.getPassword());


        newName_input.setDisable(true);
        newContact_input.setDisable(true);
        newDOB_input.setDisable(true);
        newPassword_input.setDisable(true);
        edit_btn.setOnAction(e -> editProfile());

        newName_input.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(currentUser.getName())) {
                edit_btn.setText("Save");
            } else {
                edit_btn.setDisable(true);
                edit_btn.setText("Edit");
            }
        });

        BooleanProperty change = new SimpleBooleanProperty();

    }

    private void editProfile(){
        newName_input.setDisable(false);
        newContact_input.setDisable(false);
        newDOB_input.setDisable(false);
        newPassword_input.setDisable(false);

        if(!newName_input.getText().equals(currentUser.getName()) ||
                !newContact_input.getText().equals(currentUser.getContact()) ||
                !newDOB_input.getValue().equals(currentUser.getDateOfBirth()) ||
                !newPassword_input.getText().equals(currentUser.getPassword()))
        {
            System.out.println("Hello");
        }
    }

    private void checkForChanges() {
        boolean isChanged =
                !newName_input.getText().equals(currentUser.getName()) ||
                        !newContact_input.getText().equals(currentUser.getContact()) ||
                        !Objects.equals(newDOB_input.getValue(), currentUser.getDateOfBirth()) || // Handles null-safe comparison for LocalDate
                        !newPassword_input.getText().equals(currentUser.getPassword());

        if (isChanged) {
            edit_btn.setText("Save");
            edit_btn.setDisable(false);
        } else {
            edit_btn.setText("Edit");
            edit_btn.setDisable(true);
        }
    }
}
