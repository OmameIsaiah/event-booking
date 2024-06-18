package com.event.booking.enums;

import java.util.HashMap;
import java.util.Map;

public enum Category {
    CONCERT("Concert"),
    CONFERENCE("Conference"),
    GAME("Game");
    public final String label;
    private static final Map<String, Category> map = new HashMap<>();

    static {
        for (Category e : values()) {
            map.put(e.label, e);
        }
    }

    private Category(String label) {
        this.label = label;

    }

    public static Category valueOfName(String label) {
        return map.get(label);
    }
}
