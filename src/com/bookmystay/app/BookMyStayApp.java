package com.bookmystay.app;

import java.util.List;
import java.util.Scanner;

import com.bookmystay.booking.AllocationService;
import com.bookmystay.booking.BookingQueueService;
import com.bookmystay.booking.Reservation;
import com.bookmystay.inventory.InventoryService;
import com.bookmystay.search.SearchService;

/**
 * BookMyStayApp
 * Main entry point for the application. Delegates to the Menu system.
 * @author Developer
 * @version 4.0
 */
public class BookMyStayApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        InventoryService inventory = new InventoryService();
        inventory.initializeDefaultRooms();

        SearchService search = new SearchService(inventory);
        BookingQueueService bookingQueue = new BookingQueueService();
        AllocationService allocation = new AllocationService();

        // Simulation
        if (args != null) {
            for (String a : args) {
                if ("simulate".equalsIgnoreCase(a)) {
                    devSimulate(bookingQueue, inventory);
                }
            }
        }

        boolean running = true;
        // Loop
        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1. View Room Inventory");
            System.out.println("2. Update Room Count");
            System.out.println("3. Update Room Price");
            System.out.println("4. Search Available Rooms");
            System.out.println("5. Add Booking Request");
            System.out.println("6. Process Next Booking");
            System.out.println("7. View Booking Queue");
            System.out.println("8. View Confirmed Reservations");
            System.out.println("9. Exit");
            System.out.print("Enter choice: ");
            int choice = safeReadInt(sc);

            switch (choice) {
                case 1: {
                    inventory.showInventory();
                    break;
                }
                case 2: {
                    System.out.print("Enter room type: ");
                    String type = sc.nextLine().trim();
                    System.out.print("Enter new count: ");
                    int newCount = safeReadInt(sc);
                    inventory.updateRoomCount(type, newCount);
                    break;
                }
                case 3: {
                    System.out.print("Enter room type: ");
                    String type = sc.nextLine().trim();
                    System.out.print("Enter new price: ");
                    double newPrice = safeReadDouble(sc);
                    inventory.updateRoomPrice(type, newPrice);
                    break;
                }
                case 4: {
                    List<String> available = search.getAvailableRoomTypes();
                    if (available.isEmpty()) {
                        System.out.println("(no rooms available)");
                    } else {
                        System.out.println("Available rooms:");
                        for (String rt : available) {
                            int c = search.getAvailableCount(rt);
                            double p = search.getPrice(rt);
                            System.out.println("- " + rt + " | Count: " + c + " | Price: " + p);
                        }
                    }
                    break;
                }
                case 5: {
                    System.out.print("Guest name: ");
                    String guest = sc.nextLine().trim();
                    System.out.println("Available room types:");
                    List<String> avail = search.getAvailableRoomTypes();
                    if (avail.isEmpty()) {
                        System.out.println("(none)");
                        break;
                    }
                    for (String rt : avail) System.out.println("- " + rt);
                    System.out.print("Enter room type: ");
                    String roomType = sc.nextLine().trim();
                    if (!search.exists(roomType)) {
                        System.out.println("Not found.");
                        break;
                    }
                    bookingQueue.addRequest(new Reservation(guest, inventory.getDisplayName(roomType)));
                    break;
                }
                case 6: {
                    Reservation confirmed = allocation.processNext(bookingQueue, inventory);
                    if (confirmed == null) System.out.println("Nothing processed.");
                    break;
                }
                case 7: {
                    bookingQueue.printQueue();
                    break;
                }
                case 8: {
                    allocation.printConfirmed();
                    break;
                }
                case 9: {
                    running = false;
                    break;
                }
                default: {
                    System.out.println("Invalid choice.");
                }
            }
        }
    }

    private static int safeReadInt(Scanner sc) {
        while (true) {
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Enter a number: ");
            }
        }
    }

    private static double safeReadDouble(Scanner sc) {
        while (true) {
            String line = sc.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.print("Enter a number (e.g., 129.99): ");
            }
        }
    }

    // Enqueuing requests
    private static void devSimulate(BookingQueueService bookingQueue, InventoryService inventory) {
        java.util.List<String> avail = inventory.getAvailableRoomTypes();
        if (avail.isEmpty()) return;
        String pick = avail.get(0);
        try {
            bookingQueue.addRequest(new Reservation("Rohan (Guest 1)", pick));
            Thread.sleep(1000);
            bookingQueue.addRequest(new Reservation("Mia (Guest 2)", pick));
            Thread.sleep(1000);
            bookingQueue.addRequest(new Reservation("Zara (Guest 3)", pick));
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
    }
}