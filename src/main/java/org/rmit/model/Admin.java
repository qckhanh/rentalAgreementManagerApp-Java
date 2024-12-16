package org.rmit.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
public class Admin extends Person {
    private String adminUsername;
    private String adminPassword;

    @Transient
    transient private StringProperty adminUsernameProperty = new SimpleStringProperty();
    @Transient
    transient private StringProperty adminPasswordProperty = new SimpleStringProperty();

    @Override
    protected void synWithSimpleProperty() {
        this.idPropertyProperty().setValue(this.id);
        this.nameProperty.setValue(this.name);
        this.dateOfBirthProperty.setValue(this.dateOfBirth);
        this.contactProperty.setValue(this.contact);
        this.usernameProperty.setValue(this.username);
        this.passwordProperty.setValue(this.password);

        this.adminUsernameProperty.setValue(this.adminUsername);
        this.adminPasswordProperty.setValue(this.adminPassword);
    }

    public Admin() {
        super();
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsernameProperty.setValue(adminUsername);
        this.adminUsername = adminUsernameProperty.get();
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPasswordProperty.setValue(adminPassword);
        this.adminPassword = adminPasswordProperty.get();
    }

    //////////////


    public String getAdminPasswordProperty() {
        return adminPasswordProperty.get();
    }

    public StringProperty adminPasswordPropertyProperty() {
        return adminPasswordProperty;
    }

    public String getAdminUsernameProperty() {
        return adminUsernameProperty.get();
    }

    public StringProperty adminUsernamePropertyProperty() {
        return adminUsernameProperty;
    }
}
