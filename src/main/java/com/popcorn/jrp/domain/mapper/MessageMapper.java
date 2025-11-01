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
public interface MessageMapper extends PageMapper {

    @Mapping(source = "conversation.id", target = "conversationId")
    @Mapping(source = "senderUser.id", target = "senderUserId")
    @Mapping(source = "deleted", target = "deleted") // Map `isDeleted` (boolean) sang `deleted` (boolean)
    MessageDTO toDto(MessageEntity entity);

    List<MessageDTO> toDtoList(List<MessageEntity> entities);

    @AfterMapping
    default void setCustomFields(@MappingTarget MessageDTO dto, MessageEntity entity) {

        if (entity.getSenderUser() != null) {
            dto.setSenderName("User " + entity.getSenderUser().getId());
            // TODO: Thay thế bằng logic thực tế (vd: entity.getSenderUser().getFullName())
        }

        if (entity.isDeleted()) {
            dto.setContent("This message has been deleted.");
        }
    }
}