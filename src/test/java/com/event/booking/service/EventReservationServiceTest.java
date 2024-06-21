package com.event.booking.service;

import com.event.booking.dto.response.ApiResponse;
import com.event.booking.exceptions.BadRequestException;
import com.event.booking.exceptions.UnauthorizedException;
import com.event.booking.notification.kafka.MessageProducer;
import com.event.booking.repository.EventRepository;
import com.event.booking.repository.UserEventRepository;
import com.event.booking.repository.UserRepository;
import com.event.booking.security.jwt.JwtUtils;
import com.event.booking.service.impl.EventReservationServiceImpl;
import com.event.booking.util.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static com.event.booking.util.AppMessages.*;
import static com.event.booking.utils.EventTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventReservationServiceTest {
    @InjectMocks
    private EventReservationServiceImpl eventReservationService;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserEventRepository userEventRepository;
    @Mock
    private MessageProducer messageProducer;
    @Mock
    private JwtUtils jwtUtils;

    @BeforeEach
    public void setUp() {
        eventReservationService = spy(
                new EventReservationServiceImpl(
                        eventRepository,
                        userRepository,
                        userEventRepository,
                        messageProducer,
                        jwtUtils));
    }

    @Test
    public void test_reserveTicketForEvent_return_success() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(VALID_EVENT));
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(VALID_USER));
        when(jwtUtils.getUsernameFromHttpServletRequest(any())).thenReturn(VALID_EMAIL);
        try (MockedStatic<Utils> utilities = Mockito.mockStatic(Utils.class)) {
            utilities.when(() -> Utils.validateReservationEvent(any(), any())).thenReturn(VALID_EVENT);
        }
        when(eventRepository.save(any())).thenReturn(VALID_EVENT);
        when(userEventRepository.save(any())).thenReturn(VALID_USER_EVENT);

        ResponseEntity<ApiResponse> response = eventReservationService.
                reserveTicketForEvent(VALID_HTTP_SERVLET_REQUEST, VALID_EVENT_ID, VALID_TICKET_REQUEST);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(response.getBody().getSuccess(), true);
        assertEquals(response.getBody().getMessage(), EVENT_RESERVED_SUCCESSFULLY);
        assertEquals(response.getBody().getCode(), HttpStatus.CREATED.value());
        assertEquals(response.getBody().getStatus(), HttpStatus.CREATED);
        assertNotNull(eventReservationService.
                reserveTicketForEvent(VALID_HTTP_SERVLET_REQUEST, VALID_EVENT_ID, VALID_TICKET_REQUEST));
        verify(eventReservationService, times(2)).
                reserveTicketForEvent(VALID_HTTP_SERVLET_REQUEST, VALID_EVENT_ID, VALID_TICKET_REQUEST);
    }

    @Test
    public void test_reserveTicketForEvent_throw_null_event_id_request_parameters() {
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> eventReservationService.
                        reserveTicketForEvent(VALID_HTTP_SERVLET_REQUEST, null, VALID_TICKET_REQUEST));
        assertEquals(NULL_EVENT_ID, exception.getMessage());
    }

    @Test
    public void test_reserveTicketForEvent_throw_null_ticket_request_parameters() {
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> eventReservationService.
                        reserveTicketForEvent(VALID_HTTP_SERVLET_REQUEST, VALID_EVENT_ID, null));
        assertEquals(INVALID_TICKET_REQUEST_PARAMETERS, exception.getMessage());
    }

    @Test
    public void test_reserveTicketForEvent_throw_unauthorized_user_token_1() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(VALID_EVENT));
        UnauthorizedException exception = assertThrows(UnauthorizedException.class,
                () -> eventReservationService.
                        reserveTicketForEvent(null, VALID_EVENT_ID, VALID_TICKET_REQUEST));
        assertEquals(INVALID_AUTHORIZATION_TOKEN, exception.getMessage());
    }

    @Test
    public void test_reserveTicketForEvent_throw_unauthorized_user_token_2() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(VALID_EVENT));
        try (MockedStatic<Utils> utilities = Mockito.mockStatic(Utils.class)) {
            utilities.when(() -> Utils.validateReservationEvent(any(), any())).thenReturn(null);
        }
        UnauthorizedException exception = assertThrows(UnauthorizedException.class,
                () -> eventReservationService.
                        reserveTicketForEvent(VALID_HTTP_SERVLET_REQUEST, VALID_EVENT_ID, VALID_TICKET_REQUEST));
        assertEquals(INVALID_AUTHORIZATION_TOKEN, exception.getMessage());
    }

    @Test
    public void test_reserveTicketForEvent_throw_record_not_found() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(VALID_EVENT));
        when(jwtUtils.getUsernameFromHttpServletRequest(any())).thenReturn(VALID_EMAIL);
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> eventReservationService.
                        reserveTicketForEvent(VALID_HTTP_SERVLET_REQUEST, VALID_EVENT_ID, VALID_TICKET_REQUEST));
        assertEquals(WRONG_ACCOUNT_EMAIL, exception.getMessage());
    }

}
