package com.event.booking.service;

import com.event.booking.dto.response.ApiResponse;
import com.event.booking.exceptions.BadRequestException;
import com.event.booking.exceptions.DuplicateRecordException;
import com.event.booking.exceptions.RecordNotFoundException;
import com.event.booking.notification.kafka.MessageProducer;
import com.event.booking.repository.EventRepository;
import com.event.booking.repository.UserEventRepository;
import com.event.booking.service.impl.EventManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static com.event.booking.util.AppMessages.*;
import static com.event.booking.utils.EventManagementServiceTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventManagementServiceTest {
    @InjectMocks
    private EventManagementServiceImpl eventManagementServiceImpl;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserEventRepository userEventRepository;
    @Mock
    private MessageProducer messageProducer;

    @BeforeEach
    public void setUp() {
        eventManagementServiceImpl = spy(
                new EventManagementServiceImpl(eventRepository, userEventRepository, messageProducer));
    }

    @Test
    public void test_createEvent_return_success() {
        when(eventRepository.findEventByName(anyString())).thenReturn(Optional.empty());
        when(eventRepository.save(any())).thenReturn(VALID_EVENT);

        ResponseEntity<ApiResponse> response = eventManagementServiceImpl.createEvent(VALID_EVENT_REQUEST);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(response, VALID_EVENT_SUCCESS_RESPONSE);
        assertNotNull(eventManagementServiceImpl.createEvent(VALID_EVENT_REQUEST));
        verify(eventManagementServiceImpl, times(2)).createEvent(VALID_EVENT_REQUEST);
    }

    @Test
    public void test_createEvent_throw_null_request_parameters() {
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> eventManagementServiceImpl.createEvent(null));
        assertEquals(INVALID_REQUEST_PARAMETERS, exception.getMessage());
    }

    @Test
    public void test_createEvent_throw_event_name_already_exists() {
        when(eventRepository.findEventByName(anyString())).thenReturn(Optional.of(VALID_EVENT));
        DuplicateRecordException exception = assertThrows(DuplicateRecordException.class,
                () -> eventManagementServiceImpl.createEvent(VALID_EVENT_REQUEST));
        assertEquals(EVENT_ALREADY_TAKEN, exception.getMessage());
    }

    @Test
    public void test_updateEvent_return_success() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(VALID_EVENT));
        when(eventRepository.save(any())).thenReturn(VALID_EVENT);

        ResponseEntity<ApiResponse> response = eventManagementServiceImpl.updateEvent(VALID_EVENT_UPDATE_REQUEST);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().getSuccess(), true);
        assertEquals(response.getBody().getCode(), HttpStatus.OK.value());
        assertEquals(response.getBody().getStatus(), HttpStatus.OK);
        assertEquals(response.getBody().getMessage(), EVENT_UPDATED_SUCCESSFULLY);
        assertNotNull(eventManagementServiceImpl.updateEvent(VALID_EVENT_UPDATE_REQUEST));
        verify(eventManagementServiceImpl, times(2)).updateEvent(VALID_EVENT_UPDATE_REQUEST);
    }

    @Test
    public void test_updateEvent_throw_null_request_parameters() {
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> eventManagementServiceImpl.updateEvent(null));
        assertEquals(INVALID_REQUEST_PARAMETERS, exception.getMessage());
    }

    @Test
    public void test_updateEvent_throw_event_id_not_found() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class,
                () -> eventManagementServiceImpl.updateEvent(VALID_EVENT_UPDATE_REQUEST));
        assertEquals(NO_EVENT_FOUND_WITH_ID, exception.getMessage());
    }
}
