package com.popcorn.jrp.domain.request.chat;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MarkAsReadRequestDTO {

    @NotNull
    private Long conversationId;
}