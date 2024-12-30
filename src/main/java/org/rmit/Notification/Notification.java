package org.rmit.Notification;

import jakarta.persistence.*;
import org.rmit.model.Persons.Person;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "notification_type")
public abstract class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Person sender;

    @ManyToMany
    @JoinTable(
            name = "notification_receivers",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = @JoinColumn(name = "receiver_id")
    )
    private Set<Person> receivers = new HashSet<>();

    private String message;
    private String timestamp;

    public Notification(Person sender) {
        this.sender = sender;
    }

    public Notification() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getSender() {
        return sender;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }

    public Set<Person> getReceivers() {
        return receivers;
    }

    public void setReceivers(Set<Person> receivers) {
        this.receivers = receivers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void addReceiver(Person person) {
        receivers.add(person);
    }

    // Getters and setters...

    public abstract void performAction();

    @Override
    public String toString() {
        return "Notification{" +
                "sender=" + sender.getName() +
                ", receivers=" + receivers.stream().map(Person::getName).toList() +
                ", message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}