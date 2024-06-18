package com.event.booking.dto.response;

import com.event.booking.enums.Category;
import lombok.Data;

import java.io.Serializable;

@Data
public class EventResponseDTO implements Serializable {
    private Long id;
    private String name;
    private String description;
    private String eventDate;
    private Integer availableAttendeesCount;
    private Category category;
}
