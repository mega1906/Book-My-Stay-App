package com.bookmystay.search;

import java.util.List;

import com.bookmystay.inventory.InventoryService;

/**
 * SearchService 
 * 
 * This class only calls getters from InventoryService.
 * It does NOT change any inventory data. This enforces the "read-only" idea.
 */
public class SearchService {

    private final InventoryService inventory;

    public SearchService(InventoryService inventory) {
        this.inventory = inventory;
    }

    // Show list of available room types (count > 0)
    public List<String> getAvailableRoomTypes() {
        return inventory.getAvailableRoomTypes();
    }

    // Check if a specific room type exists
    public boolean exists(String type) {
        return inventory.exists(type);
    }

    // Read-only detail lookups
    public int getAvailableCount(String type) {
        return inventory.getAvailableCount(type);
    }

    public double getPrice(String type) {
        return inventory.getPrice(type);
    }

    public List<String> getAmenities(String type) {
        return inventory.getAmenities(type);
    }
}