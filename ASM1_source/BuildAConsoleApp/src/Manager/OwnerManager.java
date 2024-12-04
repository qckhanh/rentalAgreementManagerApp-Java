package Manager;

import java.util.*;

import Database.*;
import Model.*;
import UIHelper.UserInterfaceManager;
import UIHelper.DateCreator;
import UIHelper.InputValidator;

import static Model.RentalApplication.mergeSort;
import static UIHelper.InputValidator.getValidInput;
import static UIHelper.InputValidator.isValidOption;

public class OwnerManager implements MangerInterface{
    public static final Comparator<Owner> BY_ID = Comparator.comparingInt(Owner::getId);
    public static final Comparator<Owner> BY_HOST_SIZE = Comparator.comparingInt(o->o.getHosts().size());
    public static final Comparator<Owner> BY_PROPERTY_SIZE  = Comparator.comparingInt(o->o.getPropertiesOwned().size());
    public static final Comparator<Owner> BY_DOB = Comparator.comparing(Owner::getDateOfBirth);

    private final Database db;
    private List<Owner> owners;
    public List<String> tableHeader = Arrays.asList(
            "ID", "Full Name", "Date of Birth", "Contact", "NumberProperties", "NumberHosts"
    );

    public OwnerManager(Database db) {
        this.db = db;
        owners = (List<Owner>)db.getAll(Owner.class);
    }

    public List<String> getTableHeader() {
        return tableHeader;
    }

    @Override
    public boolean add() {
        String name = (String) getValidInput(
                String.class,
                "Enter owner name: ",
                InputValidator::NoCondition
        );
        Date dob = (Date) getValidInput(
                Date.class,
                "Enter owner date of birth (dd/MM/yyyy): ",
                InputValidator::isValidDateFormat
        );
        String email = (String) getValidInput(
                String.class,
                "Enter owner contact(phone/email): ",
                InputValidator::isValidContact
        );
        if(!InputValidator.confirm("Do you want to add this owner? ")) return false;
        Owner owner = new Owner(name, dob, email);

        boolean isDone = db.add(owner);
        if(isDone) UserInterfaceManager.successMessage("Owner added successfully");
        return isDone;
    }

    @Override
    public boolean remove() {
        if(owners.isEmpty()){
            UserInterfaceManager.errorMessage("No owner found");
            return false;
        }

        UserInterfaceManager.printTable(
                "ALL OWNERS",
                tableHeader,
                convertToTable(owners)
        );

        int index = (int) getValidInput(
                Integer.class,
                "Enter renter index to remove (0 to exit): ",
                input->isValidOption(input, 0, owners.size())
        );

        if(index == 0) return false;
        Owner tmp = owners.get(index - 1);
        tmp.preview();

        if(!InputValidator.confirm("Do you want to delete? ")) return false;

        tmp.getPropertiesOwned().forEach(property -> property.setOwner(null));
        tmp.getHosts().forEach(host -> host.getCooperatingOwners().remove(tmp));

        boolean isDeleted = db.delete(tmp);
        UserInterfaceManager.successMessage("Owner removed successfully");
        return isDeleted;
    }

    @Override
    public boolean update() {
        if(owners.isEmpty()){
            UserInterfaceManager.errorMessage("No owner found");
            return false;
        }

        UserInterfaceManager.printTable(
                "Owners",
                tableHeader,
                convertToTable(owners)
        );
        int index = (int)getValidInput(
                Integer.class,
                "Enter owner index to update (0 to exit): ",
                input->isValidOption(input, 0, owners.size())
        );
        if (index == 0) return false;
        Owner tmp = owners.get(index - 1);
        tmp.preview();

        String newName = null;
        Date newDob = null;
        String newContact = null;

        if(InputValidator.confirm("Do you want to update name? ")){
             newName = (String)getValidInput(String.class, "Enter new name: ",
                    InputValidator::NoCondition);
        }
        if(InputValidator.confirm("Do you want to update date of birth? ")){
             newDob = (Date) getValidInput(Date.class, "Enter new date of birth (dd/MM/yyyy): ",
                    InputValidator::isValidDateFormat);
        }
        if(InputValidator.confirm("Do you want to update contact? ")){
             newContact = (String) getValidInput(String.class, "Enter new contact: ",
                    InputValidator::isValidContact);
        }

        if (!InputValidator.confirm("Do you want to save changes? ")) return false;

        if(newName != null) tmp.setName(newName);
        if(newDob != null) tmp.setDateOfBirth(newDob);
        if(newContact != null) tmp.setContact(newContact);
        UserInterfaceManager.successMessage("Owner updated successfully");
        return true;
    }

    @Override
    public boolean displayAll() {
        if(owners.isEmpty()){
            UserInterfaceManager.errorMessage("No owner found");
            return false;
        }
        UserInterfaceManager.printTable(
                "ALL OWNERS",
                tableHeader,
                convertToTable(owners)
        );
        int index = (int) getValidInput(
                Integer.class,
                "Enter owner index to preview (0 to exit): ",
                input->isValidOption(input, 0, owners.size())
        );
        if(index == 0) return false;
        Owner tmp = owners.get(index - 1);
        tmp.preview();
        return true;
    }

    @Override
    public List<List<String>> convertToTable(List<?> objects) {
        List<List<String>> data = new ArrayList<>();

        for (Owner o: (List<Owner>)objects){
            List<String> tmp = new ArrayList<>();
            tmp.add(o.getId() + "");
            tmp.add(o.getName());
            tmp.add(DateCreator.formatDate(o.getDateOfBirth()));
            tmp.add(o.getContact());
            tmp.add(o.getPropertiesOwned().size() + "");
            tmp.add(o.getHosts().size() + "");
            data.add(tmp);
        }
        return data;
    }

    @Override
    public void makeReport() {
        List<Owner> obj_cpy = new ArrayList<>(owners);
        if(obj_cpy.isEmpty()){
            UserInterfaceManager.errorMessage("No owners found");
            return;
        }

        UserInterfaceManager.printMenu("SORT OWNER'S INFORMATION BY: ",
                List.of("By ID",
                        "By Date of Birth",
                        "By Number of Properties",
                        "By Number of Hosts",
                        "No filter"
                )
        );
        int opt = (int) getValidInput(
                Integer.class,
                "Select the filter(0 to default) : ",
                input->isValidOption(input, 0, 4)
        );

        if(opt == 1) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_ID);
        else if(opt == 2) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_DOB);
        else if(opt == 3) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_PROPERTY_SIZE);
        else if(opt == 4) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_HOST_SIZE);

        UserInterfaceManager.printTable(
                "OWNER REPORT",
                tableHeader,
                convertToTable(obj_cpy)
        );
        List<Owner> finalObj = obj_cpy;
        UserInterfaceManager.printToFile(MangerInterface.REPORT_PATH,
                out -> UserInterfaceManager.printTable(
                        "ALL OWNERS",
                        tableHeader,
                        convertToTable(finalObj)
                )
        );
//        AsciiTable.pressAnyKeyToContinue();
    }

}
