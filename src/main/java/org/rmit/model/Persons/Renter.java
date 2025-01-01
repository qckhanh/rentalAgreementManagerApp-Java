package org.rmit.model.Persons;
//
//import UIHelper.UserInterfaceManager;
//import UIHelper.DateCreator;

import jakarta.persistence.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Agreement.RentalAgreement;

import java.util.*;

// class Tenant
@Entity(name = "Renter")
public class Renter extends Person {
    @OneToMany(mappedBy = "mainTenant", fetch = FetchType.LAZY)
    private Set<RentalAgreement> agreementList = new HashSet<>();

    @OneToMany(
            mappedBy = "mainRenter",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
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
        this.profileAvatarProperty.setValue(this.profileAvatar); // new for avatar

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

    public void addAgreement(RentalAgreement agreement) {
        this.agreementList.add(agreement);
        this.agreementListProperty.set(this.agreementList);
    }



    public Set<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.paymentsProperty.setValue(payments);
        this.payments = paymentsProperty.get();
    }

    public void addPayment(Payment payment) {
        Set<Payment> updatedPayments = new HashSet<>(paymentsProperty.get());
        updatedPayments.add(payment);
        setPayments(updatedPayments);
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
