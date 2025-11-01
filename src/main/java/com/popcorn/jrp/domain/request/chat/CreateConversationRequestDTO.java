package com.popcorn.jrp.domain.request.chat;

import lombok.Data;

import java.util.List;

@Data
public class CreateConversationRequestDTO {
    // Dùng cho cả chat nhóm (nhiều hơn 2) hoặc chat 1-1
    private List<Long> userIds;
    // Có thể thêm tên nhóm, v.v. nếu là chat nhóm
    // private String groupName;
}
