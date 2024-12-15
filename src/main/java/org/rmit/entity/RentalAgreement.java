package org.rmit.entity;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.*;

@Entity
public class RentalAgreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int agreementId;

    @ManyToOne
    @JoinColumn(name = "main_renter_id", nullable = false)
    private Renter mainTenant;

    @ManyToMany
    @JoinTable(
            name = "rental_agreement_subtenants",
            joinColumns = @JoinColumn(name = "agreement_id"),
            inverseJoinColumns = @JoinColumn(name = "subtenant_id")
    )
    private Set<Renter> subTenants = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne
    @JoinColumn(name = "host_id", nullable = false)
    private Host host;

    private RentalPeriod period;
    private Date contractDate;
    private double rentingFee;
    private AgreementStatus status;

    @OneToMany(mappedBy = "rentalAgreement")
    private final Set<Payment> payments = new HashSet<>();

    public RentalAgreement() {}
    public RentalAgreement(RentalPeriod period, Date contractDate, double rentingFee, AgreementStatus status) {
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

    public Set<Renter> getSubTenants() {
        return subTenants;
    }

    public void setSubTenants(Set<Renter> subTenants) {
        this.subTenants = subTenants;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
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

    public Set<Payment> getPayments() {
        return payments;
    }
}