package com.popcorn.jrp.domain.response.chat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConversationSummaryDTO {
    private Long id;
    private String displayName; // Tên của người kia (nếu chat 1-1) hoặc tên nhóm
    private String displayImageUrl; // Avatar của người kia hoặc nhóm
    private String lastMessageContent;
    private LocalDateTime lastMessageAt;
    private Long unreadCount; // Số tin nhắn chưa đọc
}
