package com.popcorn.jrp.domain.response.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConversationMemberDTO {
    private Long userId;
    private LocalDateTime lastSeenAt;
}