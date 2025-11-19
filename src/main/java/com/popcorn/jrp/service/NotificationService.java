package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.candidate.NotificationResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
public interface NotificationService {
    List<NotificationResponse> getRecentNotifications(Long userId, Integer limit);

    ApiPageResponse<NotificationResponse> getAllNotifications(Long userId, Pageable pageable);

    NotificationResponse markAsRead(Long notificationId);

    void markAllAsRead(Long userId);

    Long getUnreadCount(Long userId);

    void deleteNotification(Long notificationId);

    List<NotificationResponse> getAllUnreadNotifications(Long userId);
}
