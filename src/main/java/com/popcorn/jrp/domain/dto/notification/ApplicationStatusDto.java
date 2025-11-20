package com.popcorn.jrp.domain.dto.notification;

import com.popcorn.jrp.domain.enums.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationStatusDto {
    private Long userId;
    private Long jobId;
    private String message;
    private NotificationType type;
}
