package com.popcorn.jrp.domain.mapper;

import com.popcorn.jrp.domain.entity.UserEntity;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.user.UserAdminResponse;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface UserMapper extends PageMapper{

    UserAdminResponse toAdminResponse(UserEntity entity);

    default ApiPageResponse<UserAdminResponse> toPageResponse(Page<UserAdminResponse> page) {
        return toApiPageResponse(page);
    }
}
