package com.bookmystay.app;

import java.util.List;
import java.util.Scanner;

import com.bookmystay.inventory.InventoryService;
import com.bookmystay.search.SearchService;

/**
 * BookMyStayApp
 * Main entry point for the application. Delegates to the Menu system.
 * This now includes search & availability along with inventory setup.
 * @author Developer
 * @version 2.0
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        // Scanner to take input from user in the console
        Scanner sc = new Scanner(System.in);

        InventoryService inventory = new InventoryService();
        inventory.initializeDefaultRooms(); 

        SearchService searchService = new SearchService(inventory);

        System.out.println("===== BookMyStay - Room Inventory & Search =====");

        boolean running = true;

        while (running) {

            // Very simple menu
            System.out.println("\nChoose an option:");
            System.out.println("1. Show Inventory");
            System.out.println("2. Add/Update Room Type");
            System.out.println("3. Update Room Count");
            System.out.println("4. Update Room Price");
            System.out.println("5. Show Available Room Types");
            System.out.println("6. View Room Details");
            System.out.println("7. Exit");

            System.out.print("Enter choice: ");
            int choice = safeReadInt(sc);

            switch (choice) {

                case 1 -> inventory.showInventory();

                case 2 -> {
                    System.out.print("Enter room type: ");
                    String type = sc.nextLine().trim();

                    System.out.print("Enter count (required): ");
                    int count = safeReadInt(sc);

                    // Price is optional: blank -> keep previous
                    System.out.print("Enter price (optional, press Enter to keep previous): ");
                    String priceStr = sc.nextLine().trim();
                    Double price = null; // null means "keep previous"
                    if (!priceStr.isEmpty()) {
                        price = safeParseDouble(priceStr);
                    }

                    // Amenities are optional: blank -> keep previous
                    System.out.print("Enter amenities (comma separated, optional, press Enter to keep previous): ");
                    String amenitiesCsv = sc.nextLine();
                    boolean amenitiesProvided = !amenitiesCsv.trim().isEmpty();

                    // Upsert: count required; price/amenities are optional
                    inventory.upsertRoomType(type, count, price, amenitiesProvided ? amenitiesCsv : null);

                    System.out.println("Room type processed successfully.");
                }

                case 3 -> {
                    System.out.print("Enter room type: ");
                    String type = sc.nextLine().trim();
                    System.out.print("Enter new count: ");
                    int newCount = safeReadInt(sc);
                    inventory.updateRoomCount(type, newCount);
                }

                case 4 -> {
                    System.out.print("Enter room type: ");
                    String type = sc.nextLine().trim();
                    System.out.print("Enter new price: ");
                    double newPrice = safeReadDouble(sc);
                    inventory.updateRoomPrice(type, newPrice);
                }

                case 5 -> {
                    // List only room types that are currently available (count > 0)
                    System.out.println("\nAvailable room types right now:");
                    List<String> available = searchService.getAvailableRoomTypes();
                    if (available.isEmpty()) {
                        System.out.println("No rooms are available at the moment.");
                    } else {
                        for (String rt : available) {
                            System.out.println("- " + rt);
                        }
                    }
                }

                case 6 -> {
                    // Show available first, then ask for a type
                    System.out.println("\nAvailable room types:");
                    List<String> available = searchService.getAvailableRoomTypes();
                    if (available.isEmpty()) {
                        System.out.println("No rooms available right now.");
                    } else {
                        for (String rt : available) {
                            System.out.println("- " + rt);
                        }
                    }

                    System.out.print("Enter room type to view details: ");
                    String type = sc.nextLine().trim();

                    if (!searchService.exists(type)) {
                        System.out.println("That room type does not exist.");
                    } else {
                        // Note: these are read-only lookups
                        int count = searchService.getAvailableCount(type);
                        double price = searchService.getPrice(type);
                        List<String> amenities = searchService.getAmenities(type);

                        System.out.println("\nRoom Type: " + inventory.getDisplayName(type));
                        System.out.println("Available: " + count);
                        System.out.println("Price: " + price);
                        System.out.println("Amenities: " + (amenities.isEmpty() ? "-" : String.join(", ", amenities)));
                    }
                }

                case 7 -> {
                    running = false;
                    System.out.println("Exiting... Thank you!");
                }

                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static int safeReadInt(Scanner sc) {
        while (true) {
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid integer: ");
            }
        }
    }

    private static double safeReadDouble(Scanner sc) {
        while (true) {
            String line = sc.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number (e.g., 129.99): ");
            }
        }
    }

    private static double safeParseDouble(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return 0.0; // fallback, though we only call this when non-empty; guarded earlier
        }
    }
}