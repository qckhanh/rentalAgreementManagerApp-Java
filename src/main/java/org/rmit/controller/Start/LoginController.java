package org.rmit.controller.Start;

import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.rmit.database.DAOInterface;
import org.rmit.database.HostDAO;
import org.rmit.database.OwnerDAO;
import org.rmit.database.RenterDAO;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.*;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Session;
import org.rmit.view.Start.ACCOUNT_TYPE;
import org.rmit.view.Start.StartViewFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public Label username_lbl;
    public TextField username_input;
    public Label password;
    public PasswordField password_input;
    public Button forgetPass_btn;
    public Button signIn_btn;
    public Button register_btn;
    public ChoiceBox<ACCOUNT_TYPE> userLOGINType_ChoiceBox;
    public StartViewFactory viewFactory;
    public List<Renter> allRenters;
    public List<Host> allHosts;
    public List<Owner> allOwners;
    public List<Admin> allAdmins;
    public List<Guest> allGuests;
    public DAOInterface dao;
    public Label status_label;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ModelCentral.getInstance().getStartViewFactory().setIsLogin(true);
        status_label.setText("");
        ModelCentral.getInstance().getStartViewFactory().isLoginProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == false) {
                status_label.setTextFill(Color.RED);
                status_label.setText("Incorrect username or password");
            }
            else{
                status_label.setTextFill(Color.GREEN);
                status_label.setText("Login successful");
            }
        });

        viewFactory = ModelCentral.getInstance().getStartViewFactory();
        userLOGINType_ChoiceBox.setItems(FXCollections.observableArrayList(
                ACCOUNT_TYPE.ADMIN,
                ACCOUNT_TYPE.GUEST,
                ACCOUNT_TYPE.RENTER,
                ACCOUNT_TYPE.HOST,
                ACCOUNT_TYPE.OWNER)
        );
        userLOGINType_ChoiceBox.setValue(viewFactory.getAccountLoginType());
        userLOGINType_ChoiceBox.valueProperty().addListener(observable -> {
            viewFactory.setAccountLoginType(userLOGINType_ChoiceBox.getValue());
        });
        password_input.setOnAction(actionEvent -> signInValidate());
        register_btn.setOnAction(actionEvent -> openRegister());
        signIn_btn.setOnAction(actionEvent -> signInValidate());

    }

    void openRegister() {
        viewFactory.showRegisterView();
    }

    private void signInValidate(){
        String username = username_input.getText();
        String password = password_input.getText();
        Person loginUser = null;

        if(viewFactory.getAccountLoginType() == ACCOUNT_TYPE.ADMIN) {
            System.out.println("not implemented");
        }
        else if (viewFactory.getAccountLoginType() == ACCOUNT_TYPE.GUEST) {
            System.out.println("not implemented");
        }
        else if (viewFactory.getAccountLoginType() == ACCOUNT_TYPE.RENTER) {
            dao = new RenterDAO();
            loginUser = (Renter) dao.validateLogin(username, password);
            if(loginUser == null) System.out.println("Incorrect username or password");
            else {
                Session.getInstance().setCurrentUser(loginUser);
                Stage currentStage = (Stage) signIn_btn.getScene().getWindow();
                ModelCentral.getInstance().getStartViewFactory().closeStage(currentStage);
                ModelCentral.getInstance().getRenterViewFactory().startRenterView();
            }
        }
        else if (viewFactory.getAccountLoginType() == ACCOUNT_TYPE.OWNER) {
            dao = new OwnerDAO();
            loginUser = (Owner) dao.validateLogin(username, password);
            if(loginUser == null) System.out.println("Incorrect username or password");
            else {
                Session.getInstance().setCurrentUser(loginUser);
                Stage currentStage = (Stage) signIn_btn.getScene().getWindow();
                ModelCentral.getInstance().getStartViewFactory().closeStage(currentStage);
                ModelCentral.getInstance().getOwnerViewFactory().startOwnerView();
            }

        }
        else if (viewFactory.getAccountLoginType() == ACCOUNT_TYPE.HOST) {
            dao = new HostDAO();
            loginUser = (Host) dao.validateLogin(username, password);
            if(loginUser == null) System.out.println("Incorrect username or password");
            else {
                Session.getInstance().setCurrentUser(loginUser);
                Stage currentStage = (Stage) signIn_btn.getScene().getWindow();
                ModelCentral.getInstance().getStartViewFactory().closeStage(currentStage);
                ModelCentral.getInstance().getHostViewFactory().startHostView();
            }

        }

        ModelCentral.getInstance().getStartViewFactory().setIsLogin(loginUser != null);
        System.out.println("Login status: " + ModelCentral.getInstance().getStartViewFactory().isIsLogin());

        System.out.println("Current user: " + viewFactory.getAccountLoginType());
        System.out.println(Session.getInstance().getCurrentUser());
    }

    private boolean findUser(String username, String password, List<? extends Person> users){
        boolean isFound = false;
        for(Person user : users) {
            String x_username = user.getUsername() == null ? "" : user.getUsername();
            String x_contact = user.getContact() == null ? "" : user.getContact();
            String x_password = user.getPassword() == null ? "" : user.getPassword();

            if(x_username.equals(username)) isFound = true;
            else if (x_contact.equals(username)) isFound = true;
            if(isFound) {
                if(x_password.equals(password)) {
                    Session.getInstance().setCurrentUser(user);
                    status_label.setTextFill(Color.GREEN);
                    status_label.setText("Login successful");
                    return true;
                }
            }
        }
        status_label.setTextFill(Color.RED);
        status_label.setText("Incorrect username or password");
        return false;
    }


}
