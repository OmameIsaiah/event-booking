package com.event.booking.service;

import com.event.booking.dto.request.TicketRequest;
import com.event.booking.dto.response.ApiResponse;
import com.event.booking.enums.Category;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface EventReservationService {
    ResponseEntity<ApiResponse> getAllEvents(Integer page, Integer size);

    ResponseEntity<ApiResponse> filterEvents(Integer page, Integer size, Category category);

    ResponseEntity<ApiResponse> searchEventByName(String name);

    ResponseEntity<ApiResponse> searchEventByDateRange(Integer page, Integer size, String startDate, String endDate);

    ResponseEntity<ApiResponse> reserveTicketForEvent(HttpServletRequest httpServletRequest, Long eventId, TicketRequest ticketRequest);

    ResponseEntity<ApiResponse> viewMyReservations(HttpServletRequest httpServletRequest, Integer page, Integer size);

    ResponseEntity<ApiResponse> deleteMyEventReservation(HttpServletRequest httpServletRequest, String reservationNo);
}
