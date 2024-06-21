package com.event.booking.utils;

import com.event.booking.dto.request.EventRequestDTO;
import com.event.booking.dto.request.EventUpdateRequestDTO;
import com.event.booking.dto.response.ApiResponse;
import com.event.booking.enums.Category;
import com.event.booking.model.Event;
import com.event.booking.util.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.event.booking.util.AppMessages.EVENT_CREATED_SUCCESSFULLY;
import static com.event.booking.util.AppMessages.EVENT_UPDATED_SUCCESSFULLY;

public class EventManagementServiceTestData {
    public static String VALID_EVENT_NAME = "NBA Finals, 2024";
    public static String VALID_EVENT_DESCRIPTION = "Finals of the NBA Finals, 2024, between Boston Celtics and Dallas Mavericks.";
    public static String VALID_EVENT_DATE = "2024-06-30 04:30:00";
    public static Integer VALID_EVENT_AVAILABLE_ATTENDEES_COUNT = 1000;
    public static Category VALID_EVENT_CATEGORY = Category.GAME;
    public static Event VALID_EVENT = new Event();

    static {
        VALID_EVENT.setId(1L);
        VALID_EVENT.setUuid(UUID.randomUUID().toString());
        VALID_EVENT.setDateCreated(LocalDateTime.now());
        VALID_EVENT.setLastModified(LocalDateTime.now());
        VALID_EVENT.setName(VALID_EVENT_NAME);
        VALID_EVENT.setDescription(VALID_EVENT_DESCRIPTION);
        VALID_EVENT.setEventDate(LocalDateTime.now().plusMonths(1));
        VALID_EVENT.setAvailableAttendeesCount(VALID_EVENT_AVAILABLE_ATTENDEES_COUNT);
        VALID_EVENT.setCategory(VALID_EVENT_CATEGORY);
    }

    public static EventRequestDTO VALID_EVENT_REQUEST = new EventRequestDTO();

    static {
        VALID_EVENT_REQUEST.setName(VALID_EVENT_NAME);
        VALID_EVENT_REQUEST.setDescription(VALID_EVENT_DESCRIPTION);
        VALID_EVENT_REQUEST.setEventDate(VALID_EVENT_DATE);
        VALID_EVENT_REQUEST.setAvailableAttendeesCount(VALID_EVENT_AVAILABLE_ATTENDEES_COUNT);
        VALID_EVENT_REQUEST.setCategory(VALID_EVENT_CATEGORY);
    }

    public static EventUpdateRequestDTO VALID_EVENT_UPDATE_REQUEST = new EventUpdateRequestDTO();

    static {
        VALID_EVENT_UPDATE_REQUEST.setEventId(1L);
        VALID_EVENT_UPDATE_REQUEST.setName(VALID_EVENT_NAME);
        VALID_EVENT_UPDATE_REQUEST.setDescription(VALID_EVENT_DESCRIPTION);
        VALID_EVENT_UPDATE_REQUEST.setEventDate(VALID_EVENT_DATE);
        VALID_EVENT_UPDATE_REQUEST.setAvailableAttendeesCount(VALID_EVENT_AVAILABLE_ATTENDEES_COUNT);
    }
    public static ResponseEntity VALID_EVENT_SUCCESS_RESPONSE;

    static {
        VALID_EVENT_SUCCESS_RESPONSE = ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED,
                        EVENT_CREATED_SUCCESSFULLY,
                        Mapper.mapEventToEventResponseDTO(VALID_EVENT)
                ));

    }

    public static ResponseEntity VALID_EVENT_UPDATE_SUCCESS_RESPONSE;

    static {
        VALID_EVENT_UPDATE_SUCCESS_RESPONSE = ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        EVENT_UPDATED_SUCCESSFULLY,
                        Mapper.mapEventToEventResponseDTO(VALID_EVENT)
                ));

    }
}
