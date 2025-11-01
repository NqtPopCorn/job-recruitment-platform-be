package com.popcorn.jrp.domain.request.chat;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendMessageRequestDTO {
    @NotNull
    private Long conversationId;
    @NotNull
    private String content;
}
