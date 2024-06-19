package com.event.booking.service.impl;

import com.event.booking.dto.request.EventRequestDTO;
import com.event.booking.dto.request.EventUpdateRequestDTO;
import com.event.booking.dto.response.ApiResponse;
import com.event.booking.enums.Category;
import com.event.booking.exceptions.BadRequestException;
import com.event.booking.exceptions.DuplicateRecordException;
import com.event.booking.exceptions.RecordNotFoundException;
import com.event.booking.model.Event;
import com.event.booking.model.UserEvent;
import com.event.booking.repository.EventRepository;
import com.event.booking.repository.UserEventRepository;
import com.event.booking.service.EventManagementService;
import com.event.booking.util.Mapper;
import com.event.booking.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.event.booking.util.AppMessages.*;

@Service
@RequiredArgsConstructor
public class EventManagementServiceImpl implements EventManagementService {
    private final EventRepository eventRepository;
    private final UserEventRepository userEventRepository;

    @Override
    public ResponseEntity<ApiResponse> createEvent(EventRequestDTO event) {
        validateEventRequest(event);
        Event mappedEvent = Mapper.mapEventRequestDTOToEvent(event);
        mappedEvent.setUuid(UUID.randomUUID().toString());
        Event savedEvent = eventRepository.save(mappedEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED,
                        EVENT_CREATED_SUCCESSFULLY,
                        Mapper.mapEventToEventResponseDTO(savedEvent)
                ));
    }

    private void validateEventRequest(EventRequestDTO event) {
        if (Objects.isNull(event)) {
            throw new BadRequestException(INVALID_REQUEST_PARAMETERS);
        }
        Optional<Event> optional = eventRepository.findEventByName(event.getName());
        if (optional.isPresent()) {
            throw new DuplicateRecordException(EVENT_ALREADY_TAKEN);
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateEvent(EventUpdateRequestDTO eventUpdate) {
        Optional<Event> optional = validateEventUpdateRequest(eventUpdate);
        Event savedEvent = mapAndSaveUpdatedEvent(eventUpdate, optional);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        EVENT_UPDATED_SUCCESSFULLY,
                        Mapper.mapEventToEventResponseDTO(savedEvent)
                ));
    }

    private Event mapAndSaveUpdatedEvent(EventUpdateRequestDTO eventUpdate, Optional<Event> optional) {
        Event updatedEvent = optional.get();
        updatedEvent.setName(eventUpdate.getName());
        updatedEvent.setDescription(eventUpdate.getDescription());
        updatedEvent.setEventDate(Utils.convertDateStringToLocalDateTime(eventUpdate.getEventDate()));
        updatedEvent.setAvailableAttendeesCount(eventUpdate.getAvailableAttendeesCount());
        return eventRepository.save(updatedEvent);
    }

    private Optional<Event> validateEventUpdateRequest(EventUpdateRequestDTO eventUpdate) {
        if (Objects.isNull(eventUpdate)) {
            throw new BadRequestException(INVALID_REQUEST_PARAMETERS);
        }
        Optional<Event> optional = eventRepository.findById(eventUpdate.getEventId());
        if (optional.isEmpty()) {
            throw new BadRequestException(NO_EVENT_FOUND_WITH_ID);
        }
        return optional;
    }

    @Override
    public ResponseEntity<ApiResponse> deleteEvent(Long eventId) {
        validateDeleteEventId(eventId);
        eventRepository.deleteById(eventId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        EVENT_DELETED_SUCCESSFULLY
                ));
    }

    private void validateDeleteEventId(Long eventId) {
        if (Objects.isNull(eventId)) {
            throw new BadRequestException(NULL_EVENT_ID);
        }
        Optional<Event> optional = eventRepository.findById(eventId);
        if (optional.isEmpty()) {
            throw new BadRequestException(NO_EVENT_FOUND_WITH_ID);
        }
    }

    @Override
    public ResponseEntity<ApiResponse> viewAllEventsReservations(Integer page, Integer size) {
        Page<UserEvent> list = userEventRepository.findAll(PageRequest.of((Objects.isNull(page) ? 0 : page), (Objects.isNull(size) ? 50 : size)));
        if (list.isEmpty() || Objects.isNull(list)) {
            throw new RecordNotFoundException(NO_EVENT_RESERVATIONS_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        USER_RESERVATION_RETRIEVED_SUCCESSFULLY,
                        list.stream()
                                .map(Mapper::mapUserEventToReservationResponseDTO)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                ));
    }

    @Override
    public ResponseEntity<ApiResponse> filterAllEventsReservations(Integer page, Integer size, Category category) {
        Page<UserEvent> list = userEventRepository.findUserEventByCategory(
                category,
                PageRequest.of((Objects.isNull(page) ? 0 : page), (Objects.isNull(size) ? 50 : size)));
        if (list.isEmpty() || Objects.isNull(list)) {
            throw new RecordNotFoundException(NO_EVENT_RESERVATIONS_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        USER_RESERVATION_RETRIEVED_SUCCESSFULLY,
                        list.stream()
                                .map(Mapper::mapUserEventToReservationResponseDTO)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                ));
    }
}
