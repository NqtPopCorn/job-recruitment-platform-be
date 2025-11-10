package com.popcorn.jrp.domain.request.employer;

import java.util.List;

import com.popcorn.jrp.domain.response.common.SocialMediaDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEmployerDto {

    private String name;

    private String phone;

    private String website;

    private String primaryIndustry;

    private Integer size;

    private Integer foundedIn;

    private String description;

    private String country;

    private String city;

    private String address;

    private String logo;

    private Boolean status;

    private List<SocialMediaDto> socialMedias;
}
