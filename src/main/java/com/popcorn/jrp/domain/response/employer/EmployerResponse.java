package com.popcorn.jrp.domain.response.employer;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.popcorn.jrp.domain.response.common.SocialMediaResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployerResponse {
    private String id;
    private String email;
    private String name;
    private boolean isDeleted;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
