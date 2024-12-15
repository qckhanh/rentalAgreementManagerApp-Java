package org.rmit.entity;

//import UIHelper.UserInterfaceManager;
//import UIHelper.DateCreator;

import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ResidentialProperty extends Property {
    private int totalRoom;
    private int totalBedroom;
    private boolean isPetAllowed;
    private boolean hasGarden;

    public ResidentialProperty() {
        super();
    }

    public int getTotalRoom() {
        return totalRoom;
    }

    public void setTotalRoom(int totalRoom) {
        this.totalRoom = totalRoom;
    }

    public int getTotalBedroom() {
        return totalBedroom;
    }

    public void setTotalBedroom(int totalBedroom) {
        this.totalBedroom = totalBedroom;
    }

    public boolean isPetAllowed() {
        return isPetAllowed;
    }

    public void setPetAllowed(boolean petAllowed) {
        isPetAllowed = petAllowed;
    }

    public boolean isHasGarden() {
        return hasGarden;
    }

    public void setHasGarden(boolean hasGarden) {
        this.hasGarden = hasGarden;
    }
}
