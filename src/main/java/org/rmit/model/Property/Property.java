package org.rmit.model.Property;

import jakarta.persistence.*;
import javafx.beans.property.*;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Agreement.RentalAgreement;

import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Property {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = true)
    private Owner owner;

    private String address;
    private double price;

    @Enumerated(EnumType.STRING)
    private PropertyStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", insertable = false, updatable = false)
    private PropertyType type;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(mappedBy = "propertiesManaged")
    private Set<Host> hosts = new HashSet<>();

    @OneToMany(mappedBy = "property")
    private Set<RentalAgreement> agreementList = new HashSet<>();

    // JavaFX Properties (for binding)
    @Transient
    protected ObjectProperty<Owner> ownerProperty = new SimpleObjectProperty<>();
    @Transient
    protected StringProperty addressProperty = new SimpleStringProperty();
    @Transient
    protected DoubleProperty priceProperty = new SimpleDoubleProperty();
    @Transient
    protected ObjectProperty<PropertyStatus> statusProperty = new SimpleObjectProperty<>();
    @Transient
    protected ObjectProperty<PropertyType> typeProperty = new SimpleObjectProperty<>();
    @Transient
    protected LongProperty idProperty = new SimpleLongProperty();
    @Transient
    protected ObjectProperty<Set<Host>> hostsProperty = new SimpleObjectProperty<>();
    @Transient
    protected ObjectProperty<Set<RentalAgreement>> agreementListProperty = new SimpleObjectProperty<>();

    // Constructor
    public Property() {
        this.type = PropertyType.NONE;
    }

    @PostLoad
    protected abstract void synWithSimpleProperty();

    public Property(Owner owner, String address, double price, PropertyStatus status, PropertyType type) {
        this.owner = owner;
        this.address = address;
        this.price = price;
        this.status = status;
        this.type = type;
    }


    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.ownerProperty.setValue(owner);
        this.owner = ownerProperty.get();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.addressProperty.setValue(address);
        this.address = addressProperty.get();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.priceProperty.setValue(price);
        this.price = priceProperty.get();
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public PropertyType getType() {
        return type;
    }

    public void setStatus(PropertyStatus status) {
        this.statusProperty.setValue(status);
        this.status = statusProperty.get();
    }

    public void setType(PropertyType type) {
        this.typeProperty.setValue(type);
        this.type = typeProperty.get();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.idProperty.setValue(id);
        this.id = idProperty.get();
    }

    public Set<Host> getHosts() {
        return hosts;
    }

    public void setHosts(Set<Host> hosts) {
        this.hostsProperty.setValue(hosts);
        this.hosts = hostsProperty.get();
    }

    public Set<RentalAgreement> getAgreementList() {
        return agreementList;
    }

    public void setAgreementList(Set<RentalAgreement> agreementList) {
        this.agreementListProperty.setValue(agreementList);
        this.agreementList = agreementListProperty.get();
    }

    //Property


    public Owner getOwnerProperty() {
        return ownerProperty.get();
    }

    public ObjectProperty<Owner> ownerPropertyProperty() {
        return ownerProperty;
    }

    public String getAddressProperty() {
        return addressProperty.get();
    }

    public StringProperty addressPropertyProperty() {
        return addressProperty;
    }

    public double getPriceProperty() {
        return priceProperty.get();
    }

    public DoubleProperty pricePropertyProperty() {
        return priceProperty;
    }

    public PropertyStatus getStatusProperty() {
        return statusProperty.get();
    }

    public ObjectProperty<PropertyStatus> statusPropertyProperty() {
        return statusProperty;
    }

    public ObjectProperty<PropertyType> typeOfPropertyProperty() {
        return typeProperty;
    }

    public long getIdProperty() {
        return idProperty.get();
    }

    public LongProperty idPropertyProperty() {
        return idProperty;
    }

    public Set<Host> getHostsProperty() {
        return hostsProperty.get();
    }

    public ObjectProperty<Set<Host>> hostsPropertyProperty() {
        return hostsProperty;
    }

    public Set<RentalAgreement> getAgreementListProperty() {
        return agreementListProperty.get();
    }

    public ObjectProperty<Set<RentalAgreement>> agreementListPropertyProperty() {
        return agreementListProperty;
    }

    @Override
    public String toString() {
        return "Property{" +
                "owner=" + ownerProperty.get() +
                ", address='" + addressProperty.get() + '\'' +
                ", price=" + priceProperty.get() +
                ", status=" + statusProperty.get() +
                ", id=" + idProperty.get() +
                '}';
    }
}
