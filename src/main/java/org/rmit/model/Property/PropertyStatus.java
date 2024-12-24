package org.rmit.model.Property;

public enum PropertyStatus {
    AVAILABLE("Available"),
    UNAVAILABLE("Unavailable"),
    RENTED("Rented"),
    UNDER_MAINTENANCE("Under Maintenance"),
    NONE("None");

    private final String status;

    PropertyStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}