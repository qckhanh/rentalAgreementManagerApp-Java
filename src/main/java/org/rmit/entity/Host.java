package org.rmit.entity;

//import UIHelper.UserInterfaceManager;
//import UIHelper.DateCreator;

import jakarta.persistence.*;

import java.util.*;


@Entity
public class Host extends Person {
    @ManyToMany
    private Set<Property> propertiesManaged = new HashSet<>();

    @OneToMany
    private Set<RentalAgreement> rentalAgreements = new HashSet<>();

    @ManyToMany
    private List<Owner> cooperatingOwners = new ArrayList<>();

    public void setCooperatingOwners(List<Owner> cooperatingOwners) {
        this.cooperatingOwners = cooperatingOwners;
    }

    // Add constructors, getters, and setters as needed


    public Set<Property> getPropertiesManaged() {
        return propertiesManaged;
    }

    public void setPropertiesManaged(Set<Property> propertiesManaged) {
        this.propertiesManaged = propertiesManaged;
    }

    public Set<RentalAgreement> getRentalAgreements() {
        return rentalAgreements;
    }

    public void setRentalAgreements(Set<RentalAgreement> rentalAgreements) {
        this.rentalAgreements = rentalAgreements;
    }

    public List<Owner> getCooperatingOwners() {
        return cooperatingOwners;
    }

}
