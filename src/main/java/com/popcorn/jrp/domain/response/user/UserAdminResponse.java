package com.popcorn.jrp.domain.response.user;

import com.popcorn.jrp.domain.response.common.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserAdminResponse extends BaseResponse {
    private Long id;
    private String email;
    private Boolean status;
}
