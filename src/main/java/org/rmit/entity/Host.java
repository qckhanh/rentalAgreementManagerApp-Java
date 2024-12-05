package org.rmit.entity;

//import UIHelper.UserInterfaceManager;
//import UIHelper.DateCreator;

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

}
