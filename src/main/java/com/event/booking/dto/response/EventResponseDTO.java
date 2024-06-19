package com.event.booking.dto.response;

import com.event.booking.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseDTO implements Serializable {
    private Long id;
    private String name;
    private String description;
    private String eventDate;
    private Integer availableAttendeesCount;
    private Category category;
}
