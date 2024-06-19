package com.event.booking.service;

import com.event.booking.dto.request.EventReminderRequest;
import com.event.booking.dto.request.EventRequestDTO;
import com.event.booking.dto.request.EventUpdateRequestDTO;
import com.event.booking.dto.response.ApiResponse;
import com.event.booking.enums.Category;
import org.springframework.http.ResponseEntity;

public interface EventManagementService {
    ResponseEntity<ApiResponse> createEvent(EventRequestDTO event);

    ResponseEntity<ApiResponse> updateEvent(EventUpdateRequestDTO eventUpdate);

    ResponseEntity<ApiResponse> deleteEvent(Long eventId);

    ResponseEntity<ApiResponse> viewAllEventsReservations(Integer page, Integer size);

    ResponseEntity<ApiResponse> filterAllEventsReservations(Integer page, Integer size, Category category);

    ResponseEntity<ApiResponse> sendEventReminder(EventReminderRequest eventReminder);
}
