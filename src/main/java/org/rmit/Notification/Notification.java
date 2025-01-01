package org.rmit.Notification;

import jakarta.persistence.*;
import javafx.beans.property.*;
import jdk.jfr.ContentType;
import org.rmit.Helper.DateUtils;
import org.rmit.model.Persons.Person;

import java.util.HashSet;
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

    @Transient
    int totalReceivers = 0;

    @ManyToMany
    @JoinTable(
            name = "notification_receivers",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = @JoinColumn(name = "receiver_id")
    )
    private Set<Person> receivers = new HashSet<>();
    private String header;
    private String content;
    private String timestamp;


    /////////////////////////////////////////////////////
    @Transient
    private ObjectProperty<Person> senderP = new SimpleObjectProperty<>();
    @Transient
    private ObjectProperty<Set<Person>> receiversP = new SimpleObjectProperty<>();
    @Transient
    private StringProperty headerP = new SimpleStringProperty();
    @Transient
    private StringProperty contentP = new SimpleStringProperty();
    @Transient
    private StringProperty timestampP = new SimpleStringProperty();
    @Transient
    private IntegerProperty totalReceiversP = new SimpleIntegerProperty();

    @PostLoad
    public void synWithSimpleProperty(){
        this.senderP.setValue(this.sender);
        this.receiversP.setValue(this.receivers);
        this.headerP.setValue(this.header);
        this.contentP.setValue(this.content);
        this.timestampP.setValue(this.timestamp);
        this.totalReceiversP.setValue(this.totalReceivers);

    }
    public Notification(Person sender) {
        this.sender = sender;
        senderP.set(this.sender);
    }

    public Notification() {
    }

    public int getTotalReceivers() {
        return totalReceivers;
    }

    public void setTotalReceivers(int totalReceivers) {
        this.totalReceivers = totalReceivers;
        totalReceiversP.set(this.totalReceivers);
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
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
        senderP.set(this.sender);
    }

    public Set<Person> getReceivers() {
        return receivers;
    }

    public void setReceivers(Set<Person> receivers) {
        this.receivers = receivers;
        receiversP.set(this.receivers);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String message) {
        this.content = message;
        contentP.set(this.content);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        timestampP.set(this.timestamp);
    }

    public void addReceiver(Person person) {
        receivers.add(person);
        receiversP.set(receivers);
        totalReceivers++;
    }



    public IntegerProperty totalReceiversPProperty() {
        return totalReceiversP;
    }


    public StringProperty timestampPProperty() {
        return timestampP;
    }


    public StringProperty contentPProperty() {
        return contentP;
    }

    public StringProperty headerPProperty() {
        return headerP;
    }

    public ObjectProperty<Set<Person>> receiversPProperty() {
        return receiversP;
    }

    public ObjectProperty<Person> senderPProperty() {
        return senderP;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "sender=" + sender.getName() +
                ", receivers=" + receivers.stream().map(Person::getName).toList() +
                ", message='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

}