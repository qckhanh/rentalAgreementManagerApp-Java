package org.rmit.controller.Owner;

import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import org.rmit.database.OwnerDAO;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Person;
import org.rmit.model.Session;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Owner_EditProfileController implements Initializable {
    public TextField newName_input;
    public TextField newContact_input;
    public DatePicker newDOB_input;
    public PasswordField newPassword_input;
    public Button edit_btn;

    Person currentUser = Session.getInstance().getCurrentUser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newName_input.setText(currentUser.getName());
        newContact_input.setText(currentUser.getContact());
        newDOB_input.setValue(currentUser.getDateOfBirth());
        newPassword_input.setText(currentUser.getPassword());

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

    private void editProfile() {
        System.out.println("hallo");
        if (edit_btn.getText().equals("Save")) {
            boolean confirmed = ModelCentral.getInstance().getStartViewFactory().confirmMessage("Save changes?");
            if (confirmed) {
                saveChanges();
            }
        }
        else {
            setDisableAll(false);
        }
    }

    private void saveChanges() {
        currentUser.setName(newName_input.getText());
        currentUser.setContact(newContact_input.getText());
        currentUser.setDateOfBirth(newDOB_input.getValue());
        currentUser.setPassword(newPassword_input.getText());

        OwnerDAO ownerDAO = new OwnerDAO();
        ownerDAO.update((Owner) currentUser);

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
