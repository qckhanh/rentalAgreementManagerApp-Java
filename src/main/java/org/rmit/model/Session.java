package org.rmit.model;

import org.rmit.model.Persons.Person;

public class Session {
    private static Session session;
    private Person currentUser;

    private Session(){
        Person currentUser = null;
    }

    public static Session getInstance(){
        if(session == null){
            session = new Session();
        }
        return session;
    }

    public void setCurrentUser(Person currentUser){
        this.currentUser = currentUser;
    }

    public Person getCurrentUser() {
        return currentUser;
    }
}
