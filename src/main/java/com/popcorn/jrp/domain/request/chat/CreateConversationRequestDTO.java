package com.popcorn.jrp.domain.request.chat;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateConversationRequestDTO {

//    @Size(min = 2, max = 2, message = "This app only support private chat, please request 2 userId")
//    private List<Long> userIds;
    Long userId;
}
