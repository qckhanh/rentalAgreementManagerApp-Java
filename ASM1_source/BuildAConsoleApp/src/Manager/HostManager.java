package Manager;

import Database.Database;
import Model.*;
import UIHelper.UserInterfaceManager;
import UIHelper.DateCreator;
import UIHelper.InputValidator;

import java.util.*;

import static Model.RentalApplication.mergeSort;
import static UIHelper.InputValidator.getValidInput;
import static UIHelper.InputValidator.isValidOption;

public class HostManager implements MangerInterface{
    public static final Comparator<Host> BY_ID = Comparator.comparingInt(Host::getId);
    public static final Comparator<Host> BY_DOB = Comparator.comparing(Host::getDateOfBirth);
    public static final Comparator<Host> BY_OWNER_SIZE = Comparator.comparingInt(o->o.getCooperatingOwners().size());
    public static final Comparator<Host> BY_AGREEMENT  = Comparator.comparingInt(o->o.getRentalAgreements().size());
    public static final Comparator<Host> BY_PROPERTY_SIZE  = Comparator.comparingInt(o->o.getPropertiesManaged().size());

    private final Database db;
    private MangerInterface manager;
    private List<Host> hosts;

    public List<String> tableHeader = Arrays.asList(
            "ID", "Full Name", "Date of Birth", "Contact", "NumberProperties", "NumberOwners", "NumberAgreements"
    );

    public HostManager(Database db) {
        this.db = db;
        hosts = (List<Host>)db.getAll(Host.class);
    }
    public List<String> getTableHeader() {
        return tableHeader;
    }
    private void setManagerType(String type){
        if(type.equals("owner")) manager = new OwnerManager(db);
        if(type.equals("property")) manager = new PropertyManager(db);
        else if(type.equals("renter")) manager = new RenterManager(db);
        else if(type.equals("host")) manager = new HostManager(db);
        else if(type.equals("agreement")) manager = new RentalAgreementManager(db);
    }
    @Override
    public boolean add() {
        String name = (String) getValidInput(
                String.class,
                "Enter host name: ",
                InputValidator::NoCondition
        );
        Date dob = (Date) getValidInput(
                Date.class,
                "Enter host date of birth (dd/MM/yyyy): ",
                InputValidator::isValidDateFormat
        );
        String email = (String) getValidInput(
                String.class,
                "Enter host contact(phone/email): ",
                InputValidator::isValidContact
        );
        //create new host
        Host tmp = new Host(name, dob, email);

        if (!InputValidator.confirm("Do you want to add this host? ")) return false;
        boolean isDone = db.add(tmp);
        if(isDone) UserInterfaceManager.successMessage("Host added successfully");
        return isDone;
    }

    @Override
    public boolean remove() {
        if(hosts.isEmpty()){     // stop if nothing to delete
            UserInterfaceManager.errorMessage("No host found");
            return false;
        }

        UserInterfaceManager.printTable(        // show all host
                "ALL HOSTS",
                tableHeader,
                convertToTable(hosts)
        );

        int index = (int) getValidInput(
                Integer.class,
                "Enter host index to remove (0 to exit): ",
                input->isValidOption(input, 0, hosts.size())
        );

        if(index == 0) return false;
        Host tmp = hosts.get(index - 1);
        tmp.preview();

        if(!InputValidator.confirm("Do you want to delete? ")) return false;

        if(!tmp.getRentalAgreements().isEmpty()) return false;      // cant remove if have agreement

        // remove connection
        for(Owner p: tmp.getCooperatingOwners()) p.getHosts().remove(tmp);
        for(Property p: tmp.getPropertiesManaged()) p.getHosts().remove(tmp);

        boolean isDeleted = db.delete(tmp);
        UserInterfaceManager.successMessage("Host removed successfully");
        return isDeleted;
    }

    @Override
    public boolean update() {
        if(hosts.isEmpty()){
            UserInterfaceManager.errorMessage("No host found");
            return false;
        }

        UserInterfaceManager.printTable(
                "ALL HOSTS",
                tableHeader,
                convertToTable(hosts)
        );

        int index = (int)getValidInput(
                Integer.class,
                "Enter renter index to update (0 to exit): ",
                input->isValidOption(input, 0, hosts.size())
        );
        if (index == 0) return false;

        Host tmp = hosts.get(index - 1);
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

        UserInterfaceManager.successMessage("Host updated successfully");
        return true;
    }

    @Override
    public boolean displayAll() {
        if(hosts.isEmpty()){
            UserInterfaceManager.errorMessage("No host found");
            return false;
        }

        UserInterfaceManager.printTable(
                "ALL HOSTS",
                tableHeader, convertToTable(hosts)
        );

        int index = (int) getValidInput(
                Integer.class,
                "Enter host index to preview (0 to exit): ",
                input-> isValidOption(input, 0, hosts.size())
        );

        if(index == 0) return false;
        Host tmp = hosts.get(index - 1);
        tmp.preview();

        return true;
    }

    @Override
    public List<List<String>> convertToTable(List<?> objects) {
        List<List<String>> data = new ArrayList<>();
        for (Host o: (List<Host>)objects){
            List<String> tmp = new ArrayList<>();
            tmp.add(o.getId() + "");
            tmp.add(o.getName());
            tmp.add(DateCreator.formatDate(o.getDateOfBirth()));
            tmp.add(o.getContact());
            tmp.add(o.getPropertiesManaged().size() + "");
            tmp.add(o.getCooperatingOwners().size() + "");
            tmp.add(o.getRentalAgreements().size() + "");
            data.add(tmp);
        }
        return data;
    }

    @Override
    public void makeReport() {
        List<Host> obj_cpy = new ArrayList<>(hosts);
        if(obj_cpy.isEmpty()){
            UserInterfaceManager.errorMessage("No hosts found");
            return;
        }

        UserInterfaceManager.printMenu("SORT HOST'S INFORMATION BY: ",
                List.of("By ID",
                        "By Date of Birth",
                        "By Number of Properties",
                        "By Number of Owners",
                        "By Number of Agreements",
                        "No filter"
                )
        );
        int opt = (int) getValidInput(
                Integer.class,
                "Select the filter(0 to default) : ",
                input->isValidOption(input, 0, 5)
        );

        if(opt == 1) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_ID);
        else if(opt == 2) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_DOB);
        else if(opt == 3) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_PROPERTY_SIZE);
        else if(opt == 4) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_OWNER_SIZE);
        else if(opt == 5) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_AGREEMENT);

        UserInterfaceManager.printTable("HOST REPORT", tableHeader, convertToTable(obj_cpy));
        List<Host> finalObj = obj_cpy;
        UserInterfaceManager.printToFile(MangerInterface.REPORT_PATH,
                out -> UserInterfaceManager.printTable(
                        "ALL HOSTS",
                        tableHeader,
                        convertToTable(finalObj)
                )
        );
//        AsciiTable.pressAnyKeyToContinue();
    }

    public boolean addToOwner(){
        List<Owner> owners = (List<Owner>) db.getAll(Owner.class);
        if(hosts.isEmpty() || owners.isEmpty()){
            UserInterfaceManager.errorMessage("No host or owner found");
            return false;
        }

        UserInterfaceManager.printTable(
                "ALL HOSTS",
                tableHeader,
                convertToTable(hosts)
        );

        int index = (int) getValidInput(
                Integer.class,
                "Enter host index to add working host( 0 to exit): ",
                input-> isValidOption(input, 0, hosts.size())
        );

        if(index == 0) return false;
        Host host = hosts.get(index - 1);

        this.setManagerType("owner");
        UserInterfaceManager.printTable(
                "ALL OWNERS",
                ((OwnerManager)manager).getTableHeader(),
                manager.convertToTable(owners)
        );

        index = (int) getValidInput(
                Integer.class,
                "Enter owner index to add host( 0 to exit): ",
                input-> isValidOption(input, 0, owners.size())
        );

        this.setManagerType("owner");
        if(index == 0) return false;
        Owner owner = owners.get(index - 1);

        // host & owner
        if(!owner.getHosts().contains(host)) owner.getHosts().add(host);
        if(!host.getCooperatingOwners().contains(owner)) host.getCooperatingOwners().add(owner);


        UserInterfaceManager.successMessage("Owner added successfully");
        return true;
    }

    public boolean addToProperty(){
        List<Property> properties = (List<Property>) db.getAll(Property.class);
        if(hosts.isEmpty() || properties.isEmpty()){
            UserInterfaceManager.errorMessage("No host or property found");
            return false;
        }

        UserInterfaceManager.printTable(
                "ALL HOSTS",
                tableHeader,
                convertToTable(hosts)
        );
        int index = (int) getValidInput(
                Integer.class,
                "Enter host index to add property(0 to exit): ",
                input-> isValidOption(input, 0, hosts.size())
        );
        if(index == 0) return false;
        Host host = hosts.get(index - 1);

        this.setManagerType("property");
        UserInterfaceManager.printTable(
                "ALL PROPERTIES",
                ((PropertyManager)manager).getTableHeader(),
                manager.convertToTable(properties)
        );

        index = (int) getValidInput(
                Integer.class,
                "Enter property index to add host(0 to exit): ",
                input-> isValidOption(input, 0, properties.size())
        );
        if(index == 0) return false;
        Property property = properties.get(index - 1);
        // host & property
        if(!host.getPropertiesManaged().contains(property)) host.getPropertiesManaged().add(property);
        if(!property.getHosts().contains(host)) property.getHosts().add(host);

        UserInterfaceManager.successMessage("Property added successfully");
        return true;
    }

}