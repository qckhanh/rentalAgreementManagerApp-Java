package org.rmit.model;

public enum PropertyStatus {
    AVAILABLE("Available"),
    UNAVAILABLE("Unavailable"),
    RENTED("Rented"),
    UNDER_MAINTENANCE("Under Maintenance");

    private final String status;

    PropertyStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}