package org.rmit.entity;

import jakarta.persistence.Entity;

@Entity(name = "CommercialProperty")
public class CommercialProperty extends Property {
    private String businessType;
    private int totalParkingSpace;
    private double squareMeters;
    public CommercialProperty() {
        super();
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public int getTotalParkingSpace() {
        return totalParkingSpace;
    }

    public void setTotalParkingSpace(int totalParkingSpace) {
        this.totalParkingSpace = totalParkingSpace;
    }

    public double getSquareMeters() {
        return squareMeters;
    }

    public void setSquareMeters(double squareMeters) {
        this.squareMeters = squareMeters;
    }
}

