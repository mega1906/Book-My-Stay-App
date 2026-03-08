package com.bookmystay.app;

import java.util.Scanner;
import com.bookmystay.inventory.InventoryService;

/**
 * BookMyStayApp
 * Main entry point for the application. Delegates to the Menu system.
 * This is where the program actually starts running.
 * @author Developer
 * @version 1.0
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        // Scanner for taking input from the keyboard
        Scanner sc = new Scanner(System.in);

        // Create the inventory service
        InventoryService inventory = new InventoryService();

        System.out.println("===== BookMyStay - Room Inventory (UC1) =====");

        // Load the default rooms (Single, Double, Suite)
        // We do this first so the user sees something in the system
        inventory.initializeDefaultRooms();

        boolean running = true;

        // Simple loop to repeat menu options until user chooses to exit
        while (running) {

            // Show the options the user can choose
            System.out.println("\nChoose an option:");
            System.out.println("1. Show Inventory");
            System.out.println("2. Add/Update Room Type");
            System.out.println("3. Update Room Count");
            System.out.println("4. Update Room Price");
            System.out.println("5. Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {

                case 1:
                    // Show all rooms with count and price
                    inventory.showInventory();
                    break;

                case 2:
                    // Ask the user for room details
                    System.out.print("Enter room type: ");
                    String type = sc.nextLine();

                    System.out.print("Enter count: ");
                    int count = sc.nextInt();

                    System.out.print("Enter price: ");
                    double price = sc.nextDouble();

                    // Add or update room type
                    inventory.addOrUpdateRoomType(type, count, price);
                    break;

                case 3:
                    // Update existing room count
                    System.out.print("Enter room type: ");
                    String cType = sc.nextLine();

                    System.out.print("Enter new count: ");
                    int newCount = sc.nextInt();

                    inventory.updateRoomCount(cType, newCount);
                    break;

                case 4:
                    // Update existing room price
                    System.out.print("Enter room type: ");
                    String pType = sc.nextLine();

                    System.out.print("Enter new price: ");
                    double newPrice = sc.nextDouble();

                    inventory.updateRoomPrice(pType, newPrice);
                    break;

                case 5:
                    // Exit the program loop
                    running = false;
                    System.out.println("Exiting... Thank you!");
                    break;

                default:
                    // If user enters something wrong
                    System.out.println("Invalid choice! Please choose again.");
            }
        }

        sc.close(); 
    }
}