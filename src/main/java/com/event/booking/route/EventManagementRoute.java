package com.event.booking.route;

import com.event.booking.dto.request.EventReminderRequest;
import com.event.booking.dto.request.EventRequestDTO;
import com.event.booking.dto.request.EventUpdateRequestDTO;
import com.event.booking.dto.response.ApiResponse;
import com.event.booking.enums.Category;
import com.event.booking.service.EventManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.event.booking.util.EndpointsURL.*;

@RestController
@RequestMapping(value = EVENT_MANAGEMENT_BASE_URL, headers = "Accept=application/json")
@Api(tags = "event management route", description = "Endpoints for creating, updating, deleting and viewing all events [Accessible only to ADMIN users with valid authorization]", consumes = "application/json", produces = "application/json", protocols = "https", value = "event management route")
@RequiredArgsConstructor
public class EventManagementRoute {
    private final EventManagementService eventManagementService;

    @PostMapping(value = CREATE_EVENT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for creating new event")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> createEvent(@RequestBody @Valid EventRequestDTO event) {
        return eventManagementService.createEvent(event);
    }

    @PutMapping(value = UPDATE_EVENT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for updating event")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> updateEvent(@RequestBody @Valid EventUpdateRequestDTO eventUpdate) {
        return eventManagementService.updateEvent(eventUpdate);
    }

    @DeleteMapping(value = DELETE_EVENT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for deleting event")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> deleteEvent(@PathVariable("eventId") Long eventId) {
        return eventManagementService.deleteEvent(eventId);
    }

    @GetMapping(value = EVENTS_RESERVATIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for viewing all events reservations")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> viewAllEventsReservations(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(value = "size", defaultValue = "50") Integer size) {
        return eventManagementService.viewAllEventsReservations(page, size);
    }

    @GetMapping(value = EVENTS_FILTER, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for filtering event reservations by category")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> filterAllEventsReservations(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                   @RequestParam(value = "size", defaultValue = "50") Integer size,
                                                                   @RequestParam(value = "category", defaultValue = "CONCERT") Category category) {
        return eventManagementService.filterAllEventsReservations(page, size, category);
    }

    @PostMapping(value = EVENT_REMINDER, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for sending event reminder to all users that reserve a ticket for an event")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> sendEventReminder(@RequestBody @Valid EventReminderRequest eventReminder) {
        return eventManagementService.sendEventReminder(eventReminder);
    }
}
