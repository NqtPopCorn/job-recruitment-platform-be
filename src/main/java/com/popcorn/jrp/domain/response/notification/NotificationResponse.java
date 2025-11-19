package com.popcorn.jrp.domain.response.notification;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    Long id;
    String message;
    LocalDateTime createdAt;
    Boolean isRead;
}