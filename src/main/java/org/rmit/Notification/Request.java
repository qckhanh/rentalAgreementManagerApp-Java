package org.rmit.Notification;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import org.rmit.model.Persons.Person;

import java.util.HashMap;
import java.util.Map;

@Entity
@DiscriminatorValue("REQUEST")
public class Request extends Notification {


//    @ElementCollection
//    private Map<Person, Boolean> approvalStatus = new HashMap<>();
    private String draftObject;
    boolean isAllApproved = false;

    public Request(Person sender) {
        super(sender);
    }

    public Request() {
        super();
    }

//    public Map<Person, Boolean> getApprovalStatus() {
//        return approvalStatus;
//    }

//    public void setApprovalStatus(Map<Person, Boolean> approvalStatus) {
//        this.approvalStatus = approvalStatus;
//    }

    public String getDraftObject() {
        return draftObject;
    }

    public void setDraftObject(String draftObject) {
        this.draftObject = draftObject;
    }

    @Override
    public void addReceiver(Person person) {
        super.addReceiver(person);
    }

    // Getters and setters...

    public boolean approve(Person receiver) {
        if(totalReceivers <= 0) return false;
        totalReceivers--;
        if(totalReceivers == 0)  isAllApproved = true;
        return true;
    }

    public boolean deny(Person receiver) {
        isAllApproved = false;
        return true;
    }

    public boolean isAllApproved() {
        return isAllApproved;
    }

    @Override
    public void performAction() {
        System.out.println("All receivers approved. Performing the requested action!");
        // Add your specific action here.
    }

    @Override
    public String toString() {
        return super.toString()  +
                "draftObject='" + draftObject + '\'' +
                ", isAllApproved=" + isAllApproved +
                ", totalReceivers=" + totalReceivers;
    }
}