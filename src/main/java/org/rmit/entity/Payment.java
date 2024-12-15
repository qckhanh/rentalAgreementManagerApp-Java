package org.rmit.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Payment {
    @Id
    @GeneratedValue
    private int paymentId;

    @ManyToOne
    @JoinColumn(name = "agreement_id")
    private RentalAgreement rentalAgreement;

    @ManyToOne
    private Renter mainRenter;

    @ManyToOne
    private Property property;

    private double amount;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    // Constructors, getters, and setters

    public Payment() {}

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public RentalAgreement getRentalAgreement() {
        return rentalAgreement;
    }

    public void setRentalAgreement(RentalAgreement rentalAgreement) {
        this.rentalAgreement = rentalAgreement;
    }

    public Renter getMainRenter() {
        return mainRenter;
    }

    public void setMainRenter(Renter mainRenter) {
        this.mainRenter = mainRenter;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}