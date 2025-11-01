package com.popcorn.jrp.domain.request.chat;

import lombok.Data;

@Data
public class DeleteMessageDto {
    private Long conversationId;
    private Long messageId;
}
