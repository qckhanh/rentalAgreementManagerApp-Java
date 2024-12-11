package org.rmit.entity;
//
//import UIHelper.UserInterfaceManager;
//import UIHelper.DateCreator;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.*;

// class Tenant
@Entity(name = "Renter")
public class Renter extends Person {
    @OneToMany(mappedBy = "mainTenant")
    private Set<RentalAgreement> agreementList = new HashSet<>();

    @OneToMany(mappedBy = "mainRenter")
    private Set<Payment> payments = new HashSet<>();

    public Renter(){
        super();
    }
    public Renter(String name, Date dateOfBirth, String contact) {
        super(name, dateOfBirth, contact);
    }
//////////////////////////////////////////////////////////////////////
    public Set<RentalAgreement> getAgreementList() {
        return agreementList;
    }

    public void setAgreementList(Set<RentalAgreement> agreementList) {
        this.agreementList = agreementList;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }
}
