package Manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import Database.*;
import Model.*;
import UIHelper.UserInterfaceManager;
import UIHelper.InputValidator;

import static Model.RentalApplication.mergeSort;
import static UIHelper.InputValidator.*;

public class PropertyManager implements MangerInterface{

    private final Database db;
    private MangerInterface manager;
    private List<Property> properties;

    public static final Comparator<Property> BY_ID = Comparator.comparingInt(Property::getId);
    public static final Comparator<Property> BY_STATUS = Comparator.comparing(Property::getStatus);
    public static final Comparator<Property> BY_HOST_SIZE = Comparator.comparingInt(o->o.getHosts().size());
    public static final Comparator<Property> BY_AGREEMENT_SIZE = Comparator.comparingInt(o->o.getAgreementList().size());
    public static final Comparator<Property> BY_PRICE = Comparator.comparingDouble(Property::getPrice);
//    public static final Comparator<Property> BY_TYPE = Comparator.comparingInt(Property == );

    private final List<String> tableHeader = Arrays.asList("ID", "OWNER",  "ADDRESS", "PRICE", "STATUS", "TYPE", "ROOM", "BEDROOM", "PET", "GARDEN", "BUSINESS TYPE", "PARKING SPACE", "AREA(SQM)");
    public PropertyManager(Database db) {
        this.db = db;
        properties = (List<Property>)db.getAll(Property.class);
        if(properties == null) properties = new ArrayList<>();
    }

    private void setManagerType(String type){
        if(type.equals("owner")) manager = new OwnerManager(db);
        if(type.equals("property")) manager = new PropertyManager(db);
        else if(type.equals("renter")) manager = new RenterManager(db);
        else if(type.equals("host")) manager = new HostManager(db);
        else if(type.equals("agreement")) manager = new RentalAgreementManager(db);
    }

    public List<String> getTableHeader() {
        return tableHeader;
    }

    @Override
    public boolean add() {
        List<Owner> owners  = (List<Owner>) db.getAll(Owner.class);
        if(owners.isEmpty()){
            UserInterfaceManager.errorMessage("No owners found");
            return false;
        }
        setManagerType("owner");
        UserInterfaceManager.printTable("ALL OWNERS", ((OwnerManager)manager).getTableHeader(), manager.convertToTable(owners));
        int index = (int) getValidInput(Integer.class, "Enter the index of the owner you want to add property to: ",
                input->isValidOption(input, 1, owners.size()));
        Owner owner = owners.get(index - 1);
        setManagerType("property");
        String address = (String) getValidInput(String.class, "Enter property address: ",
                InputValidator::NoCondition);
        Double price = (Double) getValidInput(Double.class, "Enter property price: ",
                InputValidator::isValidPrice);
        String type = (String) getValidInput(String.class, "Enter property type(residential/commercial): ",
                input->isValidType(input, Arrays.asList("RESIDENTIAL", "COMMERCIAL")));
        String status = (String) getValidInput(String.class,
                "Enter property status(unavailable/available/rented/under_maintenance): ",
                input->isValidType(input, Arrays.asList("UNDER_MAINTENANCE", "UNAVAILABLE", "AVAILABLE", "RENTED"))
        );

        if(type.equalsIgnoreCase("RESIDENTIAL")){
            int rooms = (int) getValidInput(Integer.class, "Enter number of rooms: ",
                    input->isValidOption(input, 1, 100));
            int bedrooms = (int) getValidInput(Integer.class, "Enter number of bedrooms: ",
                    input->isValidOption(input, 0, rooms));
            boolean petAllow = confirm("Is Pet Allowed?: ");
            boolean hasGarden = confirm("Has Garden?: ");

            Property property = new ResidentialProperty(address, price,
                    PropertyStatus.valueOf(status.toUpperCase()),
                    rooms, bedrooms, petAllow, hasGarden
            );
            owner.addProperty(property);
            if(!InputValidator.confirm("Do you want to add this property? ")) return false;
            db.add(property);
            UserInterfaceManager.successMessage("Property added successfully");
            return true;
        }
        else if(type.equalsIgnoreCase("COMMERCIAL")){
            String businessType = (String) getValidInput(String.class, "Enter business type: ",
                    InputValidator::NoCondition);
            int parkingSlots = (int) getValidInput(Integer.class, "Enter number of parking slots: ",
                    input->isValidOption(input, 0, 10000));
            double squareMeter = (double) getValidInput(Double.class, "Enter square meter: ",
                    InputValidator::isValidPrice);

            Property property = new CommercialProperty(address, price,
                    PropertyStatus.valueOf(status.toUpperCase()),
                    businessType, parkingSlots, squareMeter
            );
            owner.addProperty(property);
            if(!InputValidator.confirm("Do you want to add this property? ")) return false;
            db.add(property);
            UserInterfaceManager.successMessage("Property added successfully");
            return true;
        }
        throw new IllegalArgumentException("Invalid property type");
    }

    @Override
    public boolean remove() {
//        List<Property> properties = (List<Property>) db.getAll(Property.class);
        if(properties.isEmpty()){
            UserInterfaceManager.errorMessage("No Property found");
            return false;
        }
        UserInterfaceManager.printTable("ALL PROPERTIES", tableHeader, convertToTable(properties));

        int index = (int) getValidInput(Integer.class,
                "Enter property index to remove (0 to exit): ",
                input->isValidOption(input, 0, properties.size()));
        if(index == 0) return false;
        Property property = properties.get(index - 1);
        if(!property.getAgreementList().isEmpty()){
            UserInterfaceManager.errorMessage("Property is in agreement, cannot be removed");
            return false;
        }
        property.preview();
        if(!confirm("Do you want to delete? ")) return false;
        property.remove();
        boolean isDeleted = db.delete(property);
        UserInterfaceManager.successMessage("Property removed successfully");
        UserInterfaceManager.pressAnyKeyToContinue();
        return isDeleted;
    }

    @Override
    public boolean update() {
//        List<Property> properties = (List<Property>) db.getAll(Property.class);
        if(properties.isEmpty()){
            UserInterfaceManager.errorMessage("No Property found");
            return false;
        }
        UserInterfaceManager.printTable("ALL PROPERTIES", tableHeader, convertToTable(properties));
        int index = (int)getValidInput(Integer.class, "Enter Property index to update (0 to exit): ",
                input->isValidOption(input, 0, properties.size()));
        if(index == 0) return false;
        Property tmp = properties.get(index - 1);
        tmp.preview();

        String newAddress = null;
        Owner newOwner = null;
        String newStatus = null;
        double newPrice = -1;
        String newBusinessType = null;
        int newParkingSpace = -1;
        double newSquareMeter = -1;
        int newTotalRoom = -1;
        int newTotalBedroom = -1;
        boolean newPetAllowed = false;
        boolean newHasGarden = false;

        if(confirm("Do you want to update address? ")){
            newAddress = (String)getValidInput(String.class, "Enter new address: ",
                    InputValidator::NoCondition);
        }
        if(confirm("Do you want to update price? ")){
            newPrice = (double)getValidInput(Double.class, "Enter new price: ",
                    InputValidator::isValidPrice);
        }
        if(confirm("Do you want to update status? ")){
            newStatus = (String)getValidInput(String.class, "Enter new status(UNAVAILABLE/AVAILABLE/RENTED/UNDER_MAINTENANCE): ",
                    input->isValidType(input, Arrays.asList(
                            "UNDER_MAINTENANCE",
                            "UNAVAILABLE",
                            "AVAILABLE",
                            "RENTED"))
            );
        }
        if(confirm("Do you want to update owner? ")){

            List<Owner> owners  = (List<Owner>) db.getAll(Owner.class);
            if(!owners.isEmpty()) {
                this.setManagerType("owner");
                UserInterfaceManager.printTable("ALL OWNERS", ((OwnerManager)manager).getTableHeader(), manager.convertToTable(owners));
                int ownerIndex = (int) getValidInput(Integer.class,
                        "Enter the index of the owner you want to add property to: ",
                        input -> isValidOption(input, 1, owners.size())
                );
                newOwner = owners.get(ownerIndex - 1);
            }
            else UserInterfaceManager.errorMessage("No owners found");

        }
        if(tmp instanceof CommercialProperty){


            if(InputValidator.confirm("Do you want to update business type? ")){
                 newBusinessType = (String) getValidInput(String.class, "Enter new business type: ",
                        InputValidator::NoCondition);
            }
            if(InputValidator.confirm("Do you want to update total parking space? ")) {
                 newParkingSpace = (int) getValidInput(Integer.class, "Enter new total parking space: ",
                        input -> isValidOption(input, 1, 100));
            }
            if(InputValidator.confirm("Do you want to update square meters? ")){
                 newSquareMeter = (double) getValidInput(Double.class, "Enter new square meter: ",
                        InputValidator::isValidPrice);
            }

        }
        else {
            if(InputValidator.confirm("Do you want to update total room? ")){
                 newTotalRoom = (int) getValidInput(Integer.class, "Enter new total room: ",
                        input -> isValidOption(input, 1, 100));
            }
            if(InputValidator.confirm("Do you want to update total bedroom? ")) {
                 newTotalBedroom = (int) getValidInput(Integer.class, "Enter new total bedroom: ",
                        input -> isValidOption(input, 1, 100));
            }
            if(InputValidator.confirm("Do you want to update pet allowed? ")) {
                newPetAllowed = InputValidator.confirm("Is Pet Allowed?: ");
            }
            if(InputValidator.confirm("Do you want to update has garden? ")) {
                newHasGarden = InputValidator.confirm("Has Garden?: ");
            }
        }

        if (!confirm("Do you want to save changes? ")) return false;

        if(newOwner != null){
            if(tmp.getOwner() != null) tmp.getOwner().getPropertiesOwned().remove(tmp);
            tmp.setOwner(newOwner);
            if(!newOwner.getPropertiesOwned().equals(tmp))  newOwner.addProperty(tmp);
        }
        if(newAddress != null) tmp.setAddress(newAddress);
        if (newPrice != -1) tmp.setPrice(newPrice);
        if(newStatus != null) tmp.setStatus(PropertyStatus.valueOf(newStatus.toUpperCase()));
        if(tmp instanceof CommercialProperty){
            if(newBusinessType != null) ((CommercialProperty) tmp).setBusinessType(newBusinessType);
            if(newParkingSpace != -1) ((CommercialProperty) tmp).setTotalParkingSpace(newParkingSpace);
            if(newSquareMeter != -1) ((CommercialProperty) tmp).setSquareMeters(newSquareMeter);
        }
        else{
            if(newTotalRoom != -1) ((ResidentialProperty) tmp).setTotalRoom(newTotalRoom);
            if(newTotalBedroom != -1) ((ResidentialProperty) tmp).setTotalBedroom(newTotalBedroom);
            ((ResidentialProperty) tmp).setPetAllowed(newPetAllowed | ((ResidentialProperty) tmp).isPetAllowed());
            ((ResidentialProperty) tmp).setHasGarden(newHasGarden | ((ResidentialProperty) tmp).isHasGarden());
        }
        UserInterfaceManager.successMessage("Property updated successfully");
        UserInterfaceManager.pressAnyKeyToContinue();

        return true;
    }

    @Override
    public boolean displayAll() {
        List<Property> properties = (List<Property>) db.getAll(Property.class);
        if(properties.isEmpty()){
            UserInterfaceManager.errorMessage("No Property found");
            return false;
        }
        UserInterfaceManager.printTable("ALL PROPERTIES", tableHeader, convertToTable(properties));
        int index = (int) getValidInput(Integer.class, "Enter property index to view detail (0 to exit): ",
                input->isValidOption(input, 0, properties.size()));
        if(index == 0) return false;
        Property tmp = (Property)db.getByIndex(Property.class, index - 1);
        tmp.preview();
        UserInterfaceManager.pressAnyKeyToContinue();
        return true;
    }
    @Override
    public List<List<String>> convertToTable(List<?> objects) {
        List<List<String>> data = new ArrayList<>();
        for (Property o: (List<Property>)objects){
            List<String> tmp = new ArrayList<>();
            String propertyType = o instanceof CommercialProperty ? "Commercial" : "Residential";
            String businessType = "";
            String parkingSpace = "";
            String squareMeter = "";
            String totalRoom = "";
            String totalBedroom = "";
            String petAllowed = "";
            String hasGarden = "";

            if(o instanceof CommercialProperty){
                businessType = ((CommercialProperty) o).getBusinessType();
                parkingSpace = String.valueOf(((CommercialProperty) o).getTotalParkingSpace());
                squareMeter = String.valueOf(((CommercialProperty) o).getSquareMeters());
            }
            else{
                totalRoom = String.valueOf(((ResidentialProperty) o).getTotalRoom());
                totalBedroom = String.valueOf(((ResidentialProperty) o).getTotalBedroom());
                petAllowed = ((ResidentialProperty) o).isPetAllowed() ? "YES" : "NO";
                hasGarden = ((ResidentialProperty) o).isHasGarden() ? "YES" : "NO";
            }

            tmp.add(String.valueOf(o.getId()));
            tmp.add(o.getOwner() == null ? "" : o.getOwner().getName());
            tmp.add(o.getAddress());
            tmp.add(String.valueOf(o.getPrice()));
            tmp.add(o.getStatus().toString());
            tmp.add(propertyType);
            tmp.add(totalRoom);
            tmp.add(totalBedroom);
            tmp.add(petAllowed);
            tmp.add(hasGarden);
            tmp.add(businessType);
            tmp.add(parkingSpace);
            tmp.add(squareMeter);


            data.add(tmp);
        }
        return data;
    }

    @Override
    public void makeReport() {
        List<Property> obj_cpy = new ArrayList<>(properties);
        if(obj_cpy.isEmpty()){
            UserInterfaceManager.errorMessage("No property found");
            return;
        }
        UserInterfaceManager.printMenu("SORT PROPERTY'S INFORMATION BY: ",
                List.of("By ID",
                        "By price",
                        "By status",
                        "By number of agreements",
                        "By number of hosts",
                        "No filter"
                )
        );
        int opt = (int) getValidInput(Integer.class,
                "Select the filter(0 to default) : ",
                input->isValidOption(input, 0, 5));

        if(opt == 1) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_ID);
        else if(opt == 2) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_PRICE);
        else if(opt == 3) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_STATUS);
        else if(opt == 4) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_AGREEMENT_SIZE);
        else if(opt == 5) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_HOST_SIZE);

        UserInterfaceManager.printTable("PROPERTY REPORT", tableHeader, convertToTable(obj_cpy));
//        AsciiTable.pressAnyKeyToContinue();
        List<Property> finalObj = obj_cpy;
        UserInterfaceManager.printToFile(MangerInterface.REPORT_PATH,
                out -> UserInterfaceManager.printTable(
                        "ALL PROPERTIES",
                        tableHeader,
                        convertToTable(finalObj)
                )
        );
    }


}
