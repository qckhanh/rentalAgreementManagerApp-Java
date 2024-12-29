package org.rmit.controller.Start;

import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.rmit.Helper.InputValidator;
import org.rmit.database.HostDAO;
import org.rmit.database.OwnerDAO;
import org.rmit.database.RenterDAO;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Person;
import org.rmit.model.Persons.Renter;
import org.rmit.view.Start.ACCOUNT_TYPE;

import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.rmit.database.DAOInterface.isValidUsername;

public class RegisterController implements Initializable {
    public TextField fullName_input;
    public PasswordField password_input;
    public Button submitRegister_btn;
    public Button backToLogin_btn;
    public TextField username_input;
    public TextField contact_input;
    public PasswordField rePassword_input;
    public DatePicker dob_datePicker;
    public ChoiceBox<ACCOUNT_TYPE> typeOfUser_choiceBox;

    public Label name_err;
    public Label username_err;
    public Label contact_err;
    public Label dob_err;
    public Label password_err;
    public Label repass_err;

    int registerAttempt = 0;
    int MAX_ATTEMPT = 4;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resetTextFields();
        resetLabel();
        typeOfUser_choiceBox.setValue(ACCOUNT_TYPE.RENTER);
        typeOfUser_choiceBox.setItems(FXCollections.observableArrayList(
                ACCOUNT_TYPE.RENTER,
                ACCOUNT_TYPE.HOST,
                ACCOUNT_TYPE.OWNER)
        );
        backToLogin_btn.setOnAction(actionEvent -> openLogin());
        submitRegister_btn.setOnAction(e -> register());

        username_input.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue)) username_err.setText("");
        });
        fullName_input.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue)) name_err.setText("");
        });
        contact_input.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue)) contact_err.setText("");
        });
        password_input.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue)) password_err.setText("");
        });
        rePassword_input.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue)) repass_err.setText("");
        });
        dob_datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(!Objects.equals(oldValue, newValue)) dob_err.setText("");
        });

    }

    void openLogin() {
        resetLabel();
        ModelCentral.getInstance().getStartViewFactory().showLoginView();
    }

    private boolean validateInput(){
        boolean isValid = true;
        if(!InputValidator.NoCondition(fullName_input.getText(), name_err)){
            isValid = false;
            System.out.println("Name is invalid");
        }
        if(!InputValidator.isValidUsername(username_input.getText(), username_err)){
            isValid = false;
            System.out.println("Username is invalid");
        }
        if(!InputValidator.isValidContact(contact_input.getText(), contact_err)){
            isValid = false;
            System.out.println("Contact is invalid");
        }
        if(!InputValidator.isValidDateFormat(dob_datePicker.getValue(), dob_err)){
            isValid = false;
            System.out.println("Date is invalid");
        }
        if(!InputValidator.isValidPassword(password_input.getText(), password_err)){
            isValid = false;
            System.out.println("Password is invalid");
        }
        if(!InputValidator.isValidPassword(rePassword_input.getText(), repass_err)){
            isValid = false;
            System.out.println("RePassword is invalid");
        }
        if(!password_input.getText().equals(rePassword_input.getText())){
            System.out.println("Password does not match");
            InputValidator.setValue(repass_err, InputValidator.RED, "Password does not match");
            isValid = false;
        }
        System.out.println(isValid);
        return isValid;
    }
    void register() {
        if(registerAttempt == MAX_ATTEMPT){
            System.out.println("Too many attempts");
            return;
        }
        registerAttempt++;
        if(!validateInput()){
            System.out.println("Invalid Input");
            return;
        }

        if(typeOfUser_choiceBox.getValue() == ACCOUNT_TYPE.RENTER){
            RenterDAO userDAO = new RenterDAO() ;
            Renter newUser = new Renter();
            UserFactory(newUser);
            if(userDAO.add(newUser)) System.out.println("Renter Registered");
            else{
                System.out.println("Renter Registration Failed. Trying again");
                register();
            }
        }
        else if(typeOfUser_choiceBox.getValue() == ACCOUNT_TYPE.HOST){
            HostDAO userDAO = new HostDAO();
            Host newUser = new Host();
            UserFactory(newUser);
            if(userDAO.add(newUser)) System.out.println("Host Registered");
            else{
                System.out.println("Renter Registration Failed. Trying again");
                register();

            }
        }
        else if(typeOfUser_choiceBox.getValue() == ACCOUNT_TYPE.OWNER){
            OwnerDAO userDAO = new OwnerDAO();
            Owner newUser = new Owner();
            UserFactory(newUser);
            if(userDAO.add(newUser)) System.out.println("Owner Registered");
            else{
                System.out.println("Renter Registration Failed. Trying again");
                register();

            }
        }

        registerAttempt = 0;
        resetTextFields();
    }

    private <T extends Person> void UserFactory(T newUser) {
        newUser.setName(fullName_input.getText());
        newUser.setUsername(username_input.getText());
        newUser.setPassword(password_input.getText());
        newUser.setContact(contact_input.getText());
        newUser.setDateOfBirth((LocalDate)dob_datePicker.getValue());
    }

    private void resetLabel(){
        name_err.setText("");
        username_err.setText("");
        contact_err.setText("");
        dob_err.setText("");
        password_err.setText("");
        repass_err.setText("");
    }
    private void resetTextFields(){
        username_input.setText("");
        fullName_input.setText("");
        password_input.setText("");
        rePassword_input.setText("");
        contact_input.setText("");
        dob_datePicker.setValue(null);
    }
}
