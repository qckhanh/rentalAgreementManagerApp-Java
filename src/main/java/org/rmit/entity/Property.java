package org.rmit.entity;

//import Database.Database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Property {
    private Owner owner;
    private String address;
    private double price;
    private PropertyStatus status;
    private int id;
    private List<Host> hosts = new ArrayList<>();
    private List<RentalAgreement> agreementList = new ArrayList<>();

    public Property() {
//        this.id = Database.IDGenerator(Payment.class);

    }

    public Property(String address, double price, PropertyStatus status) {
        this.address = address;
        this.price = price;
        this.status = status;
//        this.id = Database.IDGenerator(Payment.class);
    }

    public void addRentAgreement(RentalAgreement agreement) {
        agreement.setProperty(this);
    }

    public void remove() {
        if(owner != null) owner.getPropertiesOwned().remove(this);
        for (Host host : hosts) {
            host.getPropertiesManaged().remove(this);
        }
        for (RentalAgreement agreement : agreementList) {
            agreement.setProperty(null);
        }
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Host> getHosts() {
        return hosts;
    }

    public void setHosts(List<Host> hosts) {
        this.hosts = hosts;
    }

    public List<RentalAgreement> getAgreementList() {
        return agreementList;
    }

    public void setAgreementList(List<RentalAgreement> agreementList) {
        this.agreementList = agreementList;
    }

    public void addHost(Host host) {
        hosts.add(host);
    }

//    public String toString() {
//        return "Property [" +
//                " ID = " + id +
//                ", Owner = " + (owner == null ? "null" : owner.getName()) +
//                ", Address = '" + address + '\'' +
//                ", Price = " + price +
//                ", Status = " + status.name();
//    }

}

