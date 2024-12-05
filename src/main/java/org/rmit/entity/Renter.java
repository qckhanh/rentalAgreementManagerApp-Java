package org.rmit.entity;
//
//import UIHelper.UserInterfaceManager;
//import UIHelper.DateCreator;

import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// class Tenant
@Entity
public class Renter extends Person{
    private List<RentalAgreement> agreementList = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();

    public Renter(){
        super();
    }
    public Renter(String name, Date dateOfBirth, String contact) {
        super(name, dateOfBirth, contact);
    }

    public List<RentalAgreement> getAgreementList() {
        return agreementList;
    }

    public void setAgreementList(List<RentalAgreement> agreementList) {
        this.agreementList = agreementList;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }


    public void addAgreement(RentalAgreement agreement){
        agreementList.add(agreement);
    }

    public void addPayment(Payment payment){
        payments.add(payment);
    }


    @Override
    public String toString() {
        return  super.toString() +
                ", Payments=" + (payments.isEmpty() ? "NA" : payments)+
                ", agreementList=" +  ((agreementList.isEmpty()) ? "NA" : agreementList) +
                '\n';
    }
}
