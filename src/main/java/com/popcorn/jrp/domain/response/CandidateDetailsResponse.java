package com.popcorn.jrp.domain.response;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateDetailsResponse {
    private String id;
    private String userId;
    private String email;
    private String avatar;
    private String name;
    private String industry;
    private String designation;
    private String country;
    private String city;
    private String location;
    private Double hourlyRate;
    private List<String> tags;
    private String category;
    private CandidateEntity.Gender gender;
    private String createdAt;
    private Object experience;
    private String qualification;
    private String birthday;
    private String phone;
    private Object currentSalary;
    private Object expectedSalary;
    private String currency;
    private String description;
    private List<String> language;
    private List<SocialMediaResponse> socialMedias;
    private Boolean status;
}
