package com.bookmystay.booking;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// FIFO for booking requests
public class BookingQueueService {
    private final Queue<Reservation> queue = new LinkedList<>(); // preserves order

    public void addRequest(Reservation r) {
        queue.add(r);
        System.out.println("Enqueued: " + r.getGuestName() + " (" + r.getRoomType() + ")");
    }

    public Reservation pollNext() {
        return queue.poll(); // deque or null
    }

    public List<Reservation> getSnapshot() {
        return List.copyOf(queue);
    }

    public void printQueue() {
        if (queue.isEmpty()) {
            System.out.println("(empty)");
            return;
        }
        int i = 1;
        for (Reservation r : queue) {
            System.out.println(i++ + ". " + r.getGuestName() + " -> " + r.getRoomType());
        }
    }
}