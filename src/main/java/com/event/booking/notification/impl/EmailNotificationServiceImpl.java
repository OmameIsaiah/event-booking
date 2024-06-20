package com.event.booking.notification.impl;

import com.event.booking.dto.request.OTPNotificationRequest;
import com.event.booking.dto.response.ReservationResponseDTO;
import com.event.booking.notification.EmailNotificationService;
import com.event.booking.util.Utils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static com.event.booking.util.Utils.*;

@Service
@RequiredArgsConstructor
public class EmailNotificationServiceImpl implements EmailNotificationService {
    private final JavaMailSender javaMailSender;
    private final Configuration freemarkerConfig;

    @Value("${app.mail.sender.email}")
    private String MAIL_SENDER_EMAIL;
    @Value("${app.mail.sender.name}")
    private String MAIL_SENDER_NAME;

    @Override
    @Async
    public void sendOTPNotification(OTPNotificationRequest request) {
        //TODO SEND IT THROUGH KAFKA OR REDIS
        try {
            Map model = new HashMap();
            model.put("email", request.getEmail());
            model.put("name", request.getName());
            model.put("otp", request.getOtp());
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), EMAIL_TEMPLATES_DIR);
            Template t = freemarkerConfig.getTemplate(OTP_EMAIL_TEMPLATE);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(request.getEmail());
            helper.setFrom(new InternetAddress(MAIL_SENDER_EMAIL, MAIL_SENDER_NAME));
            helper.setText(html, true);
            helper.setSubject(OTP_EMAIL_SUBJECT);
            javaMailSender.send(message);
        } catch (MessagingException | IOException | TemplateException ex) {
            java.util.logging.Logger.getLogger(EmailNotificationService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void sendTicketReservationEmail(ReservationResponseDTO request) {
        //TODO SEND IT THROUGH KAFKA OR REDIS
        try {
            Map model = Utils.mapReservationAndEventReminderModel(request);
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), EMAIL_TEMPLATES_DIR);
            Template t = freemarkerConfig.getTemplate(RESERVATION_EMAIL_TEMPLATE);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(request.getEmail());
            helper.setFrom(new InternetAddress(MAIL_SENDER_EMAIL, MAIL_SENDER_NAME));
            helper.setText(html, true);
            helper.setSubject(RESERVATION_EMAIL_SUBJECT);
            javaMailSender.send(message);
        } catch (MessagingException | IOException | TemplateException ex) {
            java.util.logging.Logger.getLogger(EmailNotificationService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void sendEventReminder(ReservationResponseDTO request) {
        //TODO SEND IT THROUGH KAFKA OR REDIS
        try {
            Map model = Utils.mapReservationAndEventReminderModel(request);
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), EMAIL_TEMPLATES_DIR);
            Template t = freemarkerConfig.getTemplate(EVENT_REMINDER_EMAIL_TEMPLATE);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(request.getEmail());
            helper.setFrom(new InternetAddress(MAIL_SENDER_EMAIL, MAIL_SENDER_NAME));
            helper.setText(html, true);
            helper.setSubject(EVENT_REMINDER_EMAIL_SUBJECT);
            javaMailSender.send(message);
        } catch (MessagingException | IOException | TemplateException ex) {
            java.util.logging.Logger.getLogger(EmailNotificationService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
