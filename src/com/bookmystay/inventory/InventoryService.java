package com.bookmystay.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryService {

    // normalizedType (lowercase) -> available count
    private final Map<String, Integer> roomCounts = new HashMap<>();

    // normalizedType (lowercase) -> price per night
    private final Map<String, Double> roomPrices = new HashMap<>();

    // normalizedType (lowercase) -> amenities CSV (e.g., "WiFi,TV,AC")
    private final Map<String, String> roomAmenitiesCsv = new HashMap<>();

    // normalizedType (lowercase) -> display name (e.g., "Single")
    private final Map<String, String> roomDisplayName = new HashMap<>();

    /**
     * Initializes some default rooms with basic amenities.
     */
    public void initializeDefaultRooms() {
        upsertRoomType("Single", 10, 100.0, "WiFi,TV");
        upsertRoomType("Double", 5, 180.0, "WiFi,TV,AC");
        upsertRoomType("Suite", 2, 250.0, "WiFi,TV,AC,Breakfast");

        System.out.println("Default room inventory loaded.");
    }

    private String norm(String type) {
        return type == null ? "" : type.trim().toLowerCase();
    }

    private String displayOrFallback(String input, String normalized) {
        if (input != null && !input.trim().isEmpty()) return input.trim();
        return roomDisplayName.getOrDefault(normalized, normalized);
    }

    /**
     * Upsert used during initialization or admin ops when all values are present.
     */
    private void upsertRoomType(String type, int count, double price, String amenitiesCsv) {
        String key = norm(type);
        roomCounts.put(key, count);
        roomPrices.put(key, price);
        roomAmenitiesCsv.put(key, amenitiesCsv == null ? "" : amenitiesCsv.trim());
        roomDisplayName.put(key, displayOrFallback(type, key));
    }

    /**
     * Count is required; price and amenities are optional:
     *  - if price == null -> keep previous (if exists)
     *  - if amenitiesCsv == null -> keep previous (if exists)
     * If the room is new and optional fields are null, defaults are applied (0.0 / "").
     */
    public void upsertRoomType(String type, int count, Double price, String amenitiesCsvOrNull) {
        String key = norm(type);
        boolean exists = roomCounts.containsKey(key);

        roomCounts.put(key, count);

        if (price != null) {
            // update price if user provided it
            roomPrices.put(key, price);
        } else if (!exists) {
            // new room but price not provided -> default 0.0
            roomPrices.put(key, 0.0);
        } // else keep previous

        if (amenitiesCsvOrNull != null) {
            roomAmenitiesCsv.put(key, amenitiesCsvOrNull.trim());
        } else if (!exists) {
            roomAmenitiesCsv.put(key, ""); // default empty if new
        } // else keep previous

        // Always refresh display name, but prefer existing if present
        roomDisplayName.put(key, displayOrFallback(type, key));
    }

    public void updateRoomCount(String type, int newCount) {
        String key = norm(type);
        if (roomCounts.containsKey(key)) {
            roomCounts.put(key, newCount);
            System.out.println("Updated room count for " + getDisplayName(type));
        } else {
            System.out.println("Room type not found: " + type);
        }
    }

    public void updateRoomPrice(String type, double newPrice) {
        String key = norm(type);
        if (roomPrices.containsKey(key)) {
            roomPrices.put(key, newPrice);
            System.out.println("Updated price for " + getDisplayName(type));
        } else {
            System.out.println("Room type not found: " + type);
        }
    }

    /**
     * Shows the full inventory (for admin to review).
     * Prints room type, available count, price, and amenities CSV.
     */
    public void showInventory() {
        System.out.println("\n---- Current Room Inventory ----");
        if (roomCounts.isEmpty()) {
            System.out.println("(empty)");
        } else {
            for (String key : roomCounts.keySet()) {
                String display = roomDisplayName.getOrDefault(key, key);
                int count = roomCounts.getOrDefault(key, 0);
                double price = roomPrices.getOrDefault(key, 0.0);
                String amenities = roomAmenitiesCsv.getOrDefault(key, "");
                System.out.println(display + " | Count: " + count + " | Price: " + price + " | Amenities: " + amenities);
            }
        }
        System.out.println("--------------------------------");
    }

    // For search: check if room type exists (case-insensitive)
    public boolean exists(String type) {
        return roomCounts.containsKey(norm(type)) && roomPrices.containsKey(norm(type));
    }

    // For search: list only room types with count > 0 (using display names)
    public List<String> getAvailableRoomTypes() {
        List<String> result = new ArrayList<>();
        for (String key : roomCounts.keySet()) {
            Integer c = roomCounts.get(key);
            if (c != null && c > 0) {
                result.add(roomDisplayName.getOrDefault(key, key));
            }
        }
        return result;
    }

    // For search: current count
    public int getAvailableCount(String type) {
        return roomCounts.getOrDefault(norm(type), 0);
    }

    // For search: current price
    public double getPrice(String type) {
        return roomPrices.getOrDefault(norm(type), 0.0);
    }

    // For search: amenities as a list (split CSV on commas)
    public List<String> getAmenities(String type) {
        String csv = roomAmenitiesCsv.getOrDefault(norm(type), "");
        List<String> list = new ArrayList<>();
        if (!csv.isBlank()) {
            for (String part : csv.split(",")) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) list.add(trimmed);
            }
        }
        return list;
    }

    // Helper to get a nice display name from any casing of input
    public String getDisplayName(String type) {
        return roomDisplayName.getOrDefault(norm(type), type);
    }
}