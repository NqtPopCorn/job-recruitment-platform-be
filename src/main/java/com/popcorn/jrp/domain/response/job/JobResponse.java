package com.popcorn.jrp.domain.response.job;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.Destination;

import com.popcorn.jrp.domain.response.employer.CompanyResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {
    private Long id;
    private String logo;
    private String jobTitle;
    private CompanyResponse company; // có thể null
    private String location;
    private String description;
    private List<String> responsibilities;
    private List<String> skillAndExperience;
    private String level; // có thể null
    private SalaryResponse salary; // có thể null
    private WorkTimeResponse workTime; // có thể null
    private String industry;
    private int quantity;
    private String country;
    private String city;
    private List<String> jobType; // string[]
    private String website; // có thể null
    private Destination destination; // có thể null
    private String category;
    private LocalDate datePosted; // có thể null
    private LocalDate expireDate; // có thể null
    @Builder.Default
    private int experience = 0;

    @Builder.Default
    private Integer applications = 0;

    @Builder.Default
    private List<String> skills = new ArrayList<>();

    private boolean status;
}
