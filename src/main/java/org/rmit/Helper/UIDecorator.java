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
import org.kordamp.ikonli.material2.Material2RoundMZ;

import java.awt.*;

public class UIDecorator {

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




    public static FontIcon EDIT() {
        return new FontIcon(Feather.EDIT);
    }

    public static FontIcon LOG_IN() {
        return new FontIcon(Feather.LOG_IN);
    }

    public static FontIcon INFO() {
        return new FontIcon(Feather.INFO);
    }

    public static FontIcon DELETE() {
        return new FontIcon(Material2RoundAL.DELETE_FOREVER);
    }

    public static FontIcon ADD() {
        return new FontIcon(Feather.PLUS_CIRCLE);
    }

    public static FontIcon MANAGE() {
        return new FontIcon(Feather.TOOL);
    }

    public static FontIcon NOTIFICATION() {
        return new FontIcon(Feather.BELL);
    }

    // Menu
    public static FontIcon USER() {
        return new FontIcon(Material2AL.ACCOUNT_CIRCLE);
    }

    public static FontIcon PROFILE() {
        return new FontIcon(Feather.EDIT_2);
    }

    public static FontIcon LOG_OUT() {
        return new FontIcon(Feather.LOG_OUT);
    }

    public static FontIcon PAYMENT() {
        return new FontIcon(Material2AL.ATTACH_MONEY);
    }

    public static FontIcon NEW_PAYMENT() {
        return new FontIcon(Material2AL.CREDIT_CARD);
    }

    public static FontIcon RENTAL() {
        return new FontIcon(Feather.FILE_TEXT);
    }

    public static FontIcon SEARCH() {
        return new FontIcon(Feather.SEARCH);
    }

    public static FontIcon PROPERTY() {
        return new FontIcon(Material2AL.HOME);
    }

    public static FontIcon OTHER_PERSON() {
        return new FontIcon(Material2AL.GROUP);
    }

    public static FontIcon REGISTER() {
        return new FontIcon(Feather.USER_PLUS);
    }

    public static FontIcon GUEST() {
        return new FontIcon(FontAwesomeSolid.USER_SECRET);
    }

    public static FontIcon NEXT(){
        return new FontIcon(Feather.ARROW_RIGHT);
    }

    public static FontIcon PREVIOUS(){
        return new FontIcon(Feather.ARROW_LEFT);
    }

    public static FontIcon NEW_AGREEMENT(){
        return new FontIcon(Feather.FILE_PLUS);
    }


    public static FontIcon REFRESH() {
        return new FontIcon(Feather.REFRESH_CW);
    }


    public static FontIcon SEND() {
        return new FontIcon(Feather.SEND);
    }

    public static FontIcon APPROVE(){
        return new FontIcon(Feather.THUMBS_UP);
    }

    public static FontIcon DENY(){
        return new FontIcon(Feather.THUMBS_DOWN);
    }

    public static FontIcon USER_ROLE(){
        return new FontIcon(Material2RoundMZ.SUPERVISED_USER_CIRCLE);
    }

    public static FontIcon FAIL(){
        return new FontIcon(Feather.X_CIRCLE);
    }

    public static FontIcon SUCCESS() {
        return new FontIcon(Feather.CHECK_CIRCLE);
    }

    public static FontIcon WARNING() {
        return new FontIcon(Feather.ALERT_TRIANGLE);
    }

    public static FontIcon BACK_PREVIOUS_PAGE() {
        return new FontIcon(Feather.CHEVRON_LEFT);
    }

}
