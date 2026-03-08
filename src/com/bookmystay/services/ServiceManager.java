package com.bookmystay.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceManager {
    private final Map<String, List<Service>> byReservationId = new HashMap<>();

    public void addService(String reservationId, Service service) {
        byReservationId.computeIfAbsent(reservationId, k -> new ArrayList<>()).add(service);
    }

    public List<Service> getServices(String reservationId) {
        return new ArrayList<>(byReservationId.getOrDefault(reservationId, new ArrayList<>()));
    }

    public double getTotalCost(String reservationId) {
        double sum = 0.0;
        for (Service s : byReservationId.getOrDefault(reservationId, new ArrayList<>())) {
            sum += s.getPrice();
        }
        return sum;
    }
}