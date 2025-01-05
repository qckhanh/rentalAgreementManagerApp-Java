package org.rmit.Helper;


import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import static java.lang.Double.parseDouble;

public class InputValidator {

    public static Color RED = Color.RED;
    public static Color GREEN = Color.GREEN;

    public static void setLabelError(Label label, Color color, String message){
        label.setText(message);
        label.setTextFill(color);
    }
    //checker
    public static boolean isValidUsername(String username, Label username_err) {
        boolean isValid = username.length() >= 6;
        if(!isValid) setLabelError(username_err, RED, "Username must be at least 6 characters");
        return isValid;
    }
    public static boolean isValidPassword(String s, Label password_err) {
        boolean isValid = s.length() >= 8;
        if(!isValid) setLabelError(password_err, RED, "Password must be at least 8 characters");
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
        if(!isValid) setLabelError(label, RED, "Field must not be empty");
        return isValid;

    }
    public static boolean isValidDateFormat(LocalDate date, Label label) {
        if(date == null){
            setLabelError(label, RED, "Date must not be empty");
            return false;
        }
        System.out.println(DateUtils.formatDate(date));

        if(DateUtils.formatDate(date).equals(DateUtils.DEFAULT_DATE)){
            setLabelError(label, RED, "Invalid date format");
            System.out.println("Invalid date format xxxxxxxxxx");
            return false;
        }

        LocalDate localDate = LocalDate.now();
        boolean isBefore =  date.isBefore(localDate);
        if(!isBefore) setLabelError(label, RED, "Date must be before today");
        System.out.println(isBefore);
        return isBefore;

    }
    public static boolean isValidInteger(String s, Label label){
        if(!NoCondition(s, label)) return false;
        for(int i = 0; i < s.length(); i++){
            if(!Character.isDigit(s.charAt(i))){
                setLabelError(label, RED, "Input must be a number");
                return false;
            }
        }
        return true;
    }
    public static boolean isValidOption(String s, int min, int max, Label label){
        if(!isValidInteger(s, label)) return false;
        int option = Integer.parseInt(s);
        boolean isValid = (option >= min && option <= max);
        if(!isValid) setLabelError(label, RED, "Option is out of range [" + min + ", " + max + "]");
        return isValid;
    }
    public static boolean isValidContact(String s, Label label){
        if(!isValidEmail(s, label) && !isValidPhoneNumber(s, label)) {
            setLabelError(label, RED, "Invalid contact format");
            return false;
        }
        return true;

    }
    public static boolean isValidPrice(String s, Label label){
        for(int i = 0; i < s.length(); i++){
            if(!Character.isDigit(s.charAt(i)) && s.charAt(i) != '.'){
                setLabelError(label, RED, "Input must be a number");
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
        setLabelError(label, RED, "Invalid type");
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

    public static boolean isValidSquareMeters(String input, Label squareMetersErr) {
        // Check if input is a number
        if (!isValidInteger(input, squareMetersErr)) {
            return false;
        }

        // Check if the number is positive
        if (Integer.parseInt(input) <= 0) {
            squareMetersErr.setText("Square meters must be positive");
            squareMetersErr.setTextFill(RED);
            return false;
        }

        // Check if the input is empty
        if (input.isEmpty()) {
            squareMetersErr.setText("Field must not be empty");
            squareMetersErr.setTextFill(RED);
            return false;
        }

        return true;
    }

    public static boolean isValidParkingSpaces(String input, Label parkingSpaceErr) {
        // Check if input is a number
        if (!isValidInteger(input, parkingSpaceErr)) {
            return false;
        }

        // Check if the number is positive
        if (Integer.parseInt(input) <= 0) {
            parkingSpaceErr.setText("Parking spaces must be positive");
            parkingSpaceErr.setTextFill(RED);
            return false;
        }

        // Check if the input is empty
        if (input.isEmpty()) {
            parkingSpaceErr.setText("Field must not be empty");
            parkingSpaceErr.setTextFill(RED);
            return false;
        }

        return true;
    }

    public static boolean isValidBedrooms(String input, Label bedroomErr) {
        // Check if input is a number
        if (!isValidInteger(input, bedroomErr)) {
            return false;
        }

        // Check if the number is positive
        if (Integer.parseInt(input) <= 0) {
            bedroomErr.setText("Bedrooms must be positive");
            bedroomErr.setTextFill(RED);
            return false;
        }

        // Check if the input is empty
        if (input.isEmpty()) {
            bedroomErr.setText("Field must not be empty");
            bedroomErr.setTextFill(RED);
            return false;
        }

        // Check the number of bedrooms is less than or equal to the number of rooms
        if (Integer.parseInt(input) > Integer.parseInt(bedroomErr.getText())) {
            bedroomErr.setText("Bedrooms must be less than or equal to the number of rooms");
            bedroomErr.setTextFill(RED);
            return false;
        }

        return true;
    }

    public static boolean isValidRooms(String input, Label roomErr) {
        // Check if input is a number
        if (!isValidInteger(input, roomErr)) {
            return false;
        }

        // Check if the number is positive
        if (Integer.parseInt(input) <= 0) {
            roomErr.setText("Rooms must be positive");
            roomErr.setTextFill(RED);
            return false;
        }

        // Check if the input is empty
        if (input.isEmpty()) {
            roomErr.setText("Field must not be empty");
            roomErr.setTextFill(RED);
            return false;
        }

        // Check the number of rooms is greater than or equal to the number of bedrooms
        if (Integer.parseInt(input) < Integer.parseInt(roomErr.getText())) {
            roomErr.setText("Rooms must be greater than or equal to the number of bedrooms");
            roomErr.setTextFill(RED);
            return false;
        }

        return true;
    }
}
