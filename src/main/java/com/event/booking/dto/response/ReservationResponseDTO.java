package com.event.booking.dto.response;

import com.event.booking.enums.Category;
import com.event.booking.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDTO implements Serializable {
    private String reservationNo;
    private String name;
    private String email;
    private UserType userType;
    private String eventId;
    private String eventName;
    private String eventDescription;
    private String eventDate;
    private Integer attendeesCount;
    private Category eventCategory;
}
