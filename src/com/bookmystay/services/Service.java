package com.bookmystay.services;

public class Service {
    private final String code;        // e.g., "breakfast"
    private final String displayName; // e.g., "Breakfast"
    private final double price;

    public Service(String code, String displayName, double price) {
        this.code = code.toLowerCase();
        this.displayName = displayName;
        this.price = price;
    }

    public String getCode() { return code; }
    public String getDisplayName() { return displayName; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return displayName + " (" + price + ")";
    }
}
