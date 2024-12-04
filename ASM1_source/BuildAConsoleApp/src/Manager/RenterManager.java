package Manager;

import Database.Database;
import Model.*;
import UIHelper.UserInterfaceManager;
import UIHelper.DateCreator;
import UIHelper.InputValidator;

import java.util.*;

import static UIHelper.InputValidator.getValidInput;
import static UIHelper.InputValidator.isValidOption;

public class RenterManager implements MangerInterface{
    private final Database db;
    private List<Renter> renters;

    public static final Comparator<Renter> BY_ID = Comparator.comparingInt(Person::getId);
    public static final Comparator<Renter> BY_AGREEMENT_SIZE = Comparator.comparingInt(o->o.getAgreementList().size());
    public static final Comparator<Renter> BY_PAYMENT_SIZE = Comparator.comparingInt(o->o.getPayments().size());
    public static final Comparator<Renter> BY_DOB = (o1, o2) -> o1.getDateOfBirth().compareTo(o2.getDateOfBirth());
    public List<String> tableHeader = Arrays.asList("ID", "Full Name", "Date of Birth", "Contact", "Payments", "Agreements");

    public RenterManager(Database db) {
        this.db = db;
        renters = (List<Renter>)db.getAll(Renter.class);
        renters = (renters == null ? new ArrayList<>() : renters);
    }

    //getter setter
    public List<String> getTableHeader() {
        return tableHeader;
    }
    public void setTableHeader(List<String> tableHeader) {
        this.tableHeader = tableHeader;
    }

    //methods
    @Override
    public boolean add() {
        String name = (String) getValidInput(String.class,
                "Enter renter name: ",
                InputValidator::NoCondition
        );

        Date dob = (Date) getValidInput(Date.class,
                "Enter renter date of birth (dd/MM/yyyy): ",
                InputValidator::isValidDateFormat
        );

        String email = (String) getValidInput(String.class,
                "Enter renter contact(phone/email): ",
                InputValidator::isValidContact
        );

        Renter renter = new Renter(name, dob, email);
        if(!InputValidator.confirm("Do you want to add this renter? ")) return false;
        boolean isDone = db.add(renter);

        if(isDone) UserInterfaceManager.successMessage("Renter added successfully");
        UserInterfaceManager.pressAnyKeyToContinue();
        return isDone;
    }

    @Override
    public boolean remove() {
        if(renters.isEmpty()){
            UserInterfaceManager.errorMessage("No renter found");
            return false;
        }

        UserInterfaceManager.printTable("ALL RENTERS", tableHeader, convertToTable(renters));
        int index = (int) getValidInput(Integer.class,
                "Enter renter index to remove (0 to exit): ",
                input->isValidOption(input, 0, renters.size())
        );

        if(index == 0) return false;
        Renter tmp = renters.get(index - 1);
        tmp.preview();
        if(!InputValidator.confirm("Do you want to delete? ")) return false;

        // not allow to delete if have any relations
        if(!tmp.getPayments().isEmpty()){
            UserInterfaceManager.errorMessage("Cannot remove renter with payment list > 0");
            return false;
        }
        if(!tmp.getAgreementList().isEmpty()){
            UserInterfaceManager.errorMessage("Cannot remove renter with agreement list > 0");
            return false;
        }

        boolean isDeleted = db.delete(tmp);
        UserInterfaceManager.successMessage("Renter removed successfully");
        UserInterfaceManager.pressAnyKeyToContinue();
        return isDeleted;
    }

    @Override
    public boolean update() {
        if(renters.isEmpty()){
            UserInterfaceManager.errorMessage("No renter found");
            return false;
        }

        UserInterfaceManager.printTable("ALL RENTERS", tableHeader, convertToTable(renters));
        int index = (int)getValidInput(Integer.class,
                "Enter renter index to update (0 to exit): ",
                input->isValidOption(input, 0, renters.size())
        );

        if(index == 0) return false;
        Renter tmp = renters.get(index - 1);
        tmp.preview();

        String newName = null;
        String newContact = null;
        Date newDob = null;

        if(InputValidator.confirm("Do you want to update name? ")){
             newName = (String)getValidInput(String.class, "Enter new name: ",
                    InputValidator::NoCondition
             );

        }
        if(InputValidator.confirm("Do you want to update date of birth? ")){
             newDob = (Date) getValidInput(Date.class, "Enter new date of birth (dd/MM/yyyy): ",
                    InputValidator::isValidDateFormat
             );
        }
        if(InputValidator.confirm("Do you want to update contact? ")){
             newContact = (String) getValidInput(String.class, "Enter new contact: ",
                    InputValidator::isValidContact
             );
        }

        if (!InputValidator.confirm("Do you want to save changes? ")) return false;

        if(newName != null) tmp.setName(newName);
        if(newDob != null) tmp.setDateOfBirth(newDob);
        if(newContact != null) tmp.setContact(newContact);

        UserInterfaceManager.successMessage("Renter updated successfully");
        UserInterfaceManager.pressAnyKeyToContinue();

        return true;
    }

    @Override
    public boolean displayAll() {
        if(renters.isEmpty()){
            UserInterfaceManager.errorMessage("No renter found");
            return false;
        }

        UserInterfaceManager.printTable("ALL RENTERS", tableHeader, convertToTable(renters));
        int index = (int) getValidInput(Integer.class,
                "Enter renter index to view detail (0 to exit): ",
                input->isValidOption(input, 0, db.getAll(Renter.class).size())
        );

        if(index == 0) return false;
        renters.get(index - 1).preview(); // show details

        UserInterfaceManager.pressAnyKeyToContinue();
        return true;
    }

    @Override
    public List<List<String>> convertToTable(List<?> objects) {
        List<List<String>> data = new ArrayList<>();
        for (Renter o: (List<Renter>)objects){
            List<String> tmp = new ArrayList<>();

            tmp.add(o.getId() + "");
            tmp.add(o.getName());
            tmp.add(DateCreator.formatDate(o.getDateOfBirth()));
            tmp.add(o.getContact());
            tmp.add(o.getPayments().size() + "");
            tmp.add(o.getAgreementList().size() + "");
            data.add(tmp);
        }
        return data;
    }

    @Override
    public void makeReport() {
        if(renters.isEmpty()){
            UserInterfaceManager.errorMessage("No renter found");
            return;
        }

        List<Renter> rentersCpy = new ArrayList<>(renters);
        UserInterfaceManager.printMenu("SORT RENTER'S INFORMATION BY: ",
                List.of("By ID",
                        "By date of birth",
                        "By number of agreements",
                        "By number of payments",
                        "No filter"
                )
        );
        int opt = (int) getValidInput(Integer.class, "Enter your option: ", input->isValidOption(input, 0, 4));

        if(opt == 1) rentersCpy = RentalApplication.mergeSort(rentersCpy, 0, rentersCpy.size() - 1, BY_ID);
        else if(opt == 2) rentersCpy = RentalApplication.mergeSort(rentersCpy, 0, rentersCpy.size() - 1, BY_DOB);
        else if(opt == 3) rentersCpy = RentalApplication.mergeSort(rentersCpy, 0, rentersCpy.size() - 1, BY_AGREEMENT_SIZE);
        else if(opt == 4) rentersCpy = RentalApplication.mergeSort(rentersCpy, 0, rentersCpy.size() - 1, BY_PAYMENT_SIZE);

        UserInterfaceManager.printTable("ALL RENTERS", tableHeader, convertToTable(rentersCpy));
        List<Renter> finalRentersCpy = rentersCpy;
        UserInterfaceManager.printToFile(MangerInterface.REPORT_PATH,
                out -> UserInterfaceManager.printTable(
                        "ALL RENTERS",
                        tableHeader,
                        convertToTable(finalRentersCpy)
                )
        );
    }
}

