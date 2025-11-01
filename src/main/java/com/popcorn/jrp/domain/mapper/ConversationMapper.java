package com.popcorn.jrp.domain.mapper;

import com.popcorn.jrp.domain.entity.ConversationEntity;
import com.popcorn.jrp.domain.response.chat.ConversationDetailsDTO;
import com.popcorn.jrp.domain.response.chat.ConversationSummaryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ConversationMemberMapper.class})
public interface ConversationMapper extends PageMapper {

    /**
     * Mapper cho ConversationDetailsDTO (chứa danh sách thành viên).
     */
    @Mapping(target = "displayName", ignore = true)
    @Mapping(target = "displayImageUrl", ignore = true)
    @Mapping(source = "members", target = "members")
    ConversationDetailsDTO toDetailsDto(ConversationEntity entity);

    /**
     * Mapper cho ConversationSummaryDTO (không chứa danh sách thành viên).
     */
    @Mapping(target = "displayName", ignore = true)
    @Mapping(target = "displayImageUrl", ignore = true)
    @Mapping(target = "lastMessageContent", ignore = true)
    @Mapping(target = "lastMessageAt", ignore = true)
    @Mapping(target = "unreadCount", ignore = true)
    ConversationSummaryDTO toSummaryDto(ConversationEntity entity);
}