package com.popcorn.jrp.domain.dto.notification;

import java.time.LocalDateTime;

import com.popcorn.jrp.domain.enums.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobNotificationWebSocketDto {
    private Long id;

    private Long userId;

    private String message;

    private NotificationType type;

    private Boolean isRead;

    private LocalDateTime createdAt;
}
