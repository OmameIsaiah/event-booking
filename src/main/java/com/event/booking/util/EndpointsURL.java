package com.event.booking.util;

public class EndpointsURL {
    public static final String AUTH_BASE_URL = "/api/v1";
    public static final String AUTH_TOKEN_URL = "/auth";

    public static final String ONBOARDING_BASE_URL = "/api/v1/users/onboarding";
    public static final String ONBOARDING_SIGNUP = "/signup";
    public static final String ONBOARDING_SEND_OTP = "/send/otp";
    public static final String ONBOARDING_VERIFY_OTP = "/verify/otp";

    public static final String ENTRANCE_BASE_URL = "/api/v1/users/entrance";
    public static final String ENTRANCE_SIGNIN = "/signin";

    public static final String PROFILE_BASE_URL = "/api/v1/users/profile";
    public static final String PROFILE_INFO = "";
    public static final String PROFILE_UPDATE_INFO = "/update";
    public static final String PROFILE_UPDATE_PASSWORD = "/update-password";
    public static final String PROFILE_SIGNOUT = "/signout";

    public static final String USER_MANAGEMENT_BASE_URL = "/api/v1/users/management";
    public static final String USER_MANAGEMENT_FIND_ALL = "/find-all";
    public static final String USER_MANAGEMENT_FILTER = "/filter";
    public static final String USER_MANAGEMENT_SEARCH = "/search";
    public static final String USER_MANAGEMENT_DELETE = "/delete/{uuid}";


    public static final String EVENT_MANAGEMENT_BASE_URL = "/api/v1/events";
    public static final String CREATE_EVENT = "";
    public static final String UPDATE_EVENT = "";
    public static final String DELETE_EVENT = "/{eventId}";
    public static final String EVENTS_RESERVATIONS = "/all/reservations";
    public static final String EVENTS_FILTER = "/all/reservations/filter";


    public static final String EVENT_RESERVATION_BASE_URL = "/api/v1/events";
    public static final String FIND_ALL_EVENTS = "";
    public static final String FILTER_EVENTS = "/filter";
    public static final String SEARCH_EVENTS_NAME = "/search/name";
    public static final String SEARCH_EVENTS_DATE_RANGE = "/search/date-range";
    public static final String RESERVE_TICKET = "/{eventId}/tickets";
    public static final String VIEW_MY_RESERVATIONS = "/my-reservations";
    public static final String DELETE_MY_RESERVATIONS = "/reservations/{reservationNo}";
}
