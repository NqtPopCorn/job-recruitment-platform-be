package com.popcorn.jrp.domain.response.candidate;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class CandidateDetailsResponse extends CandidateResponse {
    private Long userId;
    private String email;
    private String industry;
    private String country;
    private String city;
    private Integer experience;
    private String qualification;
    private String birthday;
    private String phone;
    private String currentSalary;
    private String expectedSalary;
    private String description;
    private List<String> languages;
    private List<SocialMediaResponse> socialMedias;
}
