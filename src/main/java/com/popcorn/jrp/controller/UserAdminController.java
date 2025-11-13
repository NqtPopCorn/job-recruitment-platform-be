package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.request.user.UserQueryAdmin;
import com.popcorn.jrp.domain.response.ApiDataResponse;
import com.popcorn.jrp.domain.response.ApiNoDataResponse;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.user.UserAdminResponse;
import com.popcorn.jrp.domain.response.user.UserStatusStatisticResponse;
import com.popcorn.jrp.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Nullable;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserAdminService userAdminService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiPageResponse<UserAdminResponse> getUserListAdmin(
            @Nullable Pageable pageable,
            @Nullable UserQueryAdmin request
    ) {
        var response = userAdminService.getUserListAdmin(request, pageable);
        response.setMessage("Lấy danh sách người dùng thành công!");
        response.setStatusCode(HttpStatus.OK.value());
        return response;
    }

    @PatchMapping("/{id}/lock")
    @ResponseStatus(HttpStatus.OK)
    public ApiNoDataResponse lockUser(@PathVariable Long id) {
        return userAdminService.lockUser(id);
    }

    @GetMapping("/statistics")
    @ResponseStatus(HttpStatus.OK)
    public ApiDataResponse<UserStatusStatisticResponse> getUserStatusStatistic() {
        UserStatusStatisticResponse data = userAdminService.getUserStatusStatistic();

        return ApiDataResponse.<UserStatusStatisticResponse>builder()
                .data(data)
                .message("Lấy thống kê số lượng người dùng theo trạng thái thành công!")
                .statusCode(HttpStatus.OK.value())
                .build();
    }

}
