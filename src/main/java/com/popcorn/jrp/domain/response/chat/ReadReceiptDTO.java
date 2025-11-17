package com.popcorn.jrp.domain.response.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.messaging.Message;
import org.springframework.messaging.core.MessagePostProcessor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadReceiptDTO {
    private Long conversationId;
    private Long userId;
    private LocalDateTime readAt;
}
