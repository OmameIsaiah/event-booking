package com.event.booking.notification;

import com.event.booking.dto.request.OTPNotificationRequest;

public interface EmailNotificationService {
    void sendOTPNotification(OTPNotificationRequest request);
}
