package org.rmit.Helper;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import atlantafx.base.theme.Styles;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextField;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2RoundAL;

public class UIDecorator {
    public static Node DANGER = new FontIcon(Feather.TRASH);
    public static Node SUCCESS = new FontIcon(Feather.CHECK_CIRCLE);
    public static FontIcon EDIT = new FontIcon(Feather.EDIT);
    public static FontIcon LOG_IN = new FontIcon(Feather.LOG_IN);
    public static Node INFO = new FontIcon(Feather.INFO);
    public static FontIcon DELETE = new FontIcon(Material2RoundAL.DELETE_FOREVER);
    public static FontIcon ADD = new FontIcon(Feather.PLUS_CIRCLE);
    public static FontIcon MANAGE = new FontIcon(Feather.TOOL);

    //Menu
    public static FontIcon USER = new FontIcon(Material2AL.ACCOUNT_CIRCLE);
    public static FontIcon PROFILE = new FontIcon(Feather.EDIT_2);
    public static FontIcon LOG_OUT = new FontIcon(Feather.LOG_OUT);
    public static FontIcon PAYMENT = new FontIcon(Material2AL.ATTACH_MONEY);
    public static FontIcon NEW_PAYMENT = new FontIcon(Material2AL.CREDIT_CARD);
    public static FontIcon RENTAL = new FontIcon(Feather.FILE_TEXT);
    public static FontIcon SEARCH = new FontIcon(Material2MZ.SEARCH);
    public static FontIcon PROPERTY = new FontIcon(Material2AL.HOME);
    public static FontIcon OTHER_PERSON = new FontIcon(Material2AL.GROUP);
    public static FontIcon REGISTER = new FontIcon(Feather.USER_PLUS);
    public static FontIcon GUEST = new FontIcon(FontAwesomeSolid.USER_SECRET);

    public static void setApplicationTheme(){
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    }
    public static void setDangerButton(Button button, FontIcon icon, String message){
        button.getStyleClass().removeAll();

        button.setText(message);
        button.setGraphic(icon);
        button.getStyleClass().addAll(
                Styles.ROUNDED,
                Styles.BUTTON_OUTLINED,
                Styles.DANGER
        );
        button.setContentDisplay(ContentDisplay.LEFT);
        button.setMnemonicParsing(true);
    }

    public static void setNormalButton(Button button, FontIcon icon, String message){
        button.getStyleClass().removeAll();

        button.setText(message);
        button.setGraphic(icon);
        button.getStyleClass().addAll(
                Styles.ROUNDED,
                Styles.BUTTON_OUTLINED,
                Styles.ACCENT
        );
        button.setContentDisplay(ContentDisplay.LEFT);
//        button.setMnemonicParsing(true);
    }

    public static void setSuccessButton(Button button, FontIcon icon, String message){
        button.getStyleClass().removeAll();

        button.setText(message);
        button.setGraphic(icon);
        button.getStyleClass().addAll(
                Styles.ROUNDED,
                Styles.BUTTON_OUTLINED,
                Styles.SUCCESS
        );
        button.setContentDisplay(ContentDisplay.LEFT);
        button.setMnemonicParsing(true);
    }

    public static void buttonIcon(Button button, Node icon){
        button.getStyleClass().removeAll();


        button.setText(null);
        button.setGraphic(icon);
        button.getStyleClass().addAll(
                Styles.BUTTON_CIRCLE,
                Styles.WARNING,
                Styles.ACCENT
        );
        button.setContentDisplay(ContentDisplay.CENTER);
        button.setMnemonicParsing(true);
    }

    public static void tfOK(TextField textField){
        textField.pseudoClassStateChanged(Styles.STATE_SUCCESS, true);
    }

    public static void tfError(TextField textField) {
        textField.pseudoClassStateChanged(Styles.STATE_DANGER, true);
    }

}
