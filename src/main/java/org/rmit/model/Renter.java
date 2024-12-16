package org.rmit.model;
//
//import UIHelper.UserInterfaceManager;
//import UIHelper.DateCreator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.*;

// class Tenant
@Entity(name = "Renter")
public class Renter extends Person {
    @OneToMany(mappedBy = "mainTenant")
    private Set<RentalAgreement> agreementList = new HashSet<>();

    @OneToMany(mappedBy = "mainRenter", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Payment> payments = new HashSet<>();

    @Transient
    transient private ObjectProperty<Set<RentalAgreement>> agreementListProperty = new SimpleObjectProperty<>();
    @Transient
    transient private ObjectProperty<Set<Payment>> paymentsProperty = new SimpleObjectProperty<>();

    @Override
    protected void synWithSimpleProperty() {
        this.idPropertyProperty().setValue(this.id);
        this.nameProperty.setValue(this.name);
        this.dateOfBirthProperty.setValue(this.dateOfBirth);
        this.contactProperty.setValue(this.contact);
        this.usernameProperty.setValue(this.username);
        this.passwordProperty.setValue(this.password);

        this.agreementListProperty.setValue(this.agreementList);
        this.paymentsProperty.setValue(this.payments);
    }

    //////////////////////////////////////////////////////////////////////
    public Renter(){
        super();
    }

    public Set<RentalAgreement> getAgreementList() {
        return agreementList;
    }

    public void setAgreementList(Set<RentalAgreement> agreementList) {
        this.agreementListProperty.setValue(agreementList);
        this.agreementList = agreementListProperty.get();
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.paymentsProperty.setValue(payments);
        this.payments = paymentsProperty.get();
    }

    /////////////////////

    public Set<RentalAgreement> getAgreementListProperty() {
        return agreementListProperty.get();
    }

    public ObjectProperty<Set<RentalAgreement>> agreementListPropertyProperty() {
        return agreementListProperty;
    }

    public Set<Payment> getPaymentsProperty() {
        return paymentsProperty.get();
    }

    public ObjectProperty<Set<Payment>> paymentsPropertyProperty() {
        return paymentsProperty;
    }
}
