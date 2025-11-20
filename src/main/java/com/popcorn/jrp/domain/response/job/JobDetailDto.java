package com.popcorn.jrp.domain.response.job;

import java.time.LocalDateTime;
import java.util.List;

import com.popcorn.jrp.domain.response.employer.EmployerDetailDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class JobDetailDto {
    private Long id;
    private String logo;
    private String title;
    private EmployerDetailDto company; // DTO lồng nhau
    private String level;
    private String location;
    private String description;
    private List<String> responsibilities;
    private List<String> skillAndExperiences;
    private SalaryDto salary; // DTO lồng nhau
    private WorkTimeDto workTime; // DTO lồng nhau
    private String industry;
    private int quantity;
    private String country;
    private String city;
    private int experience;
    private List<String> jobTypes; // DTO lồng nhau
    private List<String> skills;
    private String destination;
    private Boolean status;
    private LocalDateTime datePosted;
    private LocalDateTime expireDate;

    @Builder.Default
    private Integer applications = 0;
}