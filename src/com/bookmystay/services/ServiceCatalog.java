package com.bookmystay.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceCatalog {
    private static final Map<String, Service> catalog = new HashMap<>();
    static {
        catalog.put("breakfast", new Service("breakfast", "Breakfast", 15.0));
        catalog.put("spa",       new Service("spa",       "Spa Session", 50.0));
        catalog.put("pickup",    new Service("pickup",    "Airport Pickup", 25.0));
    }

    public static Service get(String codeOrName) {
        if (codeOrName == null) return null;
        String key = codeOrName.trim().toLowerCase();
        // exact code match
        if (catalog.containsKey(key)) return catalog.get(key);
        // match by display name (case-insensitive)
        for (Service s : catalog.values()) {
            if (s.getDisplayName().equalsIgnoreCase(codeOrName)) return s;
        }
        return null;
    }

    public static List<Service> list() {
        return new ArrayList<>(catalog.values());
    }
}