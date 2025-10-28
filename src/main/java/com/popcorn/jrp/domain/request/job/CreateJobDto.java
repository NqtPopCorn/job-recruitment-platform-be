package com.popcorn.jrp.domain.request.job;

import com.popcorn.jrp.domain.response.job.JobTypeDto;
import com.popcorn.jrp.domain.response.job.SalaryDto;
import com.popcorn.jrp.domain.response.job.WorkTimeDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateJobDto {

    @NotBlank(message = "Employer ID không được để trống")
    private String employerId; // Sẽ được chuyển sang Long trong Service

    @NotBlank(message = "Job Title không được để trống")
    private String jobTitle;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    private List<String> skills; // Sẽ được chuyển thành JSON String

    private List<JobTypeDto> jobType; // Sẽ được chuyển thành JSON String

    private String level;

    private List<String> responsibilities; // Sẽ được chuyển thành JSON String

    private List<String> skillAndExperience; // Sẽ được chuyển thành JSON String

    private Integer experience;

    @NotBlank(message = "Ngành nghề không được để trống")
    private String industry;

    @NotNull(message = "Số lượng không được để trống")
    @Positive(message = "Số lượng phải là số dương")
    private Integer quantity;

    @NotBlank(message = "Quốc gia không được để trống")
    private String country;

    private String city;

    @NotBlank(message = "Địa điểm không được để trống")
    private String location;

    @NotNull(message = "Ngày hết hạn không được để trống")
    private LocalDate expirationDate;

    @NotNull(message = "Thông tin lương không được để trống")
    @Valid
    private SalaryDto salary;

    @Valid
    private WorkTimeDto workTime;
}