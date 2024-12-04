package UIHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class UserInterfaceManager {

    public UserInterfaceManager() {}
    public static final String RESET = "\033[0m";
    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[32m";
    public static final String YELLOW = "\033[33m";
    public static final String BLUE = "\033[34m";
    public static final String PURPLE = "\033[35m";
    public static final String CYAN = "\033[36m";
    private static final String HORIZONTAL_DASH = "-";
    private static final String VERTICAL_DASH = "|";
    private static final String CROSS = "+";

    public static void printTableName(String tableName, List<String> headers, List<List<String>> data) {
//        System.out.println("\n");
        if (tableName != null && !tableName.isEmpty()) {
            int totalWidth = 5; // For index column and separators
            for (int width : getColumnWidths(headers, data)) {
                totalWidth += width + 3; // Add 2 for padding and 1 for separator
            }
            System.out.println(" ".repeat((totalWidth - tableName.length()) / 2) + tableName);
        }
    }

    public static void printTable(String tableName, List<String> headers, List<List<String>> data) {
        printTableName(tableName, headers, data); // Display the table name first

        int[] columnWidths = getColumnWidths(headers, data);

        // Calculate index column width dynamically based on the number of rows
        int indexColumnWidth = Math.max(3, String.valueOf(data.size()).length()); // Minimum width of 3

        printSeparatorLine(indexColumnWidth, columnWidths);

        // Print header with dynamic index column width
        System.out.printf(VERTICAL_DASH + " %" + indexColumnWidth + "s "+ VERTICAL_DASH + " ", " "); // Adjusted index column width
        printRow(headers, columnWidths);
        printSeparatorLine(indexColumnWidth, columnWidths);

        // Print data rows with index
        for (int i = 0; i < data.size(); i++) {
            System.out.printf(VERTICAL_DASH + " %" + indexColumnWidth + "d " + VERTICAL_DASH + " ", i + 1); // Dynamic width for index
            List<String> row = data.get(i);
            printRow(row, columnWidths);
        }
        printSeparatorLine(indexColumnWidth, columnWidths);
    }

    private static int[] getColumnWidths(List<String> headers, List<List<String>> data) {
        int[] widths = new int[headers.size()];

        // Set width based on header names
        for (int i = 0; i < headers.size(); i++) {
            widths[i] = headers.get(i).length();
        }

        // Adjust width based on each row's cell contents
        for (List<String> row : data) {
            for (int i = 0; i < row.size(); i++) {
                if (row.get(i).length() > widths[i]) {
                    widths[i] = row.get(i).length();
                }
            }
        }

        return widths;
    }

    private static void printSeparatorLine(int indexColumnWidth, int[] columnWidths) {
        System.out.print(CROSS + HORIZONTAL_DASH.repeat(indexColumnWidth + 2) + CROSS); // Adjust index column width in separator
        for (int width : columnWidths) {
            System.out.print(HORIZONTAL_DASH.repeat(width + 2) + CROSS);
        }
        System.out.println();
    }

    private static void printRow(List<String> row, int[] columnWidths) {
        for (int i = 0; i < columnWidths.length; i++) {
            String cell = i < row.size() ? row.get(i) : "";
            System.out.print(cell);
            System.out.print(" ".repeat(columnWidths[i] - cell.length()));
            System.out.print(" " + VERTICAL_DASH + " ");
        }
        System.out.println();
    }

    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text + " ".repeat(Math.max(0, width - text.length() - padding));
    }

    private static String padRight(String text, int width) {
        return text + " ".repeat(Math.max(0, width - text.length()));
    }

    public static void printMenu(String menuTitle, List<String> options) {
        int maxOptionLength = menuTitle.length();
        for (String option : options) {
            maxOptionLength = Math.max(maxOptionLength, option.length() + 4); // Include option numbering
        }

        int totalWidth = maxOptionLength + 6; // Extra space for padding and borders
        System.out.println(CROSS + HORIZONTAL_DASH.repeat(totalWidth) + CROSS);
        System.out.println(VERTICAL_DASH + " " + centerText(menuTitle, totalWidth - 2) + " " + VERTICAL_DASH);
        System.out.println(CROSS + HORIZONTAL_DASH.repeat(totalWidth) + CROSS);

        for (int i = 0; i < options.size(); i++) {
            String optionText = (i + 1) + ". " + options.get(i);
            if(i + 1 == options.size()) {
                optionText = "0. " + options.get(i);
            }
            System.out.println(VERTICAL_DASH + " " + padRight(optionText, totalWidth - 2) + " " + VERTICAL_DASH);
        }

        System.out.println(CROSS + HORIZONTAL_DASH.repeat(totalWidth) + CROSS);
    }

    public static void printPrompt(String type, String promptMessage) {
        int width = promptMessage.length() + 6; // Calculate total width with padding and borders
        System.out.println(CROSS + HORIZONTAL_DASH.repeat(width) + CROSS);
        System.out.print("| " + padRight( type + promptMessage, 0) + " ");
//        System.out.println("+" + "-".repeat(width) + "+");
    }

    public static void errorMessage(String promptMessage) {
        int width = promptMessage.length() + 6; // Calculate total width with padding and borders
//        System.out.println("\n+" + "_".repeat(width) + "+");

        System.out.print(">> " + RED +  padRight( "[System] " + promptMessage, 0) +  " ❌ "+ RESET + "\n");
//        System.out.println("+" + "_".repeat(width) + "+");
    }

    public static void successMessage(String promptMessage) {
        int width = promptMessage.length() + 6; // Calculate total width with padding and borders
//        System.out.println("\n+" + "_".repeat(width) + "+");
        System.out.print(">> " + GREEN +  padRight( "[System] " + promptMessage, 0) + " ✅ " + RESET +  "\n");
//        System.out.println("+" + "_".repeat(width) + "+");
    }

    public static String generateActionTitle(String action) {
        return action.toUpperCase();
    }

    public static void displayTitle(String action) {
        String title = generateActionTitle(action);
        int width = title.length() + 4; // Adjust the width based on title length

        // Print the title banner
        System.out.println(CROSS + HORIZONTAL_DASH.repeat(width) + CROSS);
        System.out.println(VERTICAL_DASH + " " + title + " " + VERTICAL_DASH);
        System.out.println(CROSS + HORIZONTAL_DASH.repeat(width) + CROSS);
    }

    public static void pressAnyKeyToContinue() {
        System.out.println(BLUE + ">> Press any key to continue..." + RESET);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

    }

    public static void printToFile(String fileName, Consumer<PrintStream> method) {
        PrintStream originalOut = System.out; // Save the original System.out
        try (PrintStream fileOut = new PrintStream(new FileOutputStream(fileName, true))) {
            System.setOut(fileOut); // Redirect System.out to the file
            method.accept(fileOut); // Execute the method passed as an argument
        } catch (IOException e) {
            originalOut.println(RED + "[Error] Failed to write to file: " + e.getMessage() + RESET);
        } finally {
            System.setOut(originalOut); // Restore the original System.out
        }
    }







}
