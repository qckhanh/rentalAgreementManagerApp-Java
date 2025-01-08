package org.rmit.model.Property;

//import UIHelper.UserInterfaceManager;
//import UIHelper.DateCreator;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

@Entity
@DiscriminatorValue("RESIDENTIAL")
public class ResidentialProperty extends Property {
    private int totalRoom;
    private int totalBedroom;
    private boolean isPetAllowed;
    private boolean hasGarden;

    //////////////////////////////////
    @Transient
    transient private IntegerProperty totalRoomProperty = new SimpleIntegerProperty();
    @Transient
    transient private IntegerProperty totalBedroomProperty = new SimpleIntegerProperty();
    @Transient
    transient private BooleanProperty isPetAllowedProperty = new SimpleBooleanProperty();
    @Transient
    transient private BooleanProperty hasGardenProperty = new SimpleBooleanProperty();


    public ResidentialProperty() {
        super();
    }

    @Override
    protected void synWithSimpleProperty() {
        super.ownerProperty.setValue(super.getOwner());
        super.addressProperty.setValue(super.getAddress());
        super.priceProperty.setValue(super.getPrice());
        super.statusProperty.setValue(super.getStatus());
        super.typeProperty.setValue(super.getType());
        super.idProperty.setValue(super.getId());
        super.hostsProperty.setValue(super.getHosts());
        super.agreementListProperty.setValue(super.getAgreementList());
        super.imagesProperty.setValue(super.getImages());


        this.totalRoomProperty.setValue(this.totalRoom);
        this.totalBedroomProperty.setValue(this.totalBedroom);
        this.isPetAllowedProperty.setValue(this.isPetAllowed);
        this.hasGardenProperty.setValue(this.hasGarden);
    }

    public int getTotalRoom() {
        return totalRoom;
    }

    public void setTotalRoom(int totalRoom) {
        this.totalRoomProperty.setValue(totalRoom);
        this.totalRoom = totalRoomProperty.get();
    }

    public int getTotalBedroom() {
        return totalBedroom;
    }

    public void setTotalBedroom(int totalBedroom) {
        this.totalBedroomProperty.setValue(totalBedroom);
        this.totalBedroom = totalBedroomProperty.get();
    }

    public boolean isPetAllowed() {
        return isPetAllowed;
    }

    public void setPetAllowed(boolean petAllowed) {
        this.isPetAllowedProperty.setValue(petAllowed);
        this.isPetAllowed = isPetAllowedProperty.get();
    }

    public boolean isHasGarden() {
        return hasGarden;
    }

    public void setHasGarden(boolean hasGarden) {
        this.hasGardenProperty.setValue(hasGarden);
        this.hasGarden = hasGardenProperty.get();
    }
/////////////////////////////
    public int getTotalRoomProperty() {
        return totalRoomProperty.get();
    }

    public IntegerProperty totalRoomPropertyProperty() {
        return totalRoomProperty;
    }

    public int getTotalBedroomProperty() {
        return totalBedroomProperty.get();
    }

    public IntegerProperty totalBedroomPropertyProperty() {
        return totalBedroomProperty;
    }

    public boolean isIsPetAllowedProperty() {
        return isPetAllowedProperty.get();
    }

    public BooleanProperty isPetAllowedPropertyProperty() {
        return isPetAllowedProperty;
    }

    public boolean isHasGardenProperty() {
        return hasGardenProperty.get();
    }

    public BooleanProperty hasGardenPropertyProperty() {
        return hasGardenProperty;
    }

    @Override
    public String toString() {
        return  "ID - " + super.getId();
    }
}
