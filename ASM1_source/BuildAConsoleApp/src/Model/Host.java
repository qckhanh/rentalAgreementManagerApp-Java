package Model;

import UIHelper.UserInterfaceManager;
import UIHelper.DateCreator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Host extends Person{
    private List<Property> propertiesManaged = new ArrayList<>();
    private List<Owner> cooperatingOwners = new ArrayList<>();
    private List<RentalAgreement> rentalAgreements = new ArrayList<>();

    public Host(String name, Date dateOfBirth, String contact) {
        super(name, dateOfBirth, contact);
    }

    public void addProperty(Property property){
        if(!property.getHosts().contains(this)) property.getHosts().add(this);
        this.propertiesManaged.add(property);

        Owner owner = property.getOwner();
        if(owner == null) return;
        if(!owner.getHosts().contains(this)) owner.getHosts().add(this);
        if(!this.cooperatingOwners.contains(owner)) this.cooperatingOwners.add(owner);
    }

    public void addOwner(Owner owner){
        cooperatingOwners.add(owner);
        owner.getHosts().add(this);
    }

    public List<Property> getPropertiesManaged() {
        return propertiesManaged;
    }

    public void setPropertiesManaged(List<Property> propertiesManaged) {
        this.propertiesManaged = propertiesManaged;
    }

    public List<Owner> getCooperatingOwners() {
        return cooperatingOwners;
    }

    public void setCooperatingOwners(List<Owner> cooperatingOwners) {
        this.cooperatingOwners = cooperatingOwners;
    }

    public List<RentalAgreement> getRentalAgreements() {
        return rentalAgreements;
    }

    public void setRentalAgreements(List<RentalAgreement> rentalAgreements) {
        this.rentalAgreements = rentalAgreements;
    }

    @Override
    public void preview() {
        super.preview();
        if (propertiesManaged == null || propertiesManaged.isEmpty()) UserInterfaceManager.successMessage("No property found");
        else {
            List<List<String>> data = new ArrayList<>();
            List<String> header = List.of("Property ID", "Address", "Type", "Status");
            for (Property property : propertiesManaged) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(property.getId()));
                row.add(property.getAddress());
                row.add(property instanceof CommercialProperty ? "Commercial" : "Residential");
                row.add(property.getStatus().toString());
                data.add(row);
            }
            UserInterfaceManager.printTable(this.name + "'s properties ", header, data);
        }
        if (cooperatingOwners == null || cooperatingOwners.isEmpty()) UserInterfaceManager.successMessage("No owner found");
        else {
            List<List<String>> data = new ArrayList<>();
            List<String> header = List.of("Owner ID", "Name", "Contact");
            for (Owner owner : cooperatingOwners) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf( owner != null ?  owner.getId() : "NA"));
                row.add(owner.getName());
                row.add(owner.getContact());
                data.add(row);
            }
            UserInterfaceManager.printTable("Cooperating owners ", header, data);
        }
        if (rentalAgreements == null || rentalAgreements.isEmpty()) UserInterfaceManager.successMessage("No agreement found");
        else {
            List<List<String>> data = new ArrayList<>();
            List<String> header = List.of("Agreement ID", "Property's address", "Period", "Contract Date", "Price", "Status");
            for (RentalAgreement rentalAgreement : rentalAgreements) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(rentalAgreement.getAgreementId()));
                row.add(rentalAgreement.getProperty().getAddress());
                row.add(rentalAgreement.getPeriod().toString());
                row.add(DateCreator.formatDate(rentalAgreement.getContractDate()));
                row.add(rentalAgreement.getRentingFee() + "");
                row.add(rentalAgreement.getStatus().toString());
                data.add(row);
            }
            UserInterfaceManager.printTable(this.name + "'s rental agreements ", header, data);
        }
    }
}
