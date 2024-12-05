package org.rmit.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
//import Database.*;
//import UIHelper.UserInterfaceManager;
//import UIHelper.DateCreator;

@MappedSuperclass
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    protected String name;
    protected Date dateOfBirth;
    protected String contact;


    public Person() {
    }

    public Person(String name, Date dateOfBirth, String contact) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.contact = contact;
//        id = Database.IDGenerator(Person.class);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name, dateOfBirth, contact);
    }

}
