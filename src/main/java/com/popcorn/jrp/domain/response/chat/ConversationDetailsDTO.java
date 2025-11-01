package com.popcorn.jrp.domain.response.chat;

import lombok.Data;

import java.util.List;

@Data
public class ConversationDetailsDTO {
    private Long id;
    private String displayName;
    private String displayImageUrl;
    private List<ConversationMemberDTO> members;
}
