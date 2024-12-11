package org.rmit.entity;

//import Database.Database;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public abstract class Property {
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    private String address;
    private double price;

    @Enumerated(EnumType.STRING)
    private PropertyStatus status;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany(mappedBy = "propertiesManaged")
    private Set<Host> hosts = new HashSet<>();

    @OneToMany(mappedBy = "property")
    private Set<RentalAgreement> agreementList = new HashSet<>();

    // Add constructors, getters, and setters as needed


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

    public Set<Host> getHosts() {
        return hosts;
    }

    public void setHosts(Set<Host> hosts) {
        this.hosts = hosts;
    }

    public Set<RentalAgreement> getAgreementList() {
        return agreementList;
    }

    public void setAgreementList(Set<RentalAgreement> agreementList) {
        this.agreementList = agreementList;
    }
}

