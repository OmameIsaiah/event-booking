
package com.event.booking.notification.kafka;

import com.event.booking.dto.request.OTPNotificationRequest;
import com.event.booking.dto.request.QueueRequest;
import com.event.booking.dto.response.ReservationResponseDTO;
import com.event.booking.exceptions.BadRequestException;
import com.event.booking.notification.EmailNotificationService;
import com.event.booking.util.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.event.booking.util.Utils.*;


@Component
@Slf4j
@RequiredArgsConstructor
public class MessageConsumer {
    private final ObjectMapper objectMapper;
    private final EmailNotificationService notificationService;

    @KafkaListener(topics = "" + TOPIC_NOTIFICATION + "", groupId = "event-booking-group-id")
    public void messageConsumer(String message) {
        log.info("######################### Event Received: {}", message);
        if (Objects.nonNull(message)) {
            try {
                QueueRequest request = Utils.getGson().fromJson(message, QueueRequest.class);
                switch (request.getType()) {
                    case EMAIL_EVENT_REMINDER -> {
                        notificationService.sendEventReminder(Utils.getGson().fromJson(objectMapper.writeValueAsString(request.getData()),
                                ReservationResponseDTO.class));
                    }
                    case EMAIL_TICKET_RESERVATION -> {
                        notificationService.sendTicketReservationEmail(Utils.getGson().fromJson(objectMapper.writeValueAsString(request.getData()),
                                ReservationResponseDTO.class));
                    }
                    case EMAIL_SIGNUP_OTP -> {
                        notificationService.sendOTPNotification(Utils.getGson().fromJson(objectMapper.writeValueAsString(request.getData()),
                                OTPNotificationRequest.class));
                    }
                    default -> {
                    }
                }
            } catch (Exception e) {
                throw new BadRequestException("Error converting data");
            }
        }
    }
}