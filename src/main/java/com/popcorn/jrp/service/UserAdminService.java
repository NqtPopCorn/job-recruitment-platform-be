package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.request.user.UserQueryAdmin;
import com.popcorn.jrp.domain.response.ApiNoDataResponse;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.user.UserAdminResponse;
import com.popcorn.jrp.domain.response.user.UserStatusStatisticResponse;
import org.springframework.data.domain.Pageable;

public interface UserAdminService {
    ApiPageResponse<UserAdminResponse> getUserListAdmin(UserQueryAdmin request, Pageable pageable);
    ApiNoDataResponse lockUser(Long id);
    UserStatusStatisticResponse getUserStatusStatistic();
}
