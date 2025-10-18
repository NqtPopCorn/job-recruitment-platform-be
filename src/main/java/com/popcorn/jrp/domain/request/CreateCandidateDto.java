package com.popcorn.jrp.domain.request;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.response.SocialMediaResponse;
import lombok.*;
import java.util.List;

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
    private String experience;
    private String currentSalary;
    private String expectedSalary;
    private CandidateEntity.Gender gender;
    private List<String> language;
    private String educationLevel;
    private List<SocialMediaResponse> socialMedias;
}

