package com.bookmystay.booking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bookmystay.inventory.InventoryService;

public class AllocationService {

    private final Set<String> bookedRoomIds = new HashSet<>();
    private final Map<String, Set<String>> assignedByType = new LinkedHashMap<>();
    private final Map<String, Integer> seqByType = new HashMap<>();
    private final List<Reservation> confirmed = new ArrayList<>();

    public Reservation processNext(BookingQueueService queueService, InventoryService inventory) {
        Reservation r = queueService.pollNext();
        if (r == null) return null;

        String type = r.getRoomType();
        int available = inventory.getAvailableCount(type);
        if (available <= 0) {
            System.out.println("No availability for " + inventory.getDisplayName(type));
            return null;
        }

        String key = norm(type);
        int next = seqByType.getOrDefault(key, 0) + 1;
        String roomId = makeRoomId(inventory.getDisplayName(type), next);
        while (bookedRoomIds.contains(roomId)) {
            next++;
            roomId = makeRoomId(inventory.getDisplayName(type), next);
        }
        seqByType.put(key, next);

        bookedRoomIds.add(roomId);
        assignedByType.computeIfAbsent(inventory.getDisplayName(type), k -> new HashSet<>()).add(roomId);

        inventory.updateRoomCount(type, available - 1);

        r.setAllocatedRoomId(roomId);
        r.setConfirmed(true);
        confirmed.add(r);

        System.out.println("Confirmed: " + r.getGuestName() + " -> " + r.getRoomType() + " | " + roomId);
        return r;
    }


public Reservation cancelReservation(String roomId, InventoryService inventory) {
        if (roomId == null || roomId.isBlank()) return null;
        Reservation target = null;
        for (Reservation r : new ArrayList<>(confirmed)) {
            if (roomId.equalsIgnoreCase(r.getAllocatedRoomId())) {
                target = r; break;
            }
        }
        if (target == null) return null;

        int available = inventory.getAvailableCount(target.getRoomType());
        inventory.updateRoomCount(target.getRoomType(), available + 1);

        target.setStatus("CANCELED");
        target.setConfirmed(false);
        target.setCanceledAt(System.currentTimeMillis());

        confirmed.remove(target);
        // bookedRoomIds not freed to avoid ID reuse; assignedByType left as-is for audit display
        return target;
    }

    public void printConfirmed() {
        if (confirmed.isEmpty()) {
            System.out.println("(no confirmed reservations)");
            return;
        }
        for (Reservation r : confirmed) {
            System.out.println("- " + r.getGuestName() + " | " + r.getRoomType() + " | " + r.getAllocatedRoomId());
        }
    }


    public List<Reservation> getConfirmed() {
        return new ArrayList<>(confirmed);
    }

    public Reservation findByRoomId(String roomId) {
        if (roomId == null || roomId.isEmpty()) return null;
        for (Reservation r : confirmed) {
            if (roomId.equalsIgnoreCase(r.getAllocatedRoomId())) return r;
        }
        return null;
    }

    private String makeRoomId(String displayType, int seq) {
        String shortType = displayType.replaceAll("\\s+", "").toUpperCase();
        return shortType + "-" + String.format("%03d", seq);
    }

    private String norm(String type) {
        return type == null ? "" : type.trim().toLowerCase();
    }
}