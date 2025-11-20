package com.popcorn.jrp.messaging.consumer.impl;

import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.popcorn.jrp.domain.dto.notification.JobNotificationWebSocketDto;
import com.popcorn.jrp.domain.response.ApiEventChatResponse;
import com.popcorn.jrp.domain.response.chat.MessageDTO;
import com.popcorn.jrp.messaging.consumer.WebSocketNotificationConsumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketNotificationConsumerImpl implements WebSocketNotificationConsumer {

    private final String WEB_SOCKET_TOPIC = "web-socket-notification";

    private final ObjectMapper objectMapper;

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @KafkaListener(topics = WEB_SOCKET_TOPIC, groupId = "main-service-group", containerFactory = "mainServiceListenerFactory")
    public void consumeWebSocketJobNotification(String message) {
        try {
            log.info("Received WebSocket notification message: {}", message);
            JobNotificationWebSocketDto jobNotificationDto = objectMapper.readValue(message,
                    JobNotificationWebSocketDto.class);

            // Send notification to WebSocket clients
            String destination = "/topic/notifications/" + jobNotificationDto.getUserId();
            messagingTemplate.convertAndSend(destination, jobNotificationDto);

        } catch (JsonProcessingException e) {
            log.error("JSON parse error: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Kafka listener error: ", e);
        }

    }
}
