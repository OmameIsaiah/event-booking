package com.event.booking.notification;

import com.event.booking.dto.request.OTPNotificationRequest;
import com.event.booking.dto.response.ReservationResponseDTO;

public interface EmailNotificationService {
    void sendOTPNotification(OTPNotificationRequest request);

    void sendTicketReservationEmail(ReservationResponseDTO request);
}
