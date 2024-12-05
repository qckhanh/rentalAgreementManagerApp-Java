package org.rmit.entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RentalAgreement {
    private int agreementId;
    private Renter mainTenant = null;
    private List<Renter> subTenants = new ArrayList<>();
    private Property property = null;
    private Host host = null;       // in the Property
    private RentalPeriod period = null;
    private Date contractDate = null;
    private double rentingFee;
    private AgreementStatus status = null;
    private final List<Payment> payments = new ArrayList<>();

    public List<Payment> getPayments() {
        return payments;
    }

    public RentalAgreement() {
//        this.agreementId = Database.IDGenerator(Payment.class);
    }

    public RentalAgreement(RentalPeriod period, Date contractDate, double rentingFee, AgreementStatus status) {
//        this.agreementId = Database.IDGenerator(Payment.class);
        this.period = period;
        this.contractDate = contractDate;
        this.rentingFee = rentingFee;
        this.status = status;
    }

    public int getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(int agreementId) {
        this.agreementId = agreementId;
    }

    public Renter getMainTenant() {
        return mainTenant;
    }

    public void setMainTenant(Renter mainTenant) {
        this.mainTenant = mainTenant;
    }

    public List<Renter> getSubTenants() {
        return subTenants;
    }

    public void setSubTenants(List<Renter> subTenants) {
        this.subTenants = subTenants;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        if(property != null) property.getAgreementList().add(this);
        this.property = property;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public RentalPeriod getPeriod() {
        return period;
    }

    public void setPeriod(RentalPeriod period) {
        this.period = period;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public double getRentingFee() {
        return rentingFee;
    }

    public void setRentingFee(double rentingFee) {
        this.rentingFee = rentingFee;
    }

    public AgreementStatus getStatus() {
        return status;
    }

    public void setStatus(AgreementStatus status) {
        this.status = status;
    }


}