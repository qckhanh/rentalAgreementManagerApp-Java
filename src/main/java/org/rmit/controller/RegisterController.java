package org.rmit.controller;

import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.rmit.database.HostDAO;
import org.rmit.database.OwnerDAO;
import org.rmit.database.RenterDAO;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Person;
import org.rmit.model.Persons.Renter;
import org.rmit.view.StartView.ACCOUNT_TYPE;

import java.net.URL;
import java.time.LocalDate;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeOfUser_choiceBox.setValue(ACCOUNT_TYPE.RENTER);
        typeOfUser_choiceBox.setItems(FXCollections.observableArrayList(
                ACCOUNT_TYPE.RENTER,
                ACCOUNT_TYPE.HOST,
                ACCOUNT_TYPE.OWNER)
        );
        backToLogin_btn.setOnAction(actionEvent -> openLogin());
        submitRegister_btn.setOnAction(e -> register());
    }

    void openLogin() {
        ModelCentral.getInstance().getViewFactory().showLoginView();
    }

    void register(){
        if(typeOfUser_choiceBox.getValue() == ACCOUNT_TYPE.RENTER){
            RenterDAO userDAO = new RenterDAO() ;
            Renter newUser = new Renter();
            UserFactory(newUser);
            if(userDAO.add(newUser)) System.out.println("Renter Registered");
            else System.out.println("Renter Registration Failed");
        }
        else if(typeOfUser_choiceBox.getValue() == ACCOUNT_TYPE.HOST){
            HostDAO userDAO = new HostDAO();
            Host newUser = new Host();
            UserFactory(newUser);
            if(userDAO.add(newUser)) System.out.println("Host Registered");
            else System.out.println("Renter Registration Failed");
        }
        else if(typeOfUser_choiceBox.getValue() == ACCOUNT_TYPE.OWNER){
            OwnerDAO userDAO = new OwnerDAO();
            Owner newUser = new Owner();
            UserFactory(newUser);
            if(userDAO.add(newUser)) System.out.println("Owner Registered");
            else System.out.println("Renter Registration Failed");
        }

        username_input.setText("");
        password_input.setText("");
        rePassword_input.setText("");
        contact_input.setText("");
        dob_datePicker.setValue(null);
    }

    private <T extends Person> void UserFactory(T newUser) {
        newUser.setName(fullName_input.getText());
        newUser.setUsername(username_input.getText());
        newUser.setPassword(password_input.getText());
        newUser.setContact(contact_input.getText());
        newUser.setDateOfBirth((LocalDate)dob_datePicker.getValue());
    }
}
