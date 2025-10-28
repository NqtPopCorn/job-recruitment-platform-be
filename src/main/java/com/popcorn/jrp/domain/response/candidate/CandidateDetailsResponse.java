package com.popcorn.jrp.domain.response.candidate;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

import com.popcorn.jrp.domain.response.common.SocialMediaResponse;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class CandidateDetailsResponse extends CandidateResponse {
    private Long id;
    private Long userId;
    private String email;
    private String avatar;
    private String name;
    private String industry;
    private String designation;
    private String country;
    private String city;
    private String location;
    private Double hourlyRate;
    private List<String> tags; // skills
    private String category; // industry
    private String gender;
    private String createdAt;
    private Integer experience;
    private String qualification;
    private String birthday;
    private String phone;
    private String currentSalary;
    private String expectedSalary;
    private String description;
    private List<String> languages;
    private List<SocialMediaResponse> socialMedias;
    private Boolean status;
}
