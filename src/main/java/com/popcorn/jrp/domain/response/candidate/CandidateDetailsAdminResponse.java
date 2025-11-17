package com.popcorn.jrp.domain.response.candidate;

import com.popcorn.jrp.domain.response.common.SocialMediaDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class CandidateDetailsAdminResponse {
    private Long id;
    private String userId;
    private String avatar;
    private String name;
    private String email;
    private LocalDate birthday;
    private String designation;
    private String location;
    private String country;
    private String city;
    private Double hourlyRate;
    private List<String> tags; // skills
    private String category; // industry
    private String gender;
    private LocalDateTime createdAt;
    private Integer experience;
    private String qualification; // educationLevel
    private String currentSalary;
    private String expectedSalary;
    private String description;
    private List<String> languages;
    private List<SocialMediaDto> socialMedias;

    // Thêm các field dành cho admin
    private boolean status;
    private boolean isDeleted;
}