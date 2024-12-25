package org.rmit.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import org.rmit.model.Persons.Person;



public class Session {
    private static Session session;
    private ObjectProperty<Person> currentUser;

    private Session(){
        currentUser = new SimpleObjectProperty<>(null);
    }

    public static Session getInstance(){
        if(session == null){
            session = new Session();
        }
        return session;
    }

    public Person getCurrentUser(){
        return currentUser.get();
    }

    public ObjectProperty<Person> currentUserProperty(){
        return currentUser;
    }

    public void setCurrentUser(Person currentUser){
        this.currentUser.set(currentUser);
    }


}
