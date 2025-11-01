package com.popcorn.jrp.domain.response.chat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConversationSummaryDTO {
    private Long id;
    private Long otherUserId;
    private String displayName;
    private String displayImageUrl;
    private String lastMessageContent;
    private LocalDateTime lastMessageAt;
    private Long unreadCount; // Số tin nhắn chưa đọc
}
