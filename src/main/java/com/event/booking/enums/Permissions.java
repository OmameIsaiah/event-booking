package com.event.booking.enums;

import java.util.HashMap;
import java.util.Map;

public enum Permissions {
    CAN_VIEW_EVENTS("CAN_VIEW_EVENTS"),
    CAN_SEARCH_EVENTS("CAN_SEARCH_EVENTS"),
    CAN_RESERVE_EVENT_TICKET("CAN_RESERVE_EVENT_TICKET"),
    CAN_CREATE_EVENT("CAN_CREATE_EVENT"),
    CAN_SEND_EVENT_NOTIFICATION("CAN_SEND_EVENT_NOTIFICATION"),
    CAN_VIEW_USERS("CAN_VIEW_USERS"),
    CAN_ADD_ROLE("CAN_ADD_ROLE"),
    CAN_UPDATE_ROLE("CAN_UPDATE_ROLE"),
    CAN_DELETE_ROLE("CAN_DELETE_ROLE");

    public final String label;
    private static final Map<String, Permissions> map = new HashMap<>();

    static {
        for (Permissions e : values()) {
            map.put(e.label, e);
        }
    }

    private Permissions(String label) {
        this.label = label;

    }

    public static Permissions valueOfName(String label) {
        return map.get(label);
    }
}
