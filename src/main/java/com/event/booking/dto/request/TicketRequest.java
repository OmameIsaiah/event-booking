package com.event.booking.dto.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.io.Serializable;

import static com.event.booking.util.AppMessages.MIN_ATTENDEES_COUNT_EXCEEDED;
import static com.event.booking.util.AppMessages.NEGATIVE_ATTENDEES_COUNT;

@Data
public class TicketRequest implements Serializable {
    @Min(value = 1, message = MIN_ATTENDEES_COUNT_EXCEEDED)
    @Positive(message = NEGATIVE_ATTENDEES_COUNT)
    private Integer attendeesCount;
}
