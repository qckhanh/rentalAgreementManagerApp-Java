package org.rmit.Notification;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import org.rmit.model.Persons.Person;

import java.util.HashMap;
import java.util.Map;

@Entity
@DiscriminatorValue("REQUEST")
public class Request extends Notification {

    @ElementCollection
    private Map<Person, Boolean> approvalStatus = new HashMap<>();
    private String draftObject;

    public Request(Person sender) {
        super(sender);
    }

    public Request() {
        super();
    }

    public Map<Person, Boolean> getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Map<Person, Boolean> approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getDraftObject() {
        return draftObject;
    }

    public void setDraftObject(String draftObject) {
        this.draftObject = draftObject;
    }

    @Override
    public void addReceiver(Person person) {
        super.addReceiver(person);
        approvalStatus.put(person, false);
    }

    // Getters and setters...

    public boolean approve(Person receiver) {
        if (approvalStatus.containsKey(receiver)) {
            approvalStatus.put(receiver, true);
            System.out.println(receiver.getName() + " has approved the request.");
            return true;
        } else {
            System.out.println(receiver.getName() + " is not a valid receiver for this request.");
            return false;
        }
    }

    public boolean deny(Person receiver) {
        if (!approvalStatus.containsKey(receiver)) return false;
        approvalStatus.put(receiver, false);
        return true;
    }

    public boolean isAllApproved() {
        return approvalStatus.values().stream().allMatch(approved -> approved);
    }

    @Override
    public void performAction() {
        System.out.println("All receivers approved. Performing the requested action!");
        // Add your specific action here.
    }
}