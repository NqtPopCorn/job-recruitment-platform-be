package com.popcorn.jrp.domain.mapper;

import com.popcorn.jrp.domain.entity.ConversationMemberEntity;
import com.popcorn.jrp.domain.response.chat.ConversationMemberDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConversationMemberMapper {

    @Mapping(source = "user.id", target = "userId")
//    @Mapping(target = "userName", expression = "java(\"User \" + entity.getUser().getId())")
//    @Mapping(target = "userAvatarUrl", expression = "java(null)")
    ConversationMemberDTO toDto(ConversationMemberEntity entity);
}