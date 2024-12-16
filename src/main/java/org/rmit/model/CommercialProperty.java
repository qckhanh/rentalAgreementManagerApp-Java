package org.rmit.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import javafx.beans.property.*;

@Entity(name = "CommercialProperty")
public class CommercialProperty extends Property {
    private String businessType;
    private int totalParkingSpace;
    private double squareMeters;
    ////////////////////////////////
    @Transient
    transient private StringProperty businessTypeProperty = new SimpleStringProperty();
    @Transient
    transient private IntegerProperty totalParkingSpaceProperty = new SimpleIntegerProperty();
    @Transient
    transient private DoubleProperty squareMetersProperty = new SimpleDoubleProperty();

    public CommercialProperty() {
        super();
    }

    @Override
    protected void synWithSimpleProperty() {

        super.ownerProperty.setValue(super.getOwner());
        super.addressProperty.setValue(super.getAddress());
        super.priceProperty.setValue(super.getPrice());
        super.statusProperty.setValue(super.getStatus());
        super.idProperty.setValue(super.getId());
        super.hostsProperty.setValue(super.getHosts());
        super.agreementListProperty.setValue(super.getAgreementList());

        this.businessTypeProperty.setValue(this.businessType);
        this.totalParkingSpaceProperty.setValue(this.totalParkingSpace);
        this.squareMetersProperty.setValue(this.squareMeters);
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessTypeProperty.setValue(businessType);
        this.businessType = businessTypeProperty.get();
    }

    public int getTotalParkingSpace() {
        return totalParkingSpace;
    }

    public void setTotalParkingSpace(int totalParkingSpace) {
        this.totalParkingSpace = totalParkingSpace;
        this.totalParkingSpaceProperty.setValue(totalParkingSpace);
    }

    public double getSquareMeters() {
        return squareMeters;
    }

    public void setSquareMeters(double squareMeters) {
        this.squareMetersProperty.setValue(squareMeters);
        this.squareMeters = squareMetersProperty.get();
    }
    ////////////////


    public String getBusinessTypeProperty() {
        return businessTypeProperty.get();
    }

    public StringProperty businessTypePropertyProperty() {
        return businessTypeProperty;
    }

    public int getTotalParkingSpaceProperty() {
        return totalParkingSpaceProperty.get();
    }

    public IntegerProperty totalParkingSpacePropertyProperty() {
        return totalParkingSpaceProperty;
    }

    public double getSquareMetersProperty() {
        return squareMetersProperty.get();
    }

    public DoubleProperty squareMetersPropertyProperty() {
        return squareMetersProperty;
    }

    @Override
    public String toString() {
        return super.toString() +  "CommercialProperty{" +
                "businessType='" + businessTypeProperty.getValue() + '\'' +
                ", totalParkingSpace=" + totalParkingSpaceProperty.getValue() +
                ", squareMeters=" + squareMetersProperty.getValue() +
                ", businessType=" + businessTypeProperty.getValue() +
                ", totalParkingSpace=" + totalParkingSpaceProperty.getValue() +
                ", squareMeters=" + squareMetersProperty.getValue() +
                '}';
    }
}

