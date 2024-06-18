package com.event.booking.service.impl;

import com.event.booking.dto.request.TicketRequest;
import com.event.booking.dto.response.ApiResponse;
import com.event.booking.enums.Category;
import com.event.booking.service.EventReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class EventReservationServiceImpl implements EventReservationService {
    @Override
    public ResponseEntity<ApiResponse> getAllEvents(Integer page, Integer size) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> filterEvents(Integer page, Integer size, Category category) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> searchEventByName(String name) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> searchEventByDateRange(Integer page, Integer size, String startDate, String endDate) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> reserveTicketForEvent(HttpServletRequest httpServletRequest, Long eventId, TicketRequest ticketRequest) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> viewMyReservations(HttpServletRequest httpServletRequest, Integer page, Integer size) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> deleteEventReservation(HttpServletRequest httpServletRequest, Long eventId) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> updateEventReservation(HttpServletRequest httpServletRequest, Long eventId, TicketRequest ticketRequest) {
        return null;
    }
}
