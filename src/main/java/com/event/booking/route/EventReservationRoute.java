package com.event.booking.route;

import com.event.booking.dto.request.TicketRequest;
import com.event.booking.dto.response.ApiResponse;
import com.event.booking.enums.Category;
import com.event.booking.service.EventReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.event.booking.util.EndpointsURL.*;
import static com.event.booking.util.Utils.DEFAULT_END_DATE;
import static com.event.booking.util.Utils.DEFAULT_START_DATE;

@RestController
@RequestMapping(value = EVENT_RESERVATION_BASE_URL, headers = "Accept=application/json")
@Api(tags = "event reservation route", description = "Endpoints for creating event ticket, viewing and deleting reservations, and fetching events [Accessible to ALL users with valid authorization]", consumes = "application/json", produces = "application/json", protocols = "https", value = "event reservation route")
@RequiredArgsConstructor
public class EventReservationRoute {
    private final EventReservationService eventReservationService;

    @GetMapping(value = FIND_ALL_EVENTS, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for fetching all events")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> getAllEvents(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(value = "size", defaultValue = "50") Integer size) {
        return eventReservationService.getAllEvents(page, size);
    }

    @GetMapping(value = FILTER_EVENTS, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for filtering events by category")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> filterEvents(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(value = "size", defaultValue = "50") Integer size,
                                                    @RequestParam(value = "category", defaultValue = "CONCERT") Category category) {
        return eventReservationService.filterEvents(page, size, category);
    }

    @GetMapping(value = SEARCH_EVENTS_NAME, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for searching events by name")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> searchEventByName(@RequestParam("name") String name) {
        return eventReservationService.searchEventByName(name);
    }

    @GetMapping(value = SEARCH_EVENTS_DATE_RANGE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for fetching all events")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> searchEventByDateRange(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                              @RequestParam(value = "size", defaultValue = "50") Integer size,
                                                              @RequestParam(value = "startDate", defaultValue = DEFAULT_START_DATE) String startDate,
                                                              @RequestParam(value = "endDate", defaultValue = DEFAULT_END_DATE) String endDate) {
        return eventReservationService.searchEventByDateRange(page, size, startDate, endDate);
    }

    @PostMapping(value = RESERVE_TICKET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for reserving a ticket for an event")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> reserveTicketForEvent(@PathVariable("eventId") Long eventId,
                                                             @RequestBody @Valid TicketRequest ticketRequest,
                                                             HttpServletRequest httpServletRequest) {
        return eventReservationService.reserveTicketForEvent(httpServletRequest, eventId, ticketRequest);
    }

    @GetMapping(value = VIEW_MY_RESERVATIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for viewing all reservations for a user")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> viewMyReservations(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "50") Integer size,
                                                          HttpServletRequest httpServletRequest) {
        return eventReservationService.viewMyReservations(httpServletRequest, page, size);
    }

    @DeleteMapping(value = DELETE_MY_RESERVATIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for deleting a reservation")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> deleteMyEventReservation(@PathVariable("reservationNo") String reservationNo,
                                                                HttpServletRequest httpServletRequest) {
        return eventReservationService.deleteMyEventReservation(httpServletRequest, reservationNo);
    }
}
