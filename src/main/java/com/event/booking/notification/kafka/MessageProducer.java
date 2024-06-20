package com.event.booking.notification.kafka;

import com.event.booking.dto.request.QueueRequest;
import com.event.booking.exceptions.BadRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static com.event.booking.util.Utils.TOPIC_NOTIFICATION;


@Component
@Slf4j
@RequiredArgsConstructor
public class MessageProducer {

    private final ObjectMapper objectMapper;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String type, Object message) {
        QueueRequest request = QueueRequest.builder()
                .type(type)
                .data(message)
                .build();
        String content = "";
        try {
            content = objectMapper.writeValueAsString(request);

        } catch (JsonProcessingException e) {
            throw new BadRequestException("Error writing content: {}" + e.getMessage());
        }
        log.info("######################### Event Content: {}", content);
        kafkaTemplate.send(TOPIC_NOTIFICATION, content);
    }
}