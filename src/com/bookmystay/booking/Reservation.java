package com.bookmystay.booking;

// Value object for a booking request
public class Reservation {
    private final String guestName;
    private final String roomType;
    private final long createdAt;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.createdAt = System.currentTimeMillis(); // timestamp set on creation
    }

    // Getters and Setters
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public long getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return guestName + " -> " + roomType + " @ " + createdAt;
    }
}