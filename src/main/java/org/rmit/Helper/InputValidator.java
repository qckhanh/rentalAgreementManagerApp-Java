package org.rmit.Helper;


import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.rmit.database.DatabaseUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import static java.lang.Double.parseDouble;

public class InputValidator {

    public static Color RED = Color.RED;
    public static Color GREEN = Color.GREEN;

    public static void setValue(Label label, Color color, String message){
        label.setText(message);
        label.setTextFill(color);
    }
    //checker
    public static boolean isValidUsername(String username, Label username_err) {
        boolean isValid = username.length() >= 6;
        if(!isValid) setValue(username_err, RED, "Username must be at least 6 characters");
        return isValid;
    }
    public static boolean isValidPassword(String s, Label password_err) {
        boolean isValid = s.length() >= 8;
        if(!isValid) setValue(password_err, RED, "Password must be at least 8 characters");
        return isValid;
    }
    private static boolean isValidEmail(String s, Label email_err) {
        boolean isValid = s.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        return isValid;
    }
    private static boolean isValidPhoneNumber(String s, Label phone_err) {
        boolean isValid =  s.matches("^0\\d{8,9}$");
        return isValid;
    }
    public static boolean NoCondition(String s, Label label) {

        boolean isValid = !s.isBlank();
        if(!isValid) setValue(label, RED, "Field must not be empty");
        return isValid;

    }
    public static boolean isValidDateFormat(LocalDate date, Label label) {
        if(date == null){
            setValue(label, RED, "Date must not be empty");
            return false;
        }
        System.out.println(DateUtils.formatDate(date));

        if(DateUtils.formatDate(date).equals(DateUtils.DEFAULT_DATE)){
            setValue(label, RED, "Invalid date format");
            System.out.println("Invalid date format xxxxxxxxxx");
            return false;
        }

        LocalDate localDate = LocalDate.now();
        boolean isBefore =  date.isBefore(localDate);
        if(!isBefore) setValue(label, RED, "Date must be before today");
        System.out.println(isBefore);
        return isBefore;

    }
    public static boolean isValidInteger(String s, Label label){
        if(!NoCondition(s, label)) return false;
        for(int i = 0; i < s.length(); i++){
            if(!Character.isDigit(s.charAt(i))){
                setValue(label, RED, "Input must be a number");
                return false;
            }
        }
        return true;
    }
    public static boolean isValidOption(String s, int min, int max, Label label){
        if(!isValidInteger(s, label)) return false;
        int option = Integer.parseInt(s);
        boolean isValid = (option >= min && option <= max);
        if(!isValid) setValue(label, RED, "Option is out of range [" + min + ", " + max + "]");
        return isValid;
    }
    public static boolean isValidContact(String s, Label label){
        if(!isValidEmail(s, label) && !isValidPhoneNumber(s, label)) {
            setValue(label, RED, "Invalid contact format");
            return false;
        }
        return true;

    }
    public static boolean isValidPrice(String s, Label label){
        for(int i = 0; i < s.length(); i++){
            if(!Character.isDigit(s.charAt(i)) && s.charAt(i) != '.'){
                setValue(label, RED, "Input must be a number");
                return false;
            }
        }
        double price = parseDouble(s);
        boolean isValid =  price > 0;
        return isValid;
    }
    public static boolean isValidType(String s, List<String> types, Label label){
        for(String type : types){
            if(type.equalsIgnoreCase(s)) return true;
        }
        setValue(label, RED, "Invalid type");
        return false;
    }

    private static Object getValidInput(Class<?> clazz, String message, Predicate<String> validator) {
        String input;
//        UserInterfaceManager.printPrompt(" [Input] ", message);

        while (true) {
            input = "scanner.nextLine();";
            if (validator.test(input)) break;
//            UserInterfaceManager.printPrompt(" [Input] ", message);

        }

        if (clazz == Integer.class) {
            return Integer.parseInt(input);
        } else if (clazz == Double.class) {
            return parseDouble(input);
        }
        else if(clazz == Date.class){
            try{
                return DateUtils.newDate(input);
            }
            catch (Exception e){
                return getValidInput(clazz, message, validator);
            }
        }
        return input; // Default return as String
    }
}
