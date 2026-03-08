package com.bookmystay.app;

import java.util.List;
import java.util.Scanner;

import com.bookmystay.booking.BookingQueueService;
import com.bookmystay.booking.Reservation;
import com.bookmystay.inventory.InventoryService;
import com.bookmystay.search.SearchService;

/**
 * BookMyStayApp
 * Main entry point for the application. Delegates to the Menu system.
 * @author Developer
 * @version 3.0
 */
public class BookMyStayApp {

    public static void main(String[] args) {
    	// Scanner to take input from user in the console
        Scanner sc = new Scanner(System.in);

        InventoryService inventory = new InventoryService();
        inventory.initializeDefaultRooms();

        SearchService searchService = new SearchService(inventory);
        BookingQueueService bookingService = new BookingQueueService();

        System.out.println("===== BookMyStay =====");

        boolean running = true;
        
        // Loop 
        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Show Inventory");
            System.out.println("2. Add/Update Room Type");
            System.out.println("3. Update Room Count");
            System.out.println("4. Update Room Price");
            System.out.println("5. Show Available Room Types");
            System.out.println("6. View Room Details");
            System.out.println("7. Simulate Booking Requests");
            System.out.println("8. Exit");
            System.out.print("Enter choice: ");
            int choice = safeReadInt(sc);

            switch (choice) {
                case 1 -> inventory.showInventory();

                case 2 -> {
                    System.out.print("Enter room type: ");
                    String type = sc.nextLine().trim();
                    System.out.print("Enter count (required): ");
                    int count = safeReadInt(sc);

                    System.out.print("Enter price (optional, Enter to keep): ");
                    String priceStr = sc.nextLine().trim();
                    Double price = priceStr.isEmpty() ? null : safeParseDouble(priceStr);

                    System.out.print("Enter amenities (comma separated, optional, Enter to keep): ");
                    String amenitiesCsv = sc.nextLine();
                    boolean amenitiesProvided = !amenitiesCsv.trim().isEmpty();

                    inventory.upsertRoomType(type, count, price, amenitiesProvided ? amenitiesCsv : null);
                    System.out.println("Done.");
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
                    System.out.println("\nAvailable room types:");
                    List<String> available = searchService.getAvailableRoomTypes();
                    if (available.isEmpty()) System.out.println("(none)");
                    else available.forEach(rt -> System.out.println("- " + rt));
                }

                case 6 -> {
                    System.out.println("\nAvailable room types:");
                    List<String> available = searchService.getAvailableRoomTypes();
                    if (available.isEmpty()) System.out.println("(none)");
                    else available.forEach(rt -> System.out.println("- " + rt));

                    System.out.print("Enter room type to view details: ");
                    String type = sc.nextLine().trim();

                    if (!searchService.exists(type)) {
                        System.out.println("Not found.");
                    } else {
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
                    System.out.println("\nAvailable room types:");
                    List<String> available = searchService.getAvailableRoomTypes();
                    if (available.isEmpty()) {
                        System.out.println("(none) — cannot simulate bookings.");
                        break;
                    } else {
                        available.forEach(rt -> System.out.println("- " + rt));
                    }

                    System.out.print("Enter room type to request: ");
                    String roomType = sc.nextLine().trim();

                    // Simulation
                    try {
                        bookingService.addRequest(new Reservation("Rohan (Guest 1)", inventory.getDisplayName(roomType)));
                        Thread.sleep(1000);

                        bookingService.addRequest(new Reservation("Mia (Guest 2)", inventory.getDisplayName(roomType)));
                        Thread.sleep(1000);

                        bookingService.addRequest(new Reservation("Zara (Guest 3)", inventory.getDisplayName(roomType)));
                        Thread.sleep(1000);

                    } catch (InterruptedException ignored) {}

                    System.out.println("\nQueue (first-in-first-out):");
                    bookingService.printQueue();
                }

                case 8 -> {
                    running = false;
                    System.out.println("Goodbye!");
                }

                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static int safeReadInt(Scanner sc) {
        while (true) {
            String line = sc.nextLine().trim();
            try { return Integer.parseInt(line); }
            catch (NumberFormatException e) { System.out.print("Enter a number: "); }
        }
    }

    private static double safeReadDouble(Scanner sc) {
        while (true) {
            String line = sc.nextLine().trim();
            try { return Double.parseDouble(line); }
            catch (NumberFormatException e) { System.out.print("Enter a number (e.g., 129.99): "); }
        }
    }

    private static double safeParseDouble(String s) {
        try { return Double.parseDouble(s.trim()); }
        catch (Exception e) { return 0.0; }  // fallback, though we only call this when non-empty; guarded earlier
    }
}