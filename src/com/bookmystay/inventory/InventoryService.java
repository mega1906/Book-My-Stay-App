package com.bookmystay.inventory;

import java.util.HashMap;

/**
 * InventoryService for Use Case 1.
 *
 * This class is responsible for handling room inventory:
 * - room counts
 * - room prices
 *
 * Very simple: uses two HashMaps.
 * Think of HashMap like a mini-database stored in memory.
 */
public class InventoryService {

    // Stores number of rooms available for each type.
    // Example: "Single" -> 10
    private HashMap<String, Integer> roomCounts = new HashMap<>();

    // Stores price per night for each type.
    // Example: "Single" -> 100.0
    private HashMap<String, Double> roomPrices = new HashMap<>();


    /**
     * Initializes the default rooms in the hotel.
     * This simulates an admin configuring the hotel when the app first starts.
     */
    public void initializeDefaultRooms() {

        // Add three room types with sample data
        roomCounts.put("Single", 10);
        roomPrices.put("Single", 100.0);

        roomCounts.put("Double", 5);
        roomPrices.put("Double", 180.0);

        roomCounts.put("Suite", 2);
        roomPrices.put("Suite", 250.0);

        System.out.println("Default room inventory loaded.");
    }


    /**
     * Adds a new room type OR updates an existing one.
     * This is useful when the hotel expands or changes prices.
     */
    public void addOrUpdateRoomType(String type, int count, double price) {
        roomCounts.put(type, count);
        roomPrices.put(type, price);
        System.out.println(type + " room added/updated.");
    }


    /**
     * Updates only the room count.
     * Example: If 3 rooms are under maintenance, admin may reduce count.
     */
    public void updateRoomCount(String type, int newCount) {

        if (roomCounts.containsKey(type)) {
            roomCounts.put(type, newCount);
            System.out.println("Updated room count for " + type);
        } else {
            // If user tries updating a room type that doesn't exist
            System.out.println("Room type not found: " + type);
        }
    }


    /**
     * Updates only the price for a room type.
     * Example: Seasonal pricing changes.
     */
    public void updateRoomPrice(String type, double newPrice) {

        if (roomPrices.containsKey(type)) {
            roomPrices.put(type, newPrice);
            System.out.println("Updated price for " + type);
        } else {
            System.out.println("Room type not found: " + type);
        }
    }


    /**
     * Shows the full inventory (for admin to review).
     * Prints room type, available count, and price.
     */
    public void showInventory() {

        System.out.println("\n---- Current Room Inventory ----");

        // Loop through all room types
        for (String type : roomCounts.keySet()) {

            int count = roomCounts.get(type);
            double price = roomPrices.get(type);

            // Display each row in simple format
            System.out.println(type + " | Count: " + count + " | Price: " + price);
        }

        System.out.println("--------------------------------");
    }
}