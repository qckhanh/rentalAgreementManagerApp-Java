package org.rmit.entity;

//import UIHelper.UserInterfaceManager;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

import java.util.*;

@Entity
public class Owner extends Person {
    @OneToMany(mappedBy = "owner")
    private Set<Property> propertiesOwned = new HashSet<>();

    @ManyToMany(mappedBy = "cooperatingOwners")
    private Set<Host> hosts = new HashSet<>();

    public Owner() {
        super();
    }
    public Owner(String name, Date dateOfBirth, String contact) {
        super(name, dateOfBirth, contact);

    }

    public Set<Property> getPropertiesOwned() {
        return propertiesOwned;
    }

    public void setPropertiesOwned(Set<Property> propertiesOwned) {
        this.propertiesOwned = propertiesOwned;
    }

    public Set<Host> getHosts() {
        return hosts;
    }

    public void setHosts(Set<Host> hosts) {
        this.hosts = hosts;
    }
}
