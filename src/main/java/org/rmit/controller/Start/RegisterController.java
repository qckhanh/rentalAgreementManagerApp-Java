package org.rmit.controller.Start;

import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import net.synedra.validatorfx.Validator;
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
import org.rmit.view.Start.NOTIFICATION_TYPE;

import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

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
    public AnchorPane anchorPane;

    Validator validator = new Validator();
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

        //reset error label when textfield is changed
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

        validateInput();

    }

    void openLogin() {
        resetLabel();
        ModelCentral.getInstance().getStartViewFactory().showLoginView();
    }

    private void validateInput(){
        validator.createCheck()
                .dependsOn("fullName", fullName_input.textProperty())
                .withMethod(context ->{
                    String input = context.get("fullName");
                    if(!InputValidator.NoCondition(input, name_err)){
                        context.error("Name must not be empty");
                    }
                })
                .decorates(fullName_input)
                .immediateClear();

        validator.createCheck()
                .dependsOn("username", username_input.textProperty())
                .withMethod(context ->{
                    String input = context.get("username");
                    if(!InputValidator.isValidNewUsername(input, username_err, true)){
                        context.error("Username must be at least 6 characters");
                    }
                })
                .decorates(username_input)
                .immediateClear();

        validator.createCheck()
                .dependsOn("contact", contact_input.textProperty())
                .withMethod(context ->{
                    String input = context.get("contact");
                    if(!InputValidator.isValidContact(input, contact_err)){
                        context.error("Invalid Phone Number");
                    }
                })
                .decorates(contact_input)
                .immediateClear();

        validator.createCheck()
                .dependsOn("dob", dob_datePicker.valueProperty())
                .withMethod(context ->{
                    LocalDate input = context.get("dob");
                    if(!InputValidator.isValidDateFormat(input, dob_err)){
                        context.error("Invalid Date");
                    }
                })
                .decorates(dob_datePicker)
                .immediateClear();

        validator.createCheck()
                .dependsOn("password", password_input.textProperty())
                .withMethod(context ->{
                    String input = context.get("password");
                    if(!InputValidator.isValidPassword(input, password_err)){
                        context.error("Password must be at least 8 characters");
                    }
                })
                .decorates(password_input)
                .immediateClear();

        validator.createCheck()
                .dependsOn("rePassword", rePassword_input.textProperty())
                .dependsOn("password", password_input.textProperty())
                .withMethod(context ->{
                    String password = context.get("password");
                    String passwordConfirmation = context.get("rePassword");

                    if(!password.equals(passwordConfirmation)){
                        context.error("Password does not match");
                        InputValidator.setLabelError(repass_err, InputValidator.RED, "Password does not match");
                    }
                })
                .decorates(rePassword_input)
                .decorates(password_input)
                .immediateClear();     // always use this

        submitRegister_btn.disableProperty().bind(validator.containsErrorsProperty()); // binding the button to the validator
    }

    void register() {
        if(registerAttempt == MAX_ATTEMPT){     // you dont have to understand this
            System.out.println("Too many attempts");
            return;
        }
        if(!validator.validate()){
            ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.WARNING, anchorPane, "Please fill in all fields correctly");
            return;
        }

        if(typeOfUser_choiceBox.getValue() == ACCOUNT_TYPE.RENTER){
            RenterDAO userDAO = new RenterDAO() ;
            Renter newUser = new Renter();
            UserFactory(newUser);
            if(userDAO.add(newUser)){
                ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Your account has been created. Please login");
            }
            else{
                ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Something went wrong. Please try again");
                registerAttempt++;
                register();
            }
        }
        else if(typeOfUser_choiceBox.getValue() == ACCOUNT_TYPE.HOST){
            HostDAO userDAO = new HostDAO();
            Host newUser = new Host();
            UserFactory(newUser);
            if(userDAO.add(newUser)) ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Your account has been created. Please login");
            else{
                ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Something went wrong. Please try again");
                registerAttempt++;
                register();
            }
        }
        else if(typeOfUser_choiceBox.getValue() == ACCOUNT_TYPE.OWNER){
            OwnerDAO userDAO = new OwnerDAO();
            Owner newUser = new Owner();
            UserFactory(newUser);
            if(userDAO.add(newUser)) ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Your account has been created. Please login");
            else{
                ModelCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Something went wrong. Please try again");
                registerAttempt++;
                register();
            }
        }

        registerAttempt = 0; // dont need to understand this
        resetTextFields(); // reset texfields
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
