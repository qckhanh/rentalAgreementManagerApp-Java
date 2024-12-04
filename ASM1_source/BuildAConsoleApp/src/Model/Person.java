package Model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import Database.*;
import UIHelper.UserInterfaceManager;
import UIHelper.DateCreator;

public abstract class Person implements Serializable, Preview {
    private int id;
    protected String name;
    protected Date dateOfBirth;
    protected String contact;




    public Person(String name, Date dateOfBirth, String contact) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.contact = contact;
        id = Database.IDGenerator(Person.class);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person person)) return false;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dateOfBirth, contact);
    }

    @Override
    public String toString() {
        return "ID: " + id + ", " +
                "Full name: " + name + ", " +
                "Date of Birth: " + DateCreator.formatDate(dateOfBirth) + ", " +
                "Contact: " + contact;
    }

    @Override
    public void preview() {
        UserInterfaceManager.printMenu("PERSON INFORMATION", Arrays.asList(
                "ID: " + id,
                "FULL NAME: " + name,
                "DATE OF BIRTH: " + DateCreator.formatDate(dateOfBirth),
                "CONTACT: " + contact
        ));
    }

}
