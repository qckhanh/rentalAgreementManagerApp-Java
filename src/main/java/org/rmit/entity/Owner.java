package org.rmit.entity;

//import UIHelper.UserInterfaceManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Owner extends Person{
    private List<Property> propertiesOwned = new ArrayList<>();
    private List<Host> hosts = new ArrayList<>();


    public Owner(String name, Date dateOfBirth, String contact) {
        super(name, dateOfBirth, contact);

    }

    public void addProperty(Property property){
        propertiesOwned.add(property);
        property.setOwner(this);
    }

    public List<Property> getPropertiesOwned() {
        return propertiesOwned;
    }

    public void setPropertiesOwned(List<Property> propertiesOwned) {
        this.propertiesOwned = propertiesOwned;
    }

    public List<Host> getHosts() {
        return hosts;
    }

    public void setHosts(List<Host> hosts) {
        this.hosts = hosts;
    }


}
