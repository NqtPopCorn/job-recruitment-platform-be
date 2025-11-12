package com.popcorn.jrp.domain.response.employer;

import com.popcorn.jrp.domain.response.common.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmployerListAdminResponse extends BaseResponse {
    private Long id;
    private String email;
    private String name;
    private String primaryIndustry;
    private String size;
    private int foundedIn;
    private String phone;
    private String address;
    private int jobNumber = 0;
    private String logo;
    private boolean status;
}
