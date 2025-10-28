package com.popcorn.jrp.domain.request.candidate;

import lombok.*;
import java.util.List;

import com.popcorn.jrp.domain.response.common.SocialMediaResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCandidateDto {
    private String userId;
    private String name;
    private String birthday;
    private String phone;
    private String industry;
    private List<String> skills;
    private String avatar;
    private String designation;
    private String location;
    private Double hourlyRate;
    private String description;
    private Integer experience;
    private int currentSalary;
    private int expectedSalary;
    private String currency;
    private String gender;
    private List<String> languages;
    private String educationLevel;
    // private List<SocialMediaResponse> socialMedias;
}
