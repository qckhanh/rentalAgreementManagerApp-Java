package org.rmit.model.Agreement;

import jakarta.persistence.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.Property;

import java.time.LocalDate;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentId;

    @ManyToOne
    @JoinColumn(name = "agreement_id")
    private RentalAgreement rentalAgreement;

    @ManyToOne(cascade = CascadeType.ALL)
    private Renter mainRenter;

    @ManyToOne
    private Property property;

    private double amount;

    @Temporal(TemporalType.DATE)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    //////////////////////////////////////////////////
    @Transient
    transient private ObjectProperty<RentalAgreement> rentalAgreementProperty = new SimpleObjectProperty<>();
    @Transient
    transient private ObjectProperty<Renter> mainRenterProperty = new SimpleObjectProperty<>();
    @Transient
    transient private ObjectProperty<Property> propertyProperty = new SimpleObjectProperty<>();
    @Transient
    transient private DoubleProperty amountProperty = new SimpleDoubleProperty();
    @Transient
    transient private ObjectProperty<PaymentMethod> paymentMethodProperty = new SimpleObjectProperty<>();
    @Transient
    transient private ObjectProperty<LocalDate> dateProperty = new SimpleObjectProperty<>();

    // Constructors, getters, and setters

    public Payment() {}

    @PostLoad
    protected void synWithSimpleProperty() {
        this.rentalAgreementProperty.setValue(this.rentalAgreement);
        this.mainRenterProperty.setValue(this.mainRenter);
        this.propertyProperty.setValue(this.property);
        this.amountProperty.setValue(this.amount);
        this.dateProperty.setValue(this.date);
        this.paymentMethodProperty.setValue(this.paymentMethod);
    }

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public RentalAgreement getRentalAgreement() {
        return rentalAgreement;
    }

    public void setRentalAgreement(RentalAgreement rentalAgreement) {
        this.rentalAgreementProperty.setValue(rentalAgreement);
        this.rentalAgreement = rentalAgreementProperty.get();
    }

    public Renter getMainRenter() {
        return mainRenter;
    }

    public void setMainRenter(Renter mainRenter) {
        this.mainRenterProperty.setValue(mainRenter);
        this.mainRenter = mainRenterProperty.get();
//        mainRenter.addPayment(this);
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.propertyProperty.setValue(property);
        this.property = propertyProperty.get();
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amountProperty.setValue(amount);
        this.amount = amountProperty.get();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.dateProperty.setValue(date);
        this.date = dateProperty.get();
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethodProperty.setValue(paymentMethod);
        this.paymentMethod = paymentMethodProperty.get();
    }

    //////////////


    public RentalAgreement getRentalAgreementProperty() {
        return rentalAgreementProperty.get();
    }

    public ObjectProperty<RentalAgreement> rentalAgreementPropertyProperty() {
        return rentalAgreementProperty;
    }

    public Renter getMainRenterProperty() {
        return mainRenterProperty.get();
    }

    public ObjectProperty<Renter> mainRenterPropertyProperty() {
        return mainRenterProperty;
    }

    public Property getPropertyProperty() {
        return propertyProperty.get();
    }

    public ObjectProperty<Property> propertyPropertyProperty() {
        return propertyProperty;
    }

    public double getAmountProperty() {
        return amountProperty.get();
    }

    public DoubleProperty amountPropertyProperty() {
        return amountProperty;
    }

    public PaymentMethod getPaymentMethodProperty() {
        return paymentMethodProperty.get();
    }

    public ObjectProperty<PaymentMethod> paymentMethodPropertyProperty() {
        return paymentMethodProperty;
    }

    public LocalDate getDateProperty() {
        return dateProperty.get();
    }

    public ObjectProperty<LocalDate> datePropertyProperty() {
        return dateProperty;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", date=" + date +
                '}';
    }
}