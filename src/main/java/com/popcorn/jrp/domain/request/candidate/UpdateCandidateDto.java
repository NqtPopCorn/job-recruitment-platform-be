package com.popcorn.jrp.domain.request.candidate;

import com.popcorn.jrp.domain.response.common.SocialMediaDto;

import lombok.*;
import java.util.List;

import com.popcorn.jrp.domain.response.common.SocialMediaResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCandidateDto {
    private String name;
    private String birthday;
    private String phone;
    private String industry;
    private List<String> skills;
    private String avatar;
    private String designation;
    private String country;
    private String city;
    private String location;
    private Double hourlyRate;
    private String description;
    private Integer experience;
    private String currentSalary;
    private String expectedSalary;
    private String gender;
    private List<String> languages;
    private String educationLevel;
    private List<SocialMediaDto> socialMedias;
    private Boolean status;
}
