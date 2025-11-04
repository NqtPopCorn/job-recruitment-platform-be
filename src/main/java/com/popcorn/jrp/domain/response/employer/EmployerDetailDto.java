package com.popcorn.jrp.domain.response.employer;

import java.time.LocalDateTime;
import java.util.List;

import com.popcorn.jrp.domain.response.common.BaseResponse;
import com.popcorn.jrp.domain.response.common.SocialMediaDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmployerDetailDto extends BaseResponse {
    private Long id;
    private String userId; // thêm
    private String name;
    private String email;
    private String phone;
    private String primaryIndustry;
    private String size;
    private int foundedIn;
    private String description;
    private String country;
    private String city;
    private String address;
    private String website;
    private boolean status;
    private int jobNumber = 0;
    private String logo;

    private List<SocialMediaDto> socialMedias;

}