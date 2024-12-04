package Manager;

import Database.Database;
import Model.*;
import UIHelper.UserInterfaceManager;
import UIHelper.DateCreator;
import UIHelper.InputValidator;

import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static Model.RentalApplication.mergeSort;
import static UIHelper.InputValidator.getValidInput;
import static UIHelper.InputValidator.isValidOption;

public class RentalAgreementManager implements MangerInterface{
    public static final Comparator<RentalAgreement> BY_ID = Comparator.comparingInt(RentalAgreement::getAgreementId);
    public static final Comparator<RentalAgreement> BY_FEE = Comparator.comparingDouble(RentalAgreement::getRentingFee);
    public static final Comparator<RentalAgreement> BY_PAYMENT_SIZE  = Comparator.comparingInt(o->o.getPayments().size());
    public static final Comparator<RentalAgreement> BY_DATE = Comparator.comparing(RentalAgreement::getContractDate);
    public static final Comparator<RentalAgreement> BY_STATUS = Comparator.comparing(RentalAgreement::getStatus);
    private final Database db;
    private List<RentalAgreement> agreements;
    private MangerInterface manager;

    private final List<String> tableHeader = List.of(
            "Agreement ID", "Main Tenant", "SubTenants", "PropertyID", "Host", "Period", "Contract Date", "Renting Fee", "Status"
    );

    public RentalAgreementManager(Database db) {
        this.db = db;
        this.agreements = (List<RentalAgreement>) db.getAll(RentalAgreement.class);
    }
    public void setManagerType(String type) {
        if(type.equals("renter")) manager = new RenterManager(db);
        else if(type.equals("property")) manager = new PropertyManager(db);
        else if(type.equals("host")) manager = new HostManager(db);
        else if(type.equals("owner")) manager = new OwnerManager(db);
        else if(type.equals("agreement")) manager = new RentalAgreementManager(db);
        else if(type.equals("payment")) manager = new PaymentManager(db);
        else throw new IllegalArgumentException("Invalid manager type");
    }
    public List<String> getTableHeader() {
        return tableHeader;
    }

    @Override
    public boolean add() {
        List<Property> properties = (List<Property>) db.getAll(Property.class);
        List<Host> hosts = (List<Host>)db.getAll(Host.class);
        List<Renter> renters = (List<Renter>)db.getAll(Renter.class);
        if(properties.isEmpty() || hosts.isEmpty() || renters.isEmpty()){
            UserInterfaceManager.errorMessage("No property or host found");    // one of these missing --> false
            return false;
        }
        /////////////////
        this.setManagerType("property");
        UserInterfaceManager.printTable(
                "ALL PROPERTIES",
                ((PropertyManager)manager).getTableHeader(),
                manager.convertToTable(properties)
        );
        int index = (int) InputValidator.getValidInput(
                Integer.class,
                "Enter property index to add agreement( 0 to exit): ",
                input-> InputValidator.isValidOption(input, 0, properties.size())
        );
        if(index == 0) return false;
        Property property = properties.get(index - 1);
        /////////
        this.setManagerType("host");
        UserInterfaceManager.printTable(
                "ALL HOSTS",
                ((HostManager)manager).getTableHeader(),
                manager.convertToTable(hosts)
        );
        index = (int) InputValidator.getValidInput(
                Integer.class,
                "Enter host index to add agreement( 0 to exit): ",
                input-> InputValidator.isValidOption(input, 0, hosts.size())
        );
        if(index == 0) return false;
        Host host = hosts.get(index - 1);
        //////////////
        this.setManagerType("renter");
        UserInterfaceManager.printTable(
                "ALL RENTERS",
                ((RenterManager)manager).getTableHeader(),
                manager.convertToTable(renters)
        );
        index = (int) InputValidator.getValidInput(Integer.class, "Enter MAIN renter index: ( 0 to exit): ",
                input-> InputValidator.isValidOption(input, 0, renters.size()));
        if(index == 0) return false;
        Renter mainRenter = renters.get(index - 1);
        ///////////

        List<Renter> subRenter = new ArrayList<>();
        int subRenterNum = (int) InputValidator.getValidInput(
                Integer.class,
                "Enter number of sub-renters: ",
                input-> InputValidator.isValidOption(input, 0, renters.size() - 1)
        );
        if(subRenterNum > 0){
            while(subRenterNum > 0){
                int subRenter_index = (int) InputValidator.getValidInput(
                        Integer.class,
                        "Enter sub-renter index: ",
                        input-> InputValidator.isValidOption(input, 1, renters.size())
                );
                Renter tmp = renters.get(subRenter_index - 1);
                if(tmp.equals(mainRenter) || subRenter.contains(tmp)){
                    UserInterfaceManager.errorMessage("Invalid sub-renter");
                    continue;
                }
                subRenter.add(tmp);
                subRenterNum--;
            }
        }
        String rentalPeriod = (String) InputValidator.getValidInput(
                String.class,
                "Enter rental period(daily, weekly, monthly, fortnightly): ",
                input-> InputValidator.isValidType(input, Arrays.asList(
                        "DAILY",
                        "WEEKLY",
                        "MONTHLY",
                        "FORTNIGHTLY")
                )
        );

        double rentPrice = (double) InputValidator.getValidInput(
                Double.class,
                "Enter rent amount: ",
                InputValidator::isValidPrice
        );
        Date contractDate = (Date) InputValidator.getValidInput(
                Date.class,
                "Enter contract date (dd/MM/yyyy): ",
                InputValidator::isValidDateFormat
        );
        String agreementStatus = (String) InputValidator.getValidInput(
                String.class,
                "Enter agreement status(new, active, completed): ",
                input->InputValidator.isValidType(input, Arrays.asList(
                        "NEW",
                        "ACTIVE",
                        "COMPLETED"
                ))
        );

        this.setManagerType("agreement");
        if(!InputValidator.confirm("Do you want to add this agreement? ")) return false;
        RentalAgreement newRA = new RentalAgreement();

        //renter & agreement
        newRA.setMainTenant(mainRenter);
        mainRenter.getAgreementList().add(newRA);
        //set sub renter
        newRA.setSubTenants(subRenter);
        for(Renter r: subRenter){
            if(!r.getAgreementList().contains(newRA)) r.getAgreementList().add(newRA);
        }
        //set property
        newRA.setProperty(property);
        property.getAgreementList().add(newRA);
        //set host
        newRA.setHost(host); // host & agreement
        host.getRentalAgreements().add(newRA);
        // host & property
        if(!property.getHosts().contains(host)) property.getHosts().add(host);
        if(!host.getPropertiesManaged().contains(property)) host.getPropertiesManaged().add(property);
        // host & owner
        Owner o = property.getOwner();
        if(!host.getCooperatingOwners().contains(o)) host.getCooperatingOwners().add(o);
        if(!o.getHosts().contains(host)) o.getHosts().add(host);

        //set period, date, fee, status
        newRA.setPeriod(RentalPeriod.valueOf(rentalPeriod.toUpperCase()));
        newRA.setContractDate(contractDate);
        newRA.setRentingFee(rentPrice);
        newRA.setStatus(AgreementStatus.valueOf(agreementStatus.toUpperCase()));
        //
        boolean isDone = db.add(newRA);
        if(isDone) UserInterfaceManager.successMessage("Agreement added successfully");
        else UserInterfaceManager.errorMessage("Failed to add agreement");
        return isDone;
    }
    @Override
    public boolean remove() {
        if(agreements.isEmpty()){
            UserInterfaceManager.errorMessage("No agreement found");
            return false;
        }

        UserInterfaceManager.printTable(
                "ALL AGREEMENTS",
                tableHeader,
                convertToTable(agreements)
        );
        int index = (int) InputValidator.getValidInput(
                Integer.class,
                "Enter agreement index to remove (0 to exit): ",
                input-> InputValidator.isValidOption(input, 0, agreements.size())
        );

        if(index == 0) return false;
        RentalAgreement tmp = agreements.get(index - 1);
        tmp.preview();

        if(!InputValidator.confirm("Do you want to delete? ")) return false;
        if (!tmp.getStatus().equals(AgreementStatus.COMPLETED)){
            UserInterfaceManager.errorMessage("Cannot remove active agreement");
            return false;
        }


        tmp.getMainTenant().getAgreementList().remove(tmp);                         // remove MAIN renter
        for(Renter r: tmp.getSubTenants()) r.getAgreementList().remove(tmp);        // remove subrenter
        tmp.getProperty().getAgreementList().remove(tmp);                           // remove property
        tmp.getHost().getRentalAgreements().remove(tmp);                            // remove host

        boolean isDeleted = db.delete(tmp);
        if(isDeleted) UserInterfaceManager.successMessage("Agreement removed successfully");
        else UserInterfaceManager.errorMessage("Failed to remove agreement");
        return isDeleted;
    }
    @Override
    public boolean update() {
        if(agreements.isEmpty()){
            UserInterfaceManager.errorMessage("No agreement found");
            return false;
        }
        UserInterfaceManager.printTable(
                "ALL AGREEMENTS",
                tableHeader,
                convertToTable(agreements)
        );

        int index = (int) InputValidator.getValidInput(
                Integer.class,
                "Enter agreement index to update (0 to exit): ",
                input-> InputValidator.isValidOption(input, 0, agreements.size())
        );
        if(index == 0) return false;
        RentalAgreement agreement = agreements.get(index - 1);
        agreement.preview();

        Renter newMainRenter = null;
        List<Renter> newSubRenters = null;
        Property newProperty = null;
        Host newHost = null;
        String newPeriod = null;
        Date newContractDate = null;
        double newRentPrice = 0;
        String newStatus = null;

        if(InputValidator.confirm("Do you want to update main renter? ")){
            List<Renter> renters = (List<Renter>) db.getAll(Renter.class);
            if(renters.isEmpty()) UserInterfaceManager.errorMessage("No renter found"); // skip
            else{
                this.setManagerType("renter");
                UserInterfaceManager.printTable(
                        "ALL RENTERS",
                        ((RenterManager)manager).getTableHeader(),
                        manager.convertToTable(renters)
                );

                this.setManagerType("agreement");
                int renterIndex = (int) InputValidator.getValidInput(
                        Integer.class,
                        "Enter renter index to update main renter (0 to exit): ",
                        input-> InputValidator.isValidOption(input, 0, renters.size())
                );
                if(renterIndex != 0) newMainRenter = renters.get(renterIndex - 1);
            }
        }
        if(InputValidator.confirm("Do you wan to update sub-renters? ")){
            List<Renter> renters = (List<Renter>) db.getAll(Renter.class);
            if(renters.size() <= 1) UserInterfaceManager.errorMessage("No renter found");
            else{
                int renterNum = (int) InputValidator.getValidInput(Integer.class,
                        "Enter number of sub-renters: ",
                        input -> InputValidator.isValidOption(input, 0, renters.size() - 1)
                );
                newSubRenters = new ArrayList<>();
                this.setManagerType("renter");
                UserInterfaceManager.printTable(
                        "ALL RENTERS",
                        ((RenterManager)manager).getTableHeader(),
                        manager.convertToTable(renters)
                );
                this.setManagerType("agreement");
                for(int i = 1; i <= renterNum;){
                    int subRenterIndex = (int)InputValidator.getValidInput(
                            Integer.class,
                            "Enter renter's index to update: ",
                            input-> InputValidator.isValidOption(input, 1, renters.size())
                    );
                    Renter tmpsubRenter = renters.get(subRenterIndex - 1);

                    if(tmpsubRenter.equals(newMainRenter)) UserInterfaceManager.errorMessage("sub-renter must not new main renter");
                    else if(newSubRenters.contains(tmpsubRenter)) UserInterfaceManager.errorMessage("sub-renter already exists");
                    else if (tmpsubRenter.equals(agreement.getMainTenant())) UserInterfaceManager.errorMessage("sub-renter must not current main renter");
                    else{
                        newSubRenters.add(tmpsubRenter);
                        i++;
                    }
                }
            }
        }
        if(InputValidator.confirm("Do you want to update property? ")){
            List<Property> properties = (List<Property>) db.getAll(Property.class);
            if(properties.isEmpty()) UserInterfaceManager.errorMessage("No property found");
            else{
                this.setManagerType("property");
                UserInterfaceManager.printTable(
                        "ALL PROPERTIES",
                        ((PropertyManager)manager).getTableHeader(),
                        manager.convertToTable(properties)
                );
                this.setManagerType("agreement");
                int propertyIndex = (int) InputValidator.getValidInput(
                        Integer.class,
                        "Enter property index to update (0 to exit): ",
                        input-> InputValidator.isValidOption(input, 0, properties.size())
                );
                if(propertyIndex != 0) newProperty = properties.get(propertyIndex - 1);
            }
        }
        if(InputValidator.confirm("Do you want to update host? ")){
            List<Host> hosts = (List<Host>) db.getAll(Host.class);
            if(hosts.isEmpty()) UserInterfaceManager.errorMessage("No host found");
            else{
                this.setManagerType("host");
                UserInterfaceManager.printTable(
                        "ALL HOSTS",
                        ((HostManager)manager).getTableHeader(),
                        manager.convertToTable(hosts)
                );
                this.setManagerType("agreement");
                int hostIndex = (int) InputValidator.getValidInput(Integer.class,
                        "Enter host index to update (0 to exit): ",
                        input-> InputValidator.isValidOption(input, 0, hosts.size())
                );
                if(hostIndex != 0) newHost = hosts.get(hostIndex - 1);
            }
        }
        if(InputValidator.confirm("Do you want to update rental period? ")){
            newPeriod = (String) InputValidator.getValidInput(
                    String.class,
                    "Enter rental period: ",
                    input-> InputValidator.isValidType(input, Arrays.asList(
                            "DAILY",
                            "WEEKLY",
                            "MONTHLY",
                            "FORTNIGHTLY"
                    ))
            );
        }
        if(InputValidator.confirm("Do you want to update contract date? ")){
            newContractDate = (Date) InputValidator.getValidInput(
                    Date.class,
                    "Enter contract date (dd/MM/yyyy): ",
                    InputValidator::isValidDateFormat);
        }
        if(InputValidator.confirm("Do you want to update rent price? ")){
            newRentPrice = (double) InputValidator.getValidInput(
                    Double.class,
                    "Enter rent amount: ",
                    InputValidator::isValidPrice
            );
        }
        if(InputValidator.confirm("Do you want to update agreement status? ")){
            newStatus = (String) InputValidator.getValidInput(
                    String.class,
                    "Enter agreement status: ",
                    input->InputValidator.isValidType(input, Arrays.asList(
                            "NEW",
                            "ACTIVE",
                            "COMPLETED"
                    ))
            );
        }
        if(!InputValidator.confirm("Do you want to save changes? ")) return false;
        if(newMainRenter != null) {
            if(agreement.getMainTenant() != null)  agreement.getMainTenant().getAgreementList().remove(agreement); // remove old
            newMainRenter.getAgreementList().add(agreement);     // new & agreement
            agreement.setMainTenant(newMainRenter);
        }
        if(newSubRenters != null){
            for(Renter r: agreement.getSubTenants()) r.getAgreementList().remove(agreement); // remove all old
            for(Renter r: newSubRenters) r.getAgreementList().add(agreement); // add new
            agreement.setSubTenants(newSubRenters);
        }
        if(newProperty != null){
            Property oldProp = agreement.getProperty();
            if(oldProp != null){
                oldProp.getAgreementList().remove(agreement);   // remove the old property
            }


            //add agreement to property
            newProperty.getAgreementList().add(agreement);
            agreement.setProperty(newProperty);

            // current host & new property
            Host currentHost = agreement.getHost();
            if(!currentHost.getPropertiesManaged().contains(newProperty)) currentHost.getPropertiesManaged().add(newProperty);
            if(!newProperty.getHosts().contains(currentHost)) newProperty.getHosts().add(currentHost);
        }
        if(newHost != null){
            Owner currentOwner = agreement.getProperty().getOwner();
            Host currentHost = agreement.getHost();
            Property currentProperty = agreement.getProperty();

            // delete the old host
            currentHost.getRentalAgreements().remove(agreement);
            // add agreement to new host
            newHost.getRentalAgreements().add(agreement);
            agreement.setHost(newHost);

            //add owner to new host
            if(!newHost.getCooperatingOwners().contains(currentOwner)) newHost.getCooperatingOwners().add(currentOwner);
            if(!currentOwner.getHosts().contains(newHost)) currentOwner.getHosts().add(newHost);

            //add property's agreement to host
            if(!currentProperty.getHosts().contains(newHost)) agreement.getProperty().getHosts().add(newHost);
            if(!newHost.getPropertiesManaged().contains(agreement)) newHost.getPropertiesManaged().add(currentProperty);

        }
        if(newPeriod != null) agreement.setPeriod(RentalPeriod.valueOf(newPeriod.toUpperCase()));
        if(newContractDate != null) agreement.setContractDate(newContractDate);
        if(newRentPrice != 0) agreement.setRentingFee(newRentPrice);
        if(newStatus != null) agreement.setStatus(AgreementStatus.valueOf(newStatus.toUpperCase()));
        UserInterfaceManager.successMessage("Agreement updated successfully");
        return true;
    }
    @Override
    public boolean displayAll() {
        if(agreements.isEmpty()){
            UserInterfaceManager.errorMessage("No agreement found");
            return false;
        }
        UserInterfaceManager.printTable(
                "ALL AGREEMENTS",
                tableHeader,
                convertToTable(agreements)
        );
        int index = (int) InputValidator.getValidInput(
                Integer.class,
                "Enter agreement index to view details (0 to exit): ",
                input-> InputValidator.isValidOption(input, 0, agreements.size())
        );
        if(index == 0) return false;
        RentalAgreement tmp = agreements.get(index - 1);
        tmp.preview();
        return true;
    }
    @Override
    public List<List<String>> convertToTable(List<?> objects) {
        List<List<String>> data = new ArrayList<>();

        for (RentalAgreement ra: (List<RentalAgreement>)objects){
            List<String> tmp = new ArrayList<>();
            tmp.add(ra.getAgreementId() + "");
            tmp.add(ra.getMainTenant().getName());
            tmp.add(ra.getSubTenants().size() + "");
            tmp.add(ra.getProperty().getId() + "");
            tmp.add(ra.getHost().getName());
            tmp.add(ra.getPeriod().toString());
            tmp.add(DateCreator.formatDate(ra.getContractDate()));
            tmp.add(ra.getRentingFee() + "");
            tmp.add(ra.getStatus().toString());
            data.add(tmp);
        }
        return data;
    }
    @Override
    public void makeReport() {
        try{
            FileWriter file = new FileWriter(REPORT_PATH, false);
        }
        catch (Exception e){
            UserInterfaceManager.errorMessage("Failed to create report file");
            return;
        }

        UserInterfaceManager.printToFile(REPORT_PATH,
                out->{
                    UserInterfaceManager.printMenu(
                            "ASSIGMENT 1: BUILD A CONSOLE APPLICATION",
                            List.of("Name: Khong Quoc Khanh",
                                    "Student ID: s4021494",
                                    "Course: Further Programming",
                                    "Lecturers and Tutors: Dr. Minh Vu Thanh, Dr. Dung Nguyen, Dr. Phong Ngo",
                                    "Created at: " + LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm")) + " "
                                            + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            )
                    );
                    out.println("\n");
                }

        );

        this.setManagerType("agreement");
        List<RentalAgreement> obj_cpy = new ArrayList<>(agreements);
        if(obj_cpy.isEmpty()){
            UserInterfaceManager.errorMessage("No agreement found");
//            return;
        }
        else {
            //date, price, status, payment
            UserInterfaceManager.printMenu(
                    "SORT RENTAL AGREEMENT'S INFORMATION BY: ",
                    List.of("By ID",
                            "By date",
                            "By price",
                            "By status",
                            "By Number of Payments",
                            "No filter"
                    )
            );
            int opt = (int) getValidInput(
                    Integer.class,
                    "Select the filter(0 to default) : ",
                    input -> isValidOption(input, 0, 5)
            );

            if (opt == 1) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_ID);
            else if (opt == 2) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_DATE);
            else if (opt == 3) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_FEE);
            else if (opt == 4) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_STATUS);
            else if (opt == 5) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_PAYMENT_SIZE);

            UserInterfaceManager.printTable("RENTAL AGREEMENT REPORT", tableHeader, convertToTable(obj_cpy));
            List<RentalAgreement> finalObj = obj_cpy;

            UserInterfaceManager.printToFile(MangerInterface.REPORT_PATH,
                    out -> UserInterfaceManager.printTable(
                            "ALL RENTAL AGREEMENT",
                            tableHeader,
                            convertToTable(finalObj)
                    )
            );
        }
        this.setManagerType("renter");
        manager.makeReport();
        this.setManagerType("host");
        manager.makeReport();
        this.setManagerType("owner");
        manager.makeReport();
        this.setManagerType("property");
        manager.makeReport();
        this.setManagerType("payment");
        manager.makeReport();

        UserInterfaceManager.successMessage("Report saved to \" " + MangerInterface.REPORT_PATH + " \"");
        UserInterfaceManager.pressAnyKeyToContinue();

    }
}
