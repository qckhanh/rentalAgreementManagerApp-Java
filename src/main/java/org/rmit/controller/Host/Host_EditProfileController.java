package org.rmit.controller.Host;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.rmit.database.HostDAO;
import org.rmit.database.RenterDAO;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Person;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Session;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Host_EditProfileController implements Initializable {
    public TextField newName_input;
    public TextField newContact_input;
    public DatePicker newDOB_input;
    public PasswordField newPassword_input;
    public Button edit_btn;

    Person currentUser = Session.getInstance().getCurrentUser();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set default values from current user
        newName_input.setText(currentUser.getName());
        newContact_input.setText(currentUser.getContact());
        newDOB_input.setValue(currentUser.getDateOfBirth());
        newPassword_input.setText(currentUser.getPassword());
        // Add listeners to monitor changes
        newName_input.textProperty().addListener((observable, oldValue, newValue) -> checkForChanges());
        newContact_input.textProperty().addListener((observable, oldValue, newValue) -> checkForChanges());
        newDOB_input.valueProperty().addListener((observable, oldValue, newValue) -> checkForChanges());
        newPassword_input.textProperty().addListener((observable, oldValue, newValue) -> checkForChanges());

        // Initially disable all fields
        setDisableAll(true);
        edit_btn.setText("Edit");
        edit_btn.setDisable(false);
        edit_btn.setOnAction(event -> editProfile());
    }

    private void editProfile(){
        if (edit_btn.getText().equals("Save")) {
            // Invoke confirm message
            boolean confirmed = ModelCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to save changes?");
            if (confirmed) {
                saveChanges();
            }
        } else {
            setDisableAll(false);
        }
    }

    private void saveChanges() {
        // Save the changes to the currentUser object
        currentUser.setName(newName_input.getText());
        currentUser.setContact(newContact_input.getText());
        currentUser.setDateOfBirth(newDOB_input.getValue());
        currentUser.setPassword(newPassword_input.getText());

        HostDAO dao = new HostDAO();
        dao.update((Host)currentUser);

        // Reset fields and button
        setDisableAll(true);
        edit_btn.setText("Edit");
        edit_btn.setDisable(false);
    }

    private void checkForChanges() {
        boolean isChanged =
                !newName_input.getText().equals(currentUser.getName()) ||
                        !newContact_input.getText().equals(currentUser.getContact()) ||
                        !Objects.equals(newDOB_input.getValue(), currentUser.getDateOfBirth()) || // Null-safe comparison
                        !newPassword_input.getText().equals(currentUser.getPassword());

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
        edit_btn.setDisable(status);
    }
}
