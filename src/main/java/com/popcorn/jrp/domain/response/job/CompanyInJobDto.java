package com.popcorn.jrp.domain.response.job;

import java.time.Instant;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyInJobDto {
    private Long id;
    private String email;
    private String name;
    private Long userId;
    private String primaryIndustry;
    private String size;
    private Integer foundedIn;
    private String description;
    private String phone;
    private String address;
    private String logo;
    private List<SocialMediaDto> socialMedias;
    private Boolean isDeleted;
    private Instant createdAt;
    private Instant updatedAt;
}