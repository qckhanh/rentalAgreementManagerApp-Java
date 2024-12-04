package Model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import Database.*;
import UIHelper.UserInterfaceManager;

public class Payment implements Serializable, Preview{
    private int paymentId;
    private RentalAgreement agreement;
    private Renter mainRenter;
    private Property property;
    private double amount;
    private Date date;
    private PaymentMethod paymentMethod;

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Payment() {
        this.paymentId = Database.IDGenerator(Payment.class);
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

    public RentalAgreement getAgreement() {
        return agreement;
    }

    public void setAgreement(RentalAgreement agreement) {
        this.agreement = agreement;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
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

    @Override
    public void preview() {
        UserInterfaceManager.printMenu("Payment ID: " + paymentId,
                List.of("Renter: " + mainRenter.getName(),
                        "Property ID: " + property.getId(),
                        "Property Address: " + property.getAddress(),
                        "Amount Paid: " + amount,
                        "Date: " + date,
                        "Method: " + paymentMethod.toString()
                )
        );
    }
}