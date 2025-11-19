package com.popcorn.jrp.service.impl;

import com.popcorn.jrp.domain.entity.NotificationEntity;
import com.popcorn.jrp.domain.mapper.NotificationMapper;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.candidate.NotificationResponse;
import com.popcorn.jrp.exception.NotFoundException;
import com.popcorn.jrp.repository.NotificationRepository;
import com.popcorn.jrp.service.NotificationService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationServiceImpl implements NotificationService {

    NotificationRepository notificationRepository;
    NotificationMapper notificationMapper;

    @Override
    public List<NotificationResponse> getRecentNotifications(Long userId, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 6; // Default 6 notifications
        }

        Pageable pageable = PageRequest.of(0, limit);
        List<NotificationEntity> notifications = notificationRepository
                .findTop6ByUserIdOrderByCreatedAtDesc(userId, pageable);

        return notifications.stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ApiPageResponse<NotificationResponse> getAllNotifications(Long userId, Pageable pageable) {
        Page<NotificationEntity> page = notificationRepository
                .findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(userId, pageable);

        Page<NotificationResponse> responsePage = page.map(notificationMapper::toResponse);

        return ApiPageResponse.<NotificationResponse>builder()
                .statusCode(200)
                .message("Success")
                .results(responsePage.getContent())
                .meta(ApiPageResponse.Meta.builder()
                        .totalItems(responsePage.getTotalElements())
                        .currentPage(responsePage.getNumber())
                        .pageSize(responsePage.getSize())
                        .totalPages(responsePage.getTotalPages())
                        .build()
                )
                .build();
    }

    @Override
    public NotificationResponse markAsRead(Long notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException("Notification not found"));

        notification.setIsRead(true);
        notificationRepository.save(notification);

        return notificationMapper.toResponse(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsReadByUserId(userId);
    }

    @Override
    public Long getUnreadCount(Long userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    @Override
    public void deleteNotification(Long notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException("Notification not found"));

        notification.setDeleted(true);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getAllUnreadNotifications(Long userId) {
        List<NotificationEntity> notifications = notificationRepository
                .findAllUnreadByUserId(userId);

        return notifications.stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }
}