package com.popcorn.jrp.domain.mapper;

import com.popcorn.jrp.domain.entity.NotificationEntity;
import com.popcorn.jrp.domain.response.candidate.NotificationMetadata;
import com.popcorn.jrp.domain.response.candidate.NotificationResponse;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = false))
public interface NotificationMapper {

    @Mapping(target = "metadata", source = ".", qualifiedByName = "extractMetadata")
    @Mapping(target = "message", ignore = true)
    NotificationResponse toResponse(NotificationEntity entity);

//    @Mapping(target = "id", source = "id")
//    @Mapping(target = "message", source = "message")
//    @Mapping(target = "createdAt", source = "createdAt")
//    @Mapping(target = "isRead", source = "isRead")
//    NotificationResponse toNotificationResponse(NotificationEntity entity);

    @Named("extractMetadata")
    default NotificationMetadata extractMetadata(NotificationEntity entity) {
        // Parse message để lấy thông tin
        String message = entity.getMessage();

        return NotificationMetadata.builder()
                .applicantName(extractApplicantName(message))
                .jobTitle(extractJobTitle(message))
                .notificationType(determineNotificationType(message))
                .build();
    }

    default String extractApplicantName(String message) {
        if (message == null) return null;
        if (message.contains(" applied for")) {
            return message.substring(0, message.indexOf(" applied for")).trim();
        }
        return null;
    }

    default String extractJobTitle(String message) {
        if (message == null) return null;
        if (message.contains(" applied for a job ")) {
            return message.substring(message.indexOf(" applied for a job ") + 19).trim();
        }
        return null;
    }

    default String determineNotificationType(String message) {
        if (message == null) return "GENERAL";
        if (message.contains("applied for")) return "APPLICATION";
        if (message.contains("message")) return "MESSAGE";
        if (message.contains("shortlist")) return "SHORTLIST";
        return "GENERAL";
    }
}