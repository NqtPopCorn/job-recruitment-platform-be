package com.popcorn.jrp.domain.response.job;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JobDetailDto {
    private String id; // ID là String, theo chỉ dẫn
    private String logo;
    private String jobTitle;
    private CompanyInJobDto company; // DTO lồng nhau
    private String location;
    private String description;
    private List<String> responsibilities;
    private List<String> skillAndExperience;
    private SalaryDto salary; // DTO lồng nhau
    private WorkTimeDto workTime; // DTO lồng nhau
    private String industry;
    private Integer quantity;
    private String country;
    private String city;
    private List<JobTypeDto> jobType; // DTO lồng nhau
    private String destination;
    private String datePosted; // Định dạng "d/M/yyyy"
    private String expireDate; // Định dạng "d/M/yyyy"
}