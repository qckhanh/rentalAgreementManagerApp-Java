package org.rmit.model.Persons;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
public class Guest extends Person {
    private String guestUsername;
    private String guestPassword;

    @Transient
    transient private StringProperty guestUsernameProperty = new SimpleStringProperty();
    @Transient
    transient private StringProperty guestPasswordProperty = new SimpleStringProperty();

    @Override
    protected void synWithSimpleProperty() {
        this.idPropertyProperty().setValue(this.id);
        this.nameProperty.setValue(this.name);
        this.dateOfBirthProperty.setValue(this.dateOfBirth);
        this.contactProperty.setValue(this.contact);
        this.usernameProperty.setValue(this.username);
        this.passwordProperty.setValue(this.password);

        this.guestUsernameProperty.setValue(this.guestUsername);
        this.guestPasswordProperty.setValue(this.guestPassword);
    }

    public Guest() {
        super();
    }

    public String getGuestUsername() {
        return guestUsername;
    }

    public void setGuestUsername(String guestUsername) {
        guestPasswordProperty.setValue(guestUsername);
        this.guestUsername = guestPasswordProperty.get();
    }

    public String getGuestPassword() {
        return guestPassword;
    }

    public void setGuestPassword(String guestPassword) {
        guestPasswordProperty.setValue(guestPassword);
        this.guestPassword = guestPasswordProperty.get();
    }
}
