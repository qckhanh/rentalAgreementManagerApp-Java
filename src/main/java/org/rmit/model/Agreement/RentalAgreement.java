package org.rmit.model.Agreement;


import jakarta.persistence.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.Property;
import org.rmit.model.Property.RentalPeriod;

import java.time.LocalDate;
import java.util.*;

@Entity
public class RentalAgreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long agreementId;

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
    private LocalDate contractDate;
    private double rentingFee;
    private AgreementStatus status;

    @OneToMany(mappedBy = "rentalAgreement")
    private final Set<Payment> payments = new HashSet<>();

    //////////////////////////
    @Transient
    transient private ObjectProperty<Renter> mainTenantProperty = new SimpleObjectProperty<>();
    @Transient
    transient private ObjectProperty<Property> propertyProperty = new SimpleObjectProperty<>();
    @Transient
    transient private ObjectProperty<Host> hostProperty = new SimpleObjectProperty<>();
    @Transient
    transient private ObjectProperty<Set<Renter>> subTenantsProperty = new SimpleObjectProperty<>();
    @Transient
    transient private ObjectProperty<RentalPeriod> periodProperty = new SimpleObjectProperty<>();
    @Transient
    transient private ObjectProperty<LocalDate> contractDateProperty = new SimpleObjectProperty<>();
    @Transient
    transient private DoubleProperty rentingFeeProperty = new SimpleDoubleProperty();
    @Transient
    transient private ObjectProperty<AgreementStatus> statusProperty = new SimpleObjectProperty<>();
    @Transient
    transient private ObjectProperty<Set<Payment>> paymentsProperty = new SimpleObjectProperty<>();



    public RentalAgreement() {}

    @PostLoad
    protected void synWithSimpleProperty() {
        this.mainTenantProperty.setValue(this.mainTenant);
        this.propertyProperty.setValue(this.property);
        this.hostProperty.setValue(this.host);
        this.subTenantsProperty.setValue(this.subTenants);
        this.periodProperty.setValue(this.period);
        this.contractDateProperty.setValue(this.contractDate);
        this.rentingFeeProperty.setValue(this.rentingFee);
        this.statusProperty.setValue(this.status);
        this.paymentsProperty.setValue(this.payments);
    }
    public Renter getMainTenant() {
        return mainTenant;
    }

    public void setMainTenant(Renter mainTenant) {
        this.mainTenantProperty.setValue(mainTenant);
        this.mainTenant = mainTenantProperty.get();
    }

    public long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(long agreementId) {
        this.agreementId = agreementId;
    }

    public Set<Renter> getSubTenants() {
        return subTenants;
    }

    public void setSubTenants(Set<Renter> subTenants) {
        this.subTenantsProperty.setValue(subTenants);
        this.subTenants = subTenantsProperty.get();
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.propertyProperty.setValue(property);
        this.property = propertyProperty.get();
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.hostProperty.setValue(host);
        this.host = hostProperty.get();
    }

    public RentalPeriod getPeriod() {
        return period;
    }

    public void setPeriod(RentalPeriod period) {
        this.periodProperty.setValue(period);
        this.period = periodProperty.get();
    }

    public LocalDate getContractDate() {
        return contractDate;
    }

    public void setContractDate(LocalDate contractDate) {
        this.contractDateProperty.setValue(contractDate);
        this.contractDate = contractDateProperty.get();
    }

    public double getRentingFee() {
        return rentingFee;
    }

    public void setRentingFee(double rentingFee) {
        this.rentingFeeProperty.setValue(rentingFee);
        this.rentingFee = rentingFeeProperty.get();
    }

    public AgreementStatus getStatus() {
        return status;
    }

    public void setStatus(AgreementStatus status) {
        this.statusProperty.setValue(status);
        this.status = statusProperty.get();
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    //////////////

    public Renter getMainTenantProperty() {
        return mainTenantProperty.get();
    }

    public ObjectProperty<Renter> mainTenantPropertyProperty() {
        return mainTenantProperty;
    }

    public Property getPropertyProperty() {
        return propertyProperty.get();
    }

    public ObjectProperty<Property> propertyPropertyProperty() {
        return propertyProperty;
    }

    public Host getHostProperty() {
        return hostProperty.get();
    }

    public ObjectProperty<Host> hostPropertyProperty() {
        return hostProperty;
    }

    public Set<Renter> getSubTenantsProperty() {
        return subTenantsProperty.get();
    }

    public ObjectProperty<Set<Renter>> subTenantsPropertyProperty() {
        return subTenantsProperty;
    }

    public RentalPeriod getPeriodProperty() {
        return periodProperty.get();
    }

    public ObjectProperty<RentalPeriod> periodPropertyProperty() {
        return periodProperty;
    }

    public LocalDate getContractDateProperty() {
        return contractDateProperty.get();
    }

    public ObjectProperty<LocalDate> contractDatePropertyProperty() {
        return contractDateProperty;
    }

    public double getRentingFeeProperty() {
        return rentingFeeProperty.get();
    }

    public DoubleProperty rentingFeePropertyProperty() {
        return rentingFeeProperty;
    }

    public AgreementStatus getStatusProperty() {
        return statusProperty.get();
    }

    public ObjectProperty<AgreementStatus> statusPropertyProperty() {
        return statusProperty;
    }

    public Set<Payment> getPaymentsProperty() {
        return paymentsProperty.get();
    }

    public ObjectProperty<Set<Payment>> paymentsPropertyProperty() {
        return paymentsProperty;
    }
}