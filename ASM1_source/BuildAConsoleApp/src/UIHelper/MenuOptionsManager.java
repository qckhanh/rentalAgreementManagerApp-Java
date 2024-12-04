package UIHelper;
import Model.*;
import Database.Database;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MenuOptionsManager {

    public static void mainMenu() {
        UserInterfaceManager.printMenu("MAIN MENU",
                List.of("Rental Agreement Management" + " [" + Database.getInstance().getAll(RentalAgreement.class).size() + " entities]",
                        "Property Management" + " [" + Database.getInstance().getAll(Property.class).size() + " entities]",
                        "Host Management" + " [" + Database.getInstance().getAll(Host.class).size() + " entities]",
                        "Renter Management" + " [" + Database.getInstance().getAll(Renter.class).size() + " entities]",
                        "Owner Management" + " [" + Database.getInstance().getAll(Owner.class).size() + " entities]",
                        "Payment Management" + " [" + Database.getInstance().getAll(Payment.class).size() + " entities]",
                        "Exit"
                )
        );

    }

    public static void RenterManagerMenu() {
        UserInterfaceManager.printMenu(
                "RENTER MANAGER MENU",
                List.of("Add new renter",
                        "Delete renter",
                        "Edit renter profile",
                        "View All Renters",
                        "Generate Report",
                        "Back to Main Menu"
                )
        );
    }

    public static void PropertyManagerMenu() {
        UserInterfaceManager.printMenu(
                "PROPERTY MANAGER MENU",
                List.of("Add new property",
                        "Delete property",
                        "Edit property profile",
                        "View All property",
                        "Generate Report",

                        "Back to Main Menu"
                )
        );
    }

    public static void HostManagerMenu() {
        UserInterfaceManager.printMenu(
                "HOST MANAGER MENU",
                List.of("Add new host",
                        "Delete host",
                        "Edit host profile",
                        "View All Hosts",
                        "Generate Report",
                        "Add Owner to Host",
                        "Add Property to Host",
                        "Back to Main Menu"
                )
        );
    }

    public static void AgreementManagerMenu() {
        UserInterfaceManager.printMenu(
                "AGREEMENT MANAGER MENU",
                List.of("Add new agreement",
                        "Delete agreement",
                        "Edit agreement",
                        "View All Agreements",
                        "Generate Report",
                        "Back to Main Menu"
                )
        );
    }

    public static void OwnerManagerMenu() {
        UserInterfaceManager.printMenu(
                "OWNER MANAGER MENU",
                List.of("Add new owner",
                        "Delete owner",
                        "Edit owner profile",
                        "View All Owners",
                        "Generate Report",
                        "Back to Main Menu"
                )
        );
    }

    public static void PaymentManagerMenu() {
        UserInterfaceManager.printMenu(
                "PAYMENT MANAGER MENU",
                List.of("Add new payment",
                        "Delete payment",
                        "Edit payment",
                        "View All Payment",
                        "Generate Report",
                        "Back to Main Menu"
                )
        );
    }

    public static void decor() {
        System.out.println("      ':.");
        System.out.println("         []_____");
        System.out.println("  ____||____");
        System.out.println(" ///////////\\");
        System.out.println("///////////  \\");
        System.out.println("|    _    |  |");
        System.out.println("|[] | | []|[]|");
        System.out.println("|   | |   |  |");

        UserInterfaceManager.printMenu(
                "RENTAL AGREEMENT APPLICATION",
                List.of("Name: Khong Quoc Khanh",
                        "Student ID: s4021494",
                        "Course: Further Programming",
                        "Lecturers & Tutors: Dr. Minh Vu Thanh, Dr. Dung Nguyen, Dr. Phong Ngo",
                        "Admin role: " + LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm")) + " "
                                + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                )
        );

//        UserInterfaceManager.printMenu("RENTAL AGREEMENT APPLICATION", List.of());
    }

}
