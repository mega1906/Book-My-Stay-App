package com.bookmystay.booking;

// Value object for a booking request
public class Reservation {
	private final String guestName;
	private final String roomType;
	private final long createdAt;

	private boolean confirmed = false;
	private String allocatedRoomId;


	public Reservation(String guestName, String roomType) {
		this.guestName = guestName;
		this.roomType = roomType;
		this.createdAt = System.currentTimeMillis(); // timestamp set on creation
	}

	// Getters and Setters
	public String getGuestName() { return guestName; }
	public String getRoomType() { return roomType; }
	public long getCreatedAt() { return createdAt; }


	public boolean isConfirmed() { return confirmed; }
	public String getAllocatedRoomId() { return allocatedRoomId; }

	public void setConfirmed(boolean confirmed) { this.confirmed = confirmed; }
	public void setAllocatedRoomId(String allocatedRoomId) { this.allocatedRoomId = allocatedRoomId; }


	@Override
	public String toString() {
		return guestName + " -> " + roomType + " @ " + createdAt;
	}
}