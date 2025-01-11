package org.rmit.Notification;

import jakarta.persistence.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.rmit.model.Persons.Person;

@Entity
@DiscriminatorValue("REQUEST")
public class Request extends Notification {
    private String draftObject;
    boolean isAllApproved = false;

    ///////////////////////
    @Transient
    private StringProperty draftObjectP = new SimpleStringProperty();
    @Transient
    private BooleanProperty isAllApprovedP = new SimpleBooleanProperty();


    public Request() {
        super();
    }
    public Request(Person sender) {
        super(sender);
    }

    @Override
    public void synWithSimpleProperty(){
        super.synWithSimpleProperty();
        this.draftObjectP.setValue(this.draftObject);
        this.isAllApprovedP.setValue(this.isAllApproved);
    }

    public StringProperty draftObjectPProperty() {
        return draftObjectP;
    }

    public BooleanProperty isAllApprovedPProperty() {
        return isAllApprovedP;
    }

    //Helper
    public boolean approve(Person receiver) {
        isAllApproved = true;
        isAllApprovedP.set(this.isAllApproved);
        return true;
    }
    public boolean deny(Person receiver) {
        isAllApproved = false;
        isAllApprovedP.set(this.isAllApproved);
        return true;
    }

    public boolean isAllApproved() {
        return isAllApproved;
    }

    public String getDraftObject() {
        return draftObject;
    }

    public void setDraftObject(String draftObject) {
        this.draftObject = draftObject;
        this.draftObjectP.set(this.draftObject);
    }

    public void setAllApproved(boolean allApproved) {
        isAllApproved = allApproved;
        isAllApprovedP.set(this.isAllApproved);
    }

    @Override
    public String toString() {
        return super.toString()  +
                "draftObject='" + draftObject + '\'' +
                ", isAllApproved=" + isAllApproved +
                ", totalReceivers=" + totalReceivers;
    }
}