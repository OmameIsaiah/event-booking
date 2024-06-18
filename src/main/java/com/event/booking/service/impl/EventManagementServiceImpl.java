package com.event.booking.service.impl;

import com.event.booking.dto.request.EventRequestDTO;
import com.event.booking.dto.request.EventUpdateRequestDTO;
import com.event.booking.dto.response.ApiResponse;
import com.event.booking.enums.Category;
import com.event.booking.service.EventManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EventManagementServiceImpl implements EventManagementService {

    @Override
    public ResponseEntity<ApiResponse> createEvent(EventRequestDTO event) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> updateEvent(EventUpdateRequestDTO eventUpdate) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> deleteEvent(String eventId) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> viewAllEventsReservations(Integer page, Integer size) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> filterAllEventsReservations(Integer page, Integer size, Category category) {
        return null;
    }
}
