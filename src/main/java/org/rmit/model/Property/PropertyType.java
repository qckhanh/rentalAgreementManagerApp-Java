package org.rmit.model.Property;

public enum PropertyType {
    COMMERCIAL("Commercial"),
    RESIDENTIAL("Residential"),
    NONE("None");

    private final String type;

    PropertyType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}