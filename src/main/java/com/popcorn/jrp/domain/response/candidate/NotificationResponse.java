package com.popcorn.jrp.domain.response.candidate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class NotificationResponse {
    Long id;
    String message;
    Boolean isRead;
    LocalDateTime createdAt;
    NotificationMetadata metadata;
}
