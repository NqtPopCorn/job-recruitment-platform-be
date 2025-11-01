package com.popcorn.jrp.domain.response.chat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private Long id;
    private Long conversationId;
    private Long senderUserId;
    private String senderName; // Tên người gửi
    private String content;
    private LocalDateTime createdAt;
    private boolean isDeleted;
}
