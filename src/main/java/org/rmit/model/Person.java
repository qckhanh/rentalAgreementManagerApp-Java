package org.rmit.model;

import jakarta.persistence.*;
import javafx.beans.property.*;

import java.time.LocalDate;
import java.util.Date;
//import Database.*;
//import UIHelper.UserInterfaceManager;
//import UIHelper.DateCreator;

@MappedSuperclass
public abstract class Person {


    @Id
    @GeneratedValue
    protected long id;
    protected String name;
    protected LocalDate dateOfBirth;
    protected String contact;

    @Column(unique = true)
    protected String username;
    protected String password;

///////////////////////////////////////////////////////////////////////////////
    @Transient
     private LongProperty idProperty = new SimpleLongProperty();
    @Transient
     protected StringProperty nameProperty = new SimpleStringProperty();
    @Transient
     protected ObjectProperty<LocalDate> dateOfBirthProperty = new SimpleObjectProperty<>();
    @Transient
     protected StringProperty contactProperty = new SimpleStringProperty();
    @Transient
     protected StringProperty usernameProperty = new SimpleStringProperty();
    @Transient
     protected StringProperty passwordProperty = new SimpleStringProperty();

    @PostLoad
    protected abstract void synWithSimpleProperty();

    public Person() {
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.passwordProperty.setValue(password);
        this.password = passwordProperty.get();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.usernameProperty.setValue(username);
        this.username = usernameProperty.get();
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contactProperty.setValue(contact);
        this.contact = contactProperty.get();
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirthProperty.setValue(dateOfBirth);
        this.dateOfBirth = dateOfBirthProperty.get();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.nameProperty = new SimpleStringProperty(this, "name", name);
        this.name = nameProperty.get();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.idProperty.setValue(id);
        this.id = idProperty.get();
    }

    //////////


    public long getIdProperty() {
        return idProperty.get();
    }

    public LongProperty idPropertyProperty() {
        return idProperty;
    }

    public String getNameProperty() {
        return nameProperty.get();
    }

    public StringProperty namePropertyProperty() {
        return nameProperty;
    }

    public LocalDate getDateOfBirthProperty() {
        return dateOfBirthProperty.get();
    }

    public ObjectProperty<LocalDate> dateOfBirthPropertyProperty() {
        return dateOfBirthProperty;
    }

    public String getContactProperty() {
        return contactProperty.get();
    }

    public StringProperty contactPropertyProperty() {
        return contactProperty;
    }

    public String getUsernameProperty() {
        return usernameProperty.get();
    }

    public StringProperty usernamePropertyProperty() {
        return usernameProperty;
    }

    public String getPasswordProperty() {
        return passwordProperty.get();
    }

    public StringProperty passwordPropertyProperty() {
        return passwordProperty;
    }

    @Override
    public String toString() {
        return "Person{" +
                "idProperty=" + idProperty.get() +
                ", nameProperty=" + nameProperty.get() +
                ", dateOfBirthProperty=" + dateOfBirthProperty.get() +
                ", contactProperty=" + contactProperty.get() +
                ", usernameProperty=" + usernameProperty.get() +
                ", passwordProperty=" + passwordProperty.get() +
                '}';
    }

}
