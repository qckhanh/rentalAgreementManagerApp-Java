package org.rmit.model.Persons;

import jakarta.persistence.*;
import javafx.beans.property.*;
import org.rmit.Notification.Notification;
import org.rmit.Notification.Request;

import java.time.LocalDate;
import java.util.*;
//import Database.*;
//import UIHelper.UserInterfaceManager;
//import UIHelper.DateCreator;

//@MappedSuperclass
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Person {


    @Id
    @GeneratedValue
    protected long id;
    protected String name;
    protected LocalDate dateOfBirth;
    @Column(unique = true)
    protected String contact;

    @Lob
    protected byte[] profileAvatar;

    @Column(unique = true)
    protected String username;
    protected String password;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    protected Set<Notification> sentNotifications = new HashSet<>();

    @ManyToMany(mappedBy = "receivers", cascade = CascadeType.ALL)
    protected Set<Notification> receivedNotifications = new HashSet<>();

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
    @Transient
    protected ObjectProperty<byte[]> profileAvatarProperty = new SimpleObjectProperty<>();
    @Transient
    protected ObjectProperty<Set<Notification>> sentNotificationsProperty = new SimpleObjectProperty<>();
    @Transient
    protected ObjectProperty<Set<Notification>> receivedNotificationsProperty = new SimpleObjectProperty<>();

    @PostLoad
    protected abstract void synWithSimpleProperty();

    public Person() {

    }

    public void sentNotification(Notification notification) {
        sentNotifications.add(notification);
        for(Person receiver : notification.getReceivers()) {
            receiver.addReceivedNotification(notification);
        }
        receivedNotificationsProperty.set(receivedNotifications);
    }

    private void addReceivedNotification(Notification notification) {
        receivedNotifications.add(notification);
        receivedNotificationsProperty.set(receivedNotifications);
    }

    public boolean acceptRequest(Request request) {
        return request.approve(this);
    }

    public boolean denyRequest(Request request) {
        return request.deny(this);
    }

    public Set<Notification> getReceivedNotifications() {
        return receivedNotifications;
    }

    public void setReceivedNotifications(Set<Notification> receivedNotifications) {
        this.receivedNotifications = receivedNotifications;
        receivedNotificationsProperty.set(receivedNotifications);
    }

    public Set<Notification> getSentNotifications() {
        return sentNotifications;
    }

    public void setSentNotifications(Set<Notification> sentNotifications) {
        this.sentNotifications = sentNotifications;
        sentNotificationsProperty.set(sentNotifications);
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
        this.nameProperty.setValue(name);
        this.name = nameProperty.get();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.idProperty.setValue(id);
        this.id = idProperty.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person person)) return false;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dateOfBirth, contact, username);
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

    public byte[] getProfileAvatar() {
        return profileAvatar;
    }

    public void setProfileAvatar(byte[] profileAvatar) {
        this.profileAvatarProperty.set(profileAvatar);
        this.profileAvatar = profileAvatarProperty.get();
    }


    public ObjectProperty<byte[]> profileAvatarPropertyProperty() {
        return profileAvatarProperty;
    }


    @Override
    public String toString() {
        return "ID - " + id;
    }

    public Set<Notification> getReceivedNotificationsProperty() {
        return receivedNotificationsProperty.get();
    }

    public ObjectProperty<Set<Notification>> receivedNotificationsPropertyProperty() {
        return receivedNotificationsProperty;
    }

    public void setReceivedNotificationsProperty(Set<Notification> receivedNotificationsProperty) {
        this.receivedNotificationsProperty.set(receivedNotificationsProperty);
    }

    public Set<Notification> getSentNotificationsProperty() {
        return sentNotificationsProperty.get();
    }

    public ObjectProperty<Set<Notification>> sentNotificationsPropertyProperty() {
        return sentNotificationsProperty;
    }

    public void setSentNotificationsProperty(Set<Notification> sentNotificationsProperty) {
        this.sentNotificationsProperty.set(sentNotificationsProperty);
    }

    public void addSentNotification(Notification notification){
        this.sentNotifications.add(notification);
        this.sentNotificationsProperty.setValue(this.sentNotifications);
    }

//    public void addReceivedNotification(Notification notification){
//        this.receivedNotifications.add(notification);
//        this.receivedNotificationsProperty.setValue(this.receivedNotifications);
//    }
}
