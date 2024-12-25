package org.rmit.model.Persons;

//import UIHelper.UserInterfaceManager;

import jakarta.persistence.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.rmit.model.Property.Property;

import java.util.*;

@Entity
public class Owner extends Person {
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Property> propertiesOwned = new HashSet<>();

    @ManyToMany(mappedBy = "cooperatingOwners"/*, cascade = CascadeType.ALL*/)
    private Set<Host> hosts = new HashSet<>();
///////////////////////////////
    @Transient
    transient private ObjectProperty<Set<Host>> hostsProperty = new SimpleObjectProperty<>();
    @Transient
    transient private ObjectProperty<Set<Property>> propertiesOwnedProperty = new SimpleObjectProperty<>();

    @Override
    protected void synWithSimpleProperty() {
        this.idPropertyProperty().setValue(this.id);
        this.nameProperty.setValue(this.name);
        this.dateOfBirthProperty.setValue(this.dateOfBirth);
        this.contactProperty.setValue(this.contact);
        this.usernameProperty.setValue(this.username);
        this.passwordProperty.setValue(this.password);

        this.hostsProperty.setValue(this.hosts);
        this.propertiesOwnedProperty.setValue(this.propertiesOwned);
    }

    public Owner() {
        super();
    }

    public Set<Host> getHosts() {
        return hosts;
    }

    public void setHosts(Set<Host> hosts) {
        this.hostsProperty.setValue(hosts);
        this.hosts = hostsProperty.get();
    }

    public Set<Property> getPropertiesOwned() {
        return propertiesOwned;
    }

    public void setPropertiesOwned(Set<Property> propertiesOwned) {
        this.propertiesOwnedProperty.setValue(propertiesOwned);
        this.propertiesOwned = propertiesOwnedProperty.get();
    }


    //////////////

    public Set<Host> getHostsProperty() {
        return hostsProperty.get();
    }

    public ObjectProperty<Set<Host>> hostsPropertyProperty() {
        return hostsProperty;
    }

    public Set<Property> getPropertiesOwnedProperty() {
        return propertiesOwnedProperty.get();
    }

    public ObjectProperty<Set<Property>> propertiesOwnedPropertyProperty() {
        return propertiesOwnedProperty;
    }
}
