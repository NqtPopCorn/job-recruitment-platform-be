package com.popcorn.jrp.domain.response.chat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConversationMemberDTO {
    private Long userId;
    private String userName;
    private String userAvatarUrl;
    private LocalDateTime lastSeenAt;
}