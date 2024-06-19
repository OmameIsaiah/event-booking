package com.event.booking.util;

import com.event.booking.dto.request.TicketRequest;
import com.event.booking.dto.response.ReservationResponseDTO;
import com.event.booking.enums.UserType;
import com.event.booking.exceptions.BadRequestException;
import com.event.booking.exceptions.RecordNotFoundException;
import com.event.booking.model.Event;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.data.domain.Page;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import static com.event.booking.util.AppMessages.*;

public class Utils {
    public static String OTP_EMAIL_SUBJECT = "Email Verification";
    public static String OTP_EMAIL_TEMPLATE = "OTPNotification.ftl";
    public static String RESERVATION_EMAIL_SUBJECT = "Event Reservation Ticket";
    public static String RESERVATION_EMAIL_TEMPLATE = "TicketReservation.ftl";
    public static String EVENT_REMINDER_EMAIL_SUBJECT = "Event Reminder!!!";
    public static String EVENT_REMINDER_EMAIL_TEMPLATE = "EventReminder.ftl";
    public static String EMAIL_TEMPLATES_DIR = "/templates/notification";
    private static String OTP_EXPIRE_TIME = "600";
    public static String DATE_CREATED = "date_created";
    public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE = "2024-06-30 10:30:00";
    public static final String DEFAULT_START_DATE = "2024-06-15 10:30:00";
    public static final String DEFAULT_END_DATE = "2024-10-31 11:59:59";
    static EmailValidator validator = EmailValidator.getInstance();

    public static Boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);

        if (!pat.matcher(email).matches()) {
            return false;
        } else {
            return validator.isValid(email);
        }
    }

    public static Boolean isValidUserType(UserType userType) {
        if (Objects.isNull(userType)) {
            return false;
        }
        for (UserType myEnum : UserType.values()) {
            if (myEnum.label.equals(userType.label)) {
                return true;
            }
        }
        return false;
    }

    public static String generate4DigitOTPAndExpireTime() {
        int min = 9999;
        int max = 1000;
        int randomValue = (int) (Math.random() * (max - min + 1) + min);
        String otp = String.valueOf(randomValue);
        long baseTime = (System.currentTimeMillis() / 1000) + Integer.parseInt(OTP_EXPIRE_TIME);
        String otpLimitTime = "" + baseTime + "";
        return otp + "_" + otpLimitTime;
    }

    public static String convertLocalDateTimeToString(LocalDateTime dateTime) {
        if (Objects.isNull(dateTime)) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                .format(java.util.Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    public static LocalDateTime convertDateStringToLocalDateTime(String date) {
        if (Objects.isNull(date)) {
            return null;
        }
        try {
            if (date.length() <= 10) {
                date += " 00:00:00";
            }
            return LocalDateTime
                    .parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .atZone(ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of("UTC+1"))
                    .toLocalDateTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void checkIfEventsAreFound(Page<Event> events) {
        if (events.isEmpty() || Objects.isNull(events)) {
            throw new RecordNotFoundException(NO_EVENT_FOUND);
        }
    }

    public static void checkIfEventsAreFound(List<Event> events) {
        if (events.isEmpty() || Objects.isNull(events)) {
            throw new RecordNotFoundException(NO_EVENT_FOUND);
        }
    }

    public static Event validateReservationEvent(TicketRequest ticketRequest, Optional<Event> optional) {
        Event event = optional.get();
        if (event.getAvailableAttendeesCount() <= 0) {
            throw new BadRequestException(NO_SPACE_AVAILABLE);
        }
        if (event.getAvailableAttendeesCount() < ticketRequest.getAttendeesCount()) {
            throw new BadRequestException(ATTENDEES_COUNT_MORE_THAN_AVAILABLE_SPACE);
        }
        return event;
    }

    public static void validateEventUsersAndDate(Event event) {
        if (Objects.isNull(event.getUserEvents())) {
            throw new BadRequestException(NO_RECORD_OF_USERS_FOR_THE_EVENT);
        }
        if (event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException(EVENT_DATE_PASSED);
        }
    }

    public static Map mapReservationAndEventReminderModel(ReservationResponseDTO request) {
        Map model = new HashMap();
        model.put("email", request.getEmail());
        model.put("name", request.getName());
        model.put("reservationNo", request.getReservationNo());
        model.put("eventName", request.getEventName());
        model.put("eventId", request.getEventId());
        model.put("eventDescription", request.getEventDescription());
        model.put("eventDate", request.getEventDate());
        model.put("attendeesCount", request.getAttendeesCount());
        model.put("eventCategory", request.getEventCategory());
        return model;
    }
}
