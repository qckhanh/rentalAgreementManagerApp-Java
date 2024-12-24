package org.rmit.model.Persons;

//import UIHelper.UserInterfaceManager;
//import UIHelper.DateCreator;

import jakarta.persistence.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.rmit.model.Property.Property;
import org.rmit.model.Agreement.RentalAgreement;

import java.util.*;


@Entity
public class Host extends Person {
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Property> propertiesManaged = new HashSet<>();

    @OneToMany()
    private Set<RentalAgreement> rentalAgreements = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Owner> cooperatingOwners = new HashSet<>();
////////////////////////////////////////
    @Transient
    transient private ObjectProperty<Set<Property>> propertiesManagedProperty = new SimpleObjectProperty<>();
    @Transient
    transient private ObjectProperty<Set<RentalAgreement>> rentalAgreementsProperty = new SimpleObjectProperty<>();
    @Transient
    transient private ObjectProperty<Set<Owner>> cooperatingOwnersProperty = new SimpleObjectProperty<>();

    @Override
    protected void synWithSimpleProperty() {
        this.idPropertyProperty().setValue(this.id);
        this.nameProperty.setValue(this.name);
        this.dateOfBirthProperty.setValue(this.dateOfBirth);
        this.contactProperty.setValue(this.contact);
        this.usernameProperty.setValue(this.username);
        this.passwordProperty.setValue(this.password);

        this.propertiesManagedProperty.setValue(this.propertiesManaged);
        this.rentalAgreementsProperty.setValue(this.rentalAgreements);
        this.cooperatingOwnersProperty.setValue(this.cooperatingOwners);
    }

    public Host() {
        super();
    }



    public Set<Property> getPropertiesManaged() {
        return propertiesManaged;
    }

    public void setPropertiesManaged(Set<Property> propertiesManaged) {
        this.propertiesManagedProperty.setValue(propertiesManaged);
        this.propertiesManaged = propertiesManagedProperty.get();
    }

    public Set<RentalAgreement> getRentalAgreements() {
        return rentalAgreements;
    }

    public void setRentalAgreements(Set<RentalAgreement> rentalAgreements) {
        this.rentalAgreementsProperty.setValue(rentalAgreements);
        this.rentalAgreements = rentalAgreementsProperty.get();
    }

    public Set<Owner> getCooperatingOwners() {
        return cooperatingOwners;
    }

    public void setCooperatingOwners(Set<Owner> cooperatingOwners) {
        this.cooperatingOwnersProperty.setValue(cooperatingOwners);
        this.cooperatingOwners = cooperatingOwnersProperty.get();
    }
    /////////////


    public Set<Property> getPropertiesManagedProperty() {
        return propertiesManagedProperty.get();
    }

    public ObjectProperty<Set<Property>> propertiesManagedPropertyProperty() {
        return propertiesManagedProperty;
    }

    public Set<RentalAgreement> getRentalAgreementsProperty() {
        return rentalAgreementsProperty.get();
    }

    public ObjectProperty<Set<RentalAgreement>> rentalAgreementsPropertyProperty() {
        return rentalAgreementsProperty;
    }

    public Set<Owner> getCooperatingOwnersProperty() {
        return cooperatingOwnersProperty.get();
    }

    public ObjectProperty<Set<Owner>> cooperatingOwnersPropertyProperty() {
        return cooperatingOwnersProperty;
    }
}
