# Book-My-Stay-App

### Use Case 1: Room Inventory

A simple Java program for managing hotel room inventory using HashMaps.

## Features
- Add or update room types  
- Change room count or price  
- View all room inventory  
- Uses Scanner for user input

### Use Case 2: Room Search & Availability

Adds **read-only search** on top of UC1:
- Show available room types (count > 0)
- View price and amenities for a room type
- Defensive checks (room type existence)
- Search layer does **not** modify inventory

### Use Case 3: Booking Request (First-Come-First-Served)

FIFO booking requests with a simple queue and a 3-guest simulation.

## Features
- Case-insensitive room types
- Add/update room types (price/amenities optional)
- View inventory and available room types
- Read-only room details lookup
- Enqueue booking requests (FIFO)
- Simulation: Rohan, Mia, Zara enqueued 1s apart

### Use Case 4: Reservation Confirmation & Room Allocation

Adds confirmation + unique room allocation:
- FIFO: process next/all from queue
- Unique room IDs per type (e.g., SINGLE-001)
- Immediate inventory decrement
- No double-booking (IDs stored in a set)
- View assigned rooms

### Use Case 5: Add‑On Service Selection

Adds service management on top of confirmed bookings:
- Map: reservation Room ID → List<Service>
- Attach multiple services (Breakfast, Spa, Airport Pickup)
- View services and total extra cost

## How to use
- Confirm a booking (5 → 6) to get a Room ID (e.g., `SINGLE-001`)
- Use 9 to add a service to that Room ID
- Use 10 to view its services and total cost

### Use Case 6: Booking History and Reporting

Adds history & reporting on top of previous steps:

- Store confirmed reservations in an ordered list
- Support cancellation (frees inventory, keeps audit trail)
- Simple report: totals + recent items
- Optional inline cancel from the report screen

## Fetures
- History records confirmations and cancellations.
- Cancel from the report: enter Room ID when prompted.
- Confirmed list hides canceled items; history keeps them with status.
