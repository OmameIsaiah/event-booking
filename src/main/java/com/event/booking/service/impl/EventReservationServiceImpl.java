package com.event.booking.service.impl;

import com.event.booking.dto.request.OTPNotificationRequest;
import com.event.booking.dto.request.TicketRequest;
import com.event.booking.dto.response.ApiResponse;
import com.event.booking.dto.response.ReservationResponseDTO;
import com.event.booking.enums.Category;
import com.event.booking.exceptions.BadRequestException;
import com.event.booking.exceptions.RecordNotFoundException;
import com.event.booking.exceptions.UnauthorizedException;
import com.event.booking.model.Event;
import com.event.booking.model.User;
import com.event.booking.model.UserEvent;
import com.event.booking.notification.EmailNotificationService;
import com.event.booking.notification.kafka.MessageProducer;
import com.event.booking.repository.EventRepository;
import com.event.booking.repository.UserEventRepository;
import com.event.booking.repository.UserRepository;
import com.event.booking.security.jwt.JwtUtils;
import com.event.booking.service.EventReservationService;
import com.event.booking.util.Mapper;
import com.event.booking.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.event.booking.util.AppMessages.*;
import static com.event.booking.util.Utils.EMAIL_SIGNUP_OTP;
import static com.event.booking.util.Utils.EMAIL_TICKET_RESERVATION;

@Service
@RequiredArgsConstructor
public class EventReservationServiceImpl implements EventReservationService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final UserEventRepository userEventRepository;
    //private final EmailNotificationService notificationService;
    private final MessageProducer messageProducer;
    private final JwtUtils jwtUtils;

    private User validateUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException(WRONG_ACCOUNT_EMAIL));
    }

    private String validateAuthorizedUser(HttpServletRequest httpServletRequest) {
        if (Objects.isNull(httpServletRequest)) {
            throw new UnauthorizedException(INVALID_AUTHORIZATION_TOKEN);
        }
        String username = jwtUtils.getUsernameFromHttpServletRequest(httpServletRequest);
        if (Objects.isNull(username)) {
            throw new UnauthorizedException(INVALID_AUTHORIZATION_TOKEN);
        }
        return username;
    }


    @Override
    public ResponseEntity<ApiResponse> getAllEvents(Integer page, Integer size) {
        Page<Event> events = eventRepository.findAll(PageRequest.of((Objects.isNull(page) ? 0 : page), (Objects.isNull(size) ? 50 : size)));
        Utils.checkIfEventsAreFound(events);
        return Mapper.processEventPageResponse(events);
    }

    @Override
    public ResponseEntity<ApiResponse> filterEvents(Integer page, Integer size, Category category) {
        Page<Event> list = eventRepository.findEventByCategory(
                category,
                PageRequest.of((Objects.isNull(page) ? 0 : page), (Objects.isNull(size) ? 50 : size)));
        Utils.checkIfEventsAreFound(list);
        return Mapper.processEventPageResponse(list);
    }

    @Override
    public ResponseEntity<ApiResponse> searchEventByName(String name) {
        if (Objects.isNull(name)) {
            throw new BadRequestException(NULL_EVENT_NAME);
        }
        List<Event> list = eventRepository.searchEventByName(name);
        Utils.checkIfEventsAreFound(list);
        return Mapper.processEventListResponse(list);
    }

    @Override
    public ResponseEntity<ApiResponse> searchEventByDateRange(Integer page, Integer size, String startDate, String endDate) {
        Page<Event> list = eventRepository.searchEventByDateRange(
                Utils.convertDateStringToLocalDateTime(startDate),
                Utils.convertDateStringToLocalDateTime(endDate),
                PageRequest.of((Objects.isNull(page) ? 0 : page), (Objects.isNull(size) ? 50 : size)));
        Utils.checkIfEventsAreFound(list);
        return Mapper.processEventPageResponse(list);
    }

    @Override
    public ResponseEntity<ApiResponse> reserveTicketForEvent(HttpServletRequest httpServletRequest, Long eventId, TicketRequest ticketRequest) {
        Optional<Event> optional = validateTicketReservationParam(eventId, ticketRequest);
        User user = validateUserByEmail(validateAuthorizedUser(httpServletRequest));
        Event event = Utils.validateReservationEvent(ticketRequest, optional);
        event.setAvailableAttendeesCount(event.getAvailableAttendeesCount() - ticketRequest.getAttendeesCount());
        event = eventRepository.save(event);
        UserEvent userEvent = buildNewUserEventModel(ticketRequest, user, event);
        ReservationResponseDTO reservationResponseDTO = Mapper.mapUserEventToReservationResponseDTO(userEvent);
        messageProducer.sendMessage(EMAIL_TICKET_RESERVATION, reservationResponseDTO);
        //notificationService.sendTicketReservationEmail(reservationResponseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED,
                        EVENT_RESERVED_SUCCESSFULLY,
                        reservationResponseDTO
                ));
    }

    private UserEvent buildNewUserEventModel(TicketRequest ticketRequest, User user, Event event) {
        UserEvent userEvent = new UserEvent();
        userEvent.setUuid(UUID.randomUUID().toString());
        userEvent.setAttendeesCount(ticketRequest.getAttendeesCount());
        userEvent.setEventid(event);
        userEvent.setUserevent(user);
        userEvent = userEventRepository.save(userEvent);
        return userEvent;
    }


    private Optional<Event> validateTicketReservationParam(Long eventId, TicketRequest ticketRequest) {
        if (Objects.isNull(eventId)) {
            throw new BadRequestException(NULL_EVENT_ID);
        }
        if (Objects.isNull(ticketRequest)) {
            throw new BadRequestException(INVALID_TICKET_REQUEST_PARAMETERS);
        }
        Optional<Event> optional = eventRepository.findById(eventId);
        if (optional.isEmpty()) {
            throw new BadRequestException(NO_EVENT_FOUND_WITH_ID);
        }
        return optional;
    }

    @Override
    public ResponseEntity<ApiResponse> viewMyReservations(HttpServletRequest httpServletRequest, Integer page, Integer size) {
        User user = validateUserByEmail(validateAuthorizedUser(httpServletRequest));
        Page<UserEvent> list = userEventRepository.findUserEventByUserId(user.getId(), PageRequest.of((Objects.isNull(page) ? 0 : page), (Objects.isNull(size) ? 50 : size)));
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
    public ResponseEntity<ApiResponse> deleteMyEventReservation(HttpServletRequest httpServletRequest, String reservationNo) {
        if (Objects.isNull(reservationNo)) {
            throw new BadRequestException(NULL_RESERVATION_NO);
        }
        Optional<UserEvent> optional = userEventRepository.findById(Long.valueOf(reservationNo));
        if (optional.isEmpty()) {
            throw new BadRequestException(NO_RESERVATION_FOUND_WITH_NO);
        }
        validateUserByEmail(validateAuthorizedUser(httpServletRequest));
        UserEvent reservation = optional.get();
        Event reservedEvent = reservation.getEventid();
        reservedEvent.setAvailableAttendeesCount(reservedEvent.getAvailableAttendeesCount() + reservation.getAttendeesCount());
        eventRepository.save(reservedEvent);
        userEventRepository.deleteById(reservation.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        USER_RESERVATION_DELETED_SUCCESSFULLY
                ));
    }
}
