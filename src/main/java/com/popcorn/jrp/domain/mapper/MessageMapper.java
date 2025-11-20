// File: src/main/java/com/popcorn/jrp/domain/mapper/MessageMapper.java

package com.popcorn.jrp.domain.mapper;

import com.popcorn.jrp.domain.entity.MessageEntity;
import com.popcorn.jrp.domain.response.chat.MessageDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(source = "conversation.id", target = "conversationId")
    @Mapping(source = "senderUser.id", target = "senderUserId")
    MessageDTO toDto(MessageEntity entity);

    List<MessageDTO> toDtoList(List<MessageEntity> entities);

    @AfterMapping
    default void setCustomFields(@MappingTarget MessageDTO dto, MessageEntity entity) {
        if (Boolean.TRUE.equals(entity.getIsDeleted())) {
            dto.setContent("This message has been deleted.");
        }
    }
}