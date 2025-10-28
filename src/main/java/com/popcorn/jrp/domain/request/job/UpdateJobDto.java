package com.popcorn.jrp.domain.request.job;

import com.popcorn.jrp.domain.response.job.JobTypeDto;
import com.popcorn.jrp.domain.response.job.SalaryDto;
import com.popcorn.jrp.domain.response.job.WorkTimeDto;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateJobDto {
    // Các trường này là tùy chọn khi cập nhật (PATCH)
    private String jobTitle;
    private String description;
    private List<String> skills;
    private List<JobTypeDto> jobType;
    private String level;
    private List<String> responsibilities;
    private List<String> skillAndExperience;
    private Integer experience;
    private String industry;
    private Integer quantity;
    private String country;
    private String city;
    private String location;
    private LocalDate expirationDate;
    private Boolean status; // Dùng cho dashboard (active/inactive)

    @Valid
    private SalaryDto salary;

    @Valid
    private WorkTimeDto workTime;
}