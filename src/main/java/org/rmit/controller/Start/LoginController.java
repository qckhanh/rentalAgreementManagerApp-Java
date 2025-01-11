package org.rmit.controller.Start;

import atlantafx.base.controls.PasswordTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.synedra.validatorfx.Validator;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.rmit.Helper.TaskUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.database.*;
import org.rmit.view.Admin.AdminViewFactory;
import org.rmit.view.ViewCentral;
import org.rmit.model.Persons.*;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Session;
import org.rmit.view.Start.ACCOUNT_TYPE;
import org.rmit.view.Start.NOTIFICATION_TYPE;
import org.rmit.view.Start.StartViewFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    public AnchorPane anchorPane;
    public Label username_lbl;
    public TextField username_input;
    public Label password;
    public PasswordTextField password_input;
    public Button forgetPass_btn;
    public Button signIn_btn;
    public Button register_btn;
    public Button guest_btn;
    public ChoiceBox<ACCOUNT_TYPE> userLOGINType_ChoiceBox;
    public StartViewFactory viewFactory;
    public ValidateLoginDAO dao;
    public Label status_label;
    public Label password_err;
    public Label username_err;
    public Person loginUser = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        decor();
        setDataBehavior();
        setButtonAction();
    }
    private void setButtonAction(){
        password_input.setOnAction(actionEvent -> signInValidate());
        register_btn.setOnAction(actionEvent -> openRegister());
        signIn_btn.setOnAction(actionEvent -> signInValidate());
        guest_btn.setOnAction(actionEvent -> loginAsGuest());
        revealPassword(password_input);
    }

    private void setDataBehavior(){
        username_err.setText("");
        password_err.setText("");

        password_input.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue)){
                status_label.setText("");
//                UIDecorator.tfOK(password_input);
            }
        });
        username_input.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue)){
                status_label.setText("");
//                UIDecorator.tfOK(username_input);
            }
        });
        status_label.setText("");
        ViewCentral.getInstance().getStartViewFactory().setIsLogin(true);

        viewFactory = ViewCentral.getInstance().getStartViewFactory();
        userLOGINType_ChoiceBox.setItems(FXCollections.observableArrayList(
                ACCOUNT_TYPE.ADMIN,
                ACCOUNT_TYPE.GUEST,
                ACCOUNT_TYPE.RENTER,
                ACCOUNT_TYPE.HOST,
                ACCOUNT_TYPE.OWNER)
        );
        userLOGINType_ChoiceBox.setValue(viewFactory.getAccountLoginType());
        userLOGINType_ChoiceBox.valueProperty().addListener(observable -> {
            System.out.println("Selected: " + userLOGINType_ChoiceBox.getValue());
            viewFactory.setAccountLoginType(userLOGINType_ChoiceBox.getValue());
        });
    }

    private void decor(){
        UIDecorator.setNormalButton(signIn_btn, UIDecorator.LOG_IN(), "Sign In");
        UIDecorator.setNormalButton(guest_btn, UIDecorator.GUEST(), "Login as Guest");
    }

    void openRegister() {
        viewFactory.showRegisterView();
    }

    void loginAsGuest() {
        Stage currentStage = (Stage) signIn_btn.getScene().getWindow();
        ViewCentral.getInstance().getStartViewFactory().closeStage(currentStage);
        ViewCentral.getInstance().getGuestViewFactory().startGuestView();
    }

    private void signInValidate(){
        Validator validator = new Validator();
        buildValidation(validator);
        String username = username_input.getText();
        String password = password_input.getPassword();
        if(!validator.validate()){
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Please fill in all fields");
            return;
        }
        if(viewFactory.getAccountLoginType() == ACCOUNT_TYPE.ADMIN) {
            dao = new AdminDAO();
            ViewCentral.getInstance().getStartViewFactory().standOnNotification(NOTIFICATION_TYPE.INFO, anchorPane, "Logging in. Please wait");
            Task<Admin> validate = TaskUtils.createTask(() ->{
                Platform.runLater(() -> signIn_btn.setDisable(true));
                return (Admin) dao.validateLogin(username, password);
            });
            TaskUtils.run(validate);
            validate.setOnSucceeded(e -> Platform.runLater(() -> {
                signIn_btn.setDisable(false);
                loginUser = validate.getValue();
                if(loginUser == null) ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Incorrect username or password");
                else {
                    ViewCentral.getInstance().getStartViewFactory().standOnNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Login successful. Loading data");
                    Session.getInstance().setCurrentUser(loginUser);
                    Stage currentStage = (Stage) signIn_btn.getScene().getWindow();
                    AdminViewFactory.setLoginStage(currentStage);
                    ViewCentral.getInstance().getAdminViewFactory();
                }
            }));
        }
        else if (viewFactory.getAccountLoginType() == ACCOUNT_TYPE.RENTER) {
            dao = new RenterDAO();
            ViewCentral.getInstance().getStartViewFactory().standOnNotification(NOTIFICATION_TYPE.INFO, anchorPane, "Logging in. Please wait");
            Task<Renter> validate = TaskUtils.createTask(() ->{
                Platform.runLater(() -> signIn_btn.setDisable(true));
                return (Renter) dao.validateLogin(username, password);
            });
            TaskUtils.run(validate);
            validate.setOnSucceeded(e -> Platform.runLater(() -> {
                signIn_btn.setDisable(false);
                loginUser = validate.getValue();
                if(loginUser == null) ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Incorrect username or password");
                else {
                    ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Login successful. Loading data");
                    Session.getInstance().setCurrentUser(loginUser);
                    Stage currentStage = (Stage) signIn_btn.getScene().getWindow();
                    ViewCentral.getInstance().getStartViewFactory().closeStage(currentStage);
                    ViewCentral.getInstance().getRenterViewFactory().startRenterView();
                }
            }));
        }
        else if (viewFactory.getAccountLoginType() == ACCOUNT_TYPE.OWNER) {
            dao = new OwnerDAO();
            ViewCentral.getInstance().getStartViewFactory().standOnNotification(NOTIFICATION_TYPE.INFO, anchorPane, "Logging in. Please wait");
            Task<Owner> validate = TaskUtils.createTask(() ->{
                Platform.runLater(() -> signIn_btn.setDisable(true));
                return (Owner) dao.validateLogin(username, password);
            });
            TaskUtils.run(validate);
            validate.setOnSucceeded(e -> Platform.runLater(() -> {
                signIn_btn.setDisable(false);
                loginUser = validate.getValue();
                if(loginUser == null) ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Incorrect username or password");
                else {
                    ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Login successful. Loading data");
                    Session.getInstance().setCurrentUser(loginUser);
                    Stage currentStage = (Stage) signIn_btn.getScene().getWindow();
                    ViewCentral.getInstance().getStartViewFactory().closeStage(currentStage);
                    ViewCentral.getInstance().getOwnerViewFactory().startOwnerView();
                }
            }));
        }
        else if (viewFactory.getAccountLoginType() == ACCOUNT_TYPE.HOST) {
            dao = new HostDAO();
            ViewCentral.getInstance().getStartViewFactory().standOnNotification(NOTIFICATION_TYPE.INFO, anchorPane, "Logging in. Please wait");
            Task<Host> validate = TaskUtils.createTask(() ->{
                Platform.runLater(() -> signIn_btn.setDisable(true));
                return (Host) dao.validateLogin(username, password);
            });
            TaskUtils.run(validate);
            validate.setOnSucceeded(e -> Platform.runLater(() -> {
                signIn_btn.setDisable(false);
                loginUser = validate.getValue();
                if(loginUser == null) ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Incorrect username or password");
                else {
                    ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Login successful. Loading data");
                    Session.getInstance().setCurrentUser(loginUser);
                    Stage currentStage = (Stage) signIn_btn.getScene().getWindow();
                    ViewCentral.getInstance().getStartViewFactory().closeStage(currentStage);
                    ViewCentral.getInstance().getHostViewFactory().startHostView();
                }
            }));
        }
        ViewCentral.getInstance().getStartViewFactory().setIsLogin(loginUser != null);
    }

    private void buildValidation(Validator validator){
        validator.createCheck()
                .dependsOn("username", username_input.textProperty())
                .immediate()
                .withMethod(c -> {
                    String username = c.get("username");
                    if(username.isBlank()) {
                        c.error("Username cannot be empty");
//                        username_err.setText("Username cannot be empty");
                    }
                });
        validator.createCheck()
                .dependsOn("password", password_input.passwordProperty())
                .immediate()
                .withMethod(c -> {
                    String password = c.get("password");
                    if (password.isBlank()) {
                        c.error("Password cannot be empty");
                    }
                });

//        signIn_btn.disableProperty().bind(validator.containsErrorsProperty());

        validator.validationResultProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.getMessages().isEmpty()) {
                status_label.setTextFill(Color.RED);
                status_label.setText("Please fill in all fields");
            }
        });
    }

    private void revealPassword(PasswordTextField passwordTextField){
        FontIcon icon = new FontIcon(Feather.EYE_OFF);
        icon.setCursor(Cursor.HAND);
        icon.setOnMouseClicked(e -> {
            icon.setIconCode(passwordTextField.getRevealPassword()
                    ? Feather.EYE_OFF : Feather.EYE
            );
            passwordTextField.setRevealPassword(!passwordTextField.getRevealPassword());
        });
        passwordTextField.setRight(icon);
    }

}

