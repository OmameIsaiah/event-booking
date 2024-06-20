package com.event.booking.dto.request;

import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

import static com.event.booking.util.AppMessages.*;
import static com.event.booking.util.Utils.DEFAULT_DATE_FORMAT;

@Data
public class EventUpdateRequestDTO implements Serializable {
    @NotEmpty(message = EMPTY_EVENT_ID)
    @NotNull(message = NULL_EVENT_ID)
    private Long eventId;
    @Size(max = 100, message = MAX_EVENT_NAME_LIMIT_EXCEEDED)
    @NotEmpty(message = EMPTY_EVENT_NAME)
    @NotNull(message = NULL_EVENT_NAME)
    private String name;
    @Size(max = 500, message = MAX_EVENT_DESCRIPTION_LIMIT_EXCEEDED)
    private String description;
    @NotNull(message = NULL_EVENT_DATE)
    private String eventDate = DEFAULT_DATE_FORMAT;
    @Max(value = 1000, message = AVAILABLE_ATTENDEES_COUNT_EXCEEDED)
    @Min(value = 0, message = MIN_AVAILABLE_ATTENDEES_COUNT_EXCEEDED)
    @Positive(message = NEGATIVE_AVAILABLE_ATTENDEES_COUNT)
    private Integer availableAttendeesCount;
}
