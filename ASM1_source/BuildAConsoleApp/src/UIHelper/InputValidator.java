package UIHelper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

import static java.lang.Double.parseDouble;

public class InputValidator {

    private static final Scanner scanner = new Scanner(System.in);
    //checker
    public static boolean isValidEmail(String s){
        boolean isValid = s.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
//        if(!isValid) AsciiTable.errorDispplay("Invalid email format");
        return isValid;
    }
    public static boolean NoCondition(String s){

        boolean isValid = !s.isBlank();
        if(!isValid) UserInterfaceManager.errorMessage("Input cannot be empty");
        return isValid;

    }
    public static boolean isValidPhoneNumber(String s) {
        boolean isValid =  s.matches("^0\\d{8,9}$");
        return isValid;
    }
    public static boolean isValidDateFormat(String s){
        boolean isValid = s.matches("^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[0-2])/([1-9]\\d{3})$");

        if (!isValid) {
            UserInterfaceManager.errorMessage("Invalid date format");
            return false;
        }

        LocalDate localDate = LocalDate.now();
        LocalDate tmp = LocalDate.parse(s, DateTimeFormatter.ofPattern("d/M/yyyy"));

        boolean isBefore =  tmp.isBefore(localDate);
        if(!isBefore) UserInterfaceManager.errorMessage("Date must not exceed the current date");
        return isBefore;

    }
    public static boolean isValidInteger(String s){
        if(!NoCondition(s)) return false;
        for(int i = 0; i < s.length(); i++){
            if(!Character.isDigit(s.charAt(i))){
                UserInterfaceManager.errorMessage("Input must be integer number");
                return false;
            }
        }
        return true;
    }
    public static boolean isValidOption(String s, int min, int max){
        if(!isValidInteger(s)) return false;
        int option = Integer.parseInt(s);
        boolean isValid = (option >= min && option <= max);
        if (!isValid) UserInterfaceManager.errorMessage("Option is out of range [" + min + ", " + max + "]");
        return isValid;
    }
    public static boolean isValidContact(String s){

        if(!isValidEmail(s) && !isValidPhoneNumber(s)) {
            UserInterfaceManager.errorMessage("Invalid contact format");
            return false;
        }
        return true;

    }
    public static boolean isValidPrice(String s){
        for(int i = 0; i < s.length(); i++){
            if(!Character.isDigit(s.charAt(i)) && s.charAt(i) != '.'){
                UserInterfaceManager.errorMessage("Input must be a number");
                return false;
            }
        }
        double price = parseDouble(s);
        boolean isValid =  price > 0;
        if(!isValid) UserInterfaceManager.errorMessage("Price must be greater than 0");
        return isValid;
    }
    public static boolean isValidType(String s, List<String> types){
        for(String type : types){
            if(type.equalsIgnoreCase(s)) return true;
        }
        UserInterfaceManager.errorMessage("Invalid type");
        return false;
    }
    public static boolean confirm(String message){
        int choice = (int)InputValidator.getValidInput(Integer.class,  UserInterfaceManager.YELLOW +  message + " 1: Yes | 0: No? " + UserInterfaceManager.RESET, input->isValidOption(input, 0, 1));
        return choice == 1;
    }

    //common get input
    public static Object getValidInput(Class<?> clazz, String message, Predicate<String> validator) {
        String input;
        UserInterfaceManager.printPrompt(" [Input] ", message);

        while (true) {
            input = scanner.nextLine();
            if (validator.test(input)) break;
            UserInterfaceManager.printPrompt(" [Input] ", message);

        }

        if (clazz == Integer.class) {
            return Integer.parseInt(input);
        } else if (clazz == Double.class) {
            return parseDouble(input);
        }
        else if(clazz == Date.class){
            try{
                return DateCreator.newDate(input);
            }
            catch (Exception e){
                return getValidInput(clazz, message, validator);
            }
        }
        return input; // Default return as String
    }
}
