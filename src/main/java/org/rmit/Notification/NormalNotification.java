package org.rmit.Notification;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.rmit.model.Persons.Person;

@Entity
@DiscriminatorValue("NORMAL")
public class NormalNotification extends Notification {

    public NormalNotification() {
        super();
    }

    public NormalNotification(Person sender) {
        super(sender);
    }

    @Override
    public void performAction() {
        System.out.println("Normal notificationGraph delivered to receivers.");
    }
}