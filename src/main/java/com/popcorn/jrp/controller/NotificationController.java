package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.response.ApiDataResponse;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.ApiResponse;
import com.popcorn.jrp.domain.response.ApiResultsResponse;
import com.popcorn.jrp.domain.response.candidate.NotificationResponse;
import com.popcorn.jrp.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * GET RECENT NOTIFICATIONS (Default: 6 latest)
     * GET /api/v1/notifications/recent?userId={userId}&limit=6
     */
    @GetMapping("/recent")
    @ResponseStatus(HttpStatus.OK)
    public ApiResultsResponse<NotificationResponse> getRecentNotifications(
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "6") Integer limit) {

        List<NotificationResponse> notifications = notificationService
                .getRecentNotifications(userId, limit);

        return ApiResultsResponse.<NotificationResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Lấy thông báo mới nhất thành công!")
                .results(notifications)
                .build();
    }

    /**
     * GET ALL NOTIFICATIONS WITH PAGINATION
     * GET /api/v1/notifications?userId={userId}&page=0&size=20
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiPageResponse<NotificationResponse> getAllNotifications(
            @RequestParam Long userId,
            Pageable pageable) {

        ApiPageResponse<NotificationResponse> response = notificationService
                .getAllNotifications(userId, pageable);
        response.setMessage("Lấy danh sách thông báo thành công!");
        response.setStatusCode(HttpStatus.OK.value());

        return response;
    }

    /**
     * GET UNREAD COUNT
     * GET /api/v1/notifications/unread-count?userId={userId}
     */
    @GetMapping("/unread-count")
    @ResponseStatus(HttpStatus.OK)
    public ApiDataResponse<Long> getUnreadCount(@RequestParam Long userId) {
        Long count = notificationService.getUnreadCount(userId);

        return ApiDataResponse.<Long>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Lấy số thông báo chưa đọc thành công!")
                .data(count)
                .build();
    }

    /**
     * MARK AS READ
     * PATCH /api/v1/notifications/{id}/read
     */
    @PatchMapping("/{id}/read")
    @ResponseStatus(HttpStatus.OK)
    public ApiDataResponse<NotificationResponse> markAsRead(@PathVariable Long id) {
        NotificationResponse notification = notificationService.markAsRead(id);

        return ApiDataResponse.<NotificationResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Đánh dấu đã đọc thành công!")
                .data(notification)
                .build();
    }

    /**
     * MARK ALL AS READ
     * PATCH /api/v1/notifications/read-all?userId={userId}
     */
    @PatchMapping("/read-all")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse markAllAsRead(@RequestParam Long userId) {
        notificationService.markAllAsRead(userId);

        return ApiResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Đánh dấu tất cả thông báo đã đọc thành công!")
                .build();
    }

}
