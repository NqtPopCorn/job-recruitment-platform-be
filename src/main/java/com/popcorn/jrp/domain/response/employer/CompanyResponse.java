package com.popcorn.jrp.domain.response.employer;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.popcorn.jrp.domain.response.common.SocialMediaResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyResponse {
    private String id;
    private String email;
    private String name;
    private String userId;
    private String primaryIndustry; // optional
    private String size; // optional
    private Integer foundedIn; // optional
    private String description; // optional
    private String phone;
    private String country; // optional
    private String city; // optional
    private String address; // optional
    private String logo; // optional
    private String website; // optional
    private Integer jobNumber; // optional
    private Boolean status; // optional

    private List<SocialMediaResponse> socialMedias; // optional

    // private UserInfo createdBy; // optional
    // private UserInfo updatedBy; // optional
    // private UserInfo deletedBy; // optional

    private boolean isDeleted;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
