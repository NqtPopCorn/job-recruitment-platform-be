package com.popcorn.jrp.domain.request.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.popcorn.jrp.domain.response.employer.JobSalaryDto;
import com.popcorn.jrp.domain.response.job.JobTypeDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO để cập nhật thông tin công việc (PATCH /api/v1/job).
 * Tất cả các trường đều là tùy chọn.
 */
@Data
@NoArgsConstructor
public class UpdateJobDto {

    @Size(max = 100, message = "Tên công việc không được vượt quá 100 ký tự")
    private String title;

    private String description;

    @Valid
    private List<String> jobTypes;

    @Valid
    private JobSalaryDto salary;

    private String level;

    private List<String> responsibilities;

    private List<String> skillAndExperiences;

    @Min(value = 0, message = "Số năm kinh nghiệm phải lớn hơn hoặc bằng 0")
    private Integer experience;

    @Valid
    private JobWorkTimeDto workTime;

    private String industry;

    @Min(value = 1, message = "Số lượng tuyển phải ít nhất là 1")
    private Integer quantity;

    private String country;

    private String city;

    private String location;

    @Future(message = "Ngày hết hạn phải ở trong tương lai")
    private LocalDate expirationDate;

    private List<String> skills;

    private Boolean status; // Tên trong tài liệu
}