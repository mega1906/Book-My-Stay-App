package com.bookmystay.history;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.bookmystay.booking.Reservation;

public class BookingHistoryService {
    private final List<Reservation> history = new ArrayList<>();

    public void recordConfirmation(Reservation r) {
        if (r == null) return;
        if (r.getAllocatedRoomId() == null) return;
        if (!containsRoomId(r.getAllocatedRoomId())) history.add(r);
    }

    public void recordCancellation(Reservation r) {
        if (r == null) return;
        if (r.getAllocatedRoomId() == null) return;
        if (!containsRoomId(r.getAllocatedRoomId())) history.add(r);
        // if it was already in list, we keep same reference so status reflects latest
    }

    public List<Reservation> getAll() {
        List<Reservation> out = new ArrayList<>(history);
        out.sort(Comparator.comparingLong(BookingHistoryService::timeKey));
        return out;
    }

    private static long timeKey(Reservation r) {
        if (r.getConfirmedAt() != null) return r.getConfirmedAt();
        return r.getCreatedAt();
    }

    private boolean containsRoomId(String roomId) {
        for (Reservation r : history) {
            if (roomId.equalsIgnoreCase(r.getAllocatedRoomId())) return true;
        }
        return false;
    }
}