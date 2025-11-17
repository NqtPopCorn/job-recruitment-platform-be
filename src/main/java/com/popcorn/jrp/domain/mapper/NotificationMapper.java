package com.popcorn.jrp.domain.mapper;

import com.popcorn.jrp.domain.entity.NotificationEntity;
import com.popcorn.jrp.domain.response.notification.NotificationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(source = "user.id", target = "userId")
    NotificationResponse toResponse(NotificationEntity entity);

    List<NotificationResponse> toResponseList(List<NotificationEntity> entities);
}