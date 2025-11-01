package com.popcorn.jrp.domain.response.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.messaging.Message;
import org.springframework.messaging.core.MessagePostProcessor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReadReceiptDTO  {
    private Long conversationId;
    private Long userId;
    private LocalDateTime readAt;
}
