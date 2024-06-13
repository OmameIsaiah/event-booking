package com.event.booking.enums;

import java.util.HashMap;
import java.util.Map;

public enum EventCategory {
    CONCERT("Concert"),
    CONFERENCE("Conference"),
    GAME("Game");
    public final String label;
    private static final Map<String, EventCategory> map = new HashMap<>();

    static {
        for (EventCategory e : values()) {
            map.put(e.label, e);
        }
    }

    private EventCategory(String label) {
        this.label = label;

    }

    public static EventCategory valueOfName(String label) {
        return map.get(label);
    }
}
