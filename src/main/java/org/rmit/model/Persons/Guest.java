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

    }

    public Guest() {
        super();
    }

    public String getGuestUsername() {
        return guestUsername;
    }

    public void setGuestUsername(String guestUsername) {
        this.guestUsername = guestUsername;
    }

    public String getGuestPassword() {
        return guestPassword;
    }

    public void setGuestPassword(String guestPassword) {
        this.guestPassword = guestPassword;
    }
}
