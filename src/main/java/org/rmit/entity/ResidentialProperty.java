package org.rmit.entity;

//import UIHelper.UserInterfaceManager;
//import UIHelper.DateCreator;

import java.util.ArrayList;
import java.util.List;

public class ResidentialProperty extends Property {
    private int totalRoom;
    private int totalBedroom;
    private boolean isPetAllowed;
    private boolean hasGarden;

    public ResidentialProperty(String address, double price, PropertyStatus status, int totalRoom, int totalBedroom, boolean isPetAllowed, boolean hasGarden) {
        super(address, price, status);
        this.totalRoom = totalRoom;
        this.totalBedroom = totalBedroom;
        this.isPetAllowed = isPetAllowed;
        this.hasGarden = hasGarden;
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

    @Override
    public String toString() {
        return super. toString() +
                " Total Room = " + totalRoom +
                ", Total Bedroom = " + totalBedroom +
                ", PetAllowed = " + isPetAllowed +
                ", Garden = " + hasGarden +
                ']';
    }

}
