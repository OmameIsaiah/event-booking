package com.event.booking.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static com.event.booking.util.AppMessages.*;

@Data
public class EventReminderRequest implements Serializable {
    @NotEmpty(message = EMPTY_EVENT_ID)
    @NotNull(message = NULL_EVENT_ID)
    private Long eventId;
}
