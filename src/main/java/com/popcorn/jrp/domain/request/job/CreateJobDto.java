package com.popcorn.jrp.domain.request.job;

import com.popcorn.jrp.domain.response.employer.JobSalaryDto;
import com.popcorn.jrp.domain.response.job.JobTypeDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO để tạo mới một công việc (POST /api/v1/job).
 */
@Data
@NoArgsConstructor
public class CreateJobDto {

    @NotBlank(message = "Tên công việc không được để trống")
    @Size(max = 100, message = "Tên công việc không được vượt quá 100 ký tự")
    private String title;

    @NotBlank(message = "Company ID không được để trống")
    private String companyId;

    @NotBlank(message = "Trình độ (level) không được để trống")
    private String level;

    @NotEmpty(message = "Trách nhiệm (responsibilities) không được để trống")
    private List<String> responsibilities;

    @NotEmpty(message = "Kỹ năng và kinh nghiệm (skillAndExperience) không được để trống")
    private List<String> skillAndExperiences;

    @NotNull(message = "Số năm kinh nghiệm (experience) không được để trống")
    @Min(value = 0, message = "Số năm kinh nghiệm phải lớn hơn hoặc bằng 0")
    private Integer experience;

    @NotBlank(message = "Ngành nghề (industry) không được để trống")
    private String industry;

    @NotNull(message = "Số lượng (quantity) không được để trống")
    @Min(value = 1, message = "Số lượng tuyển phải ít nhất là 1")
    private Integer quantity;

    @NotBlank(message = "Quốc gia (country) không được để trống")
    private String country;

    @NotBlank(message = "Thành phố (city) không được để trống")
    private String city;

    @NotBlank(message = "Địa chỉ (location) không được để trống")
    private String location;

    @NotNull(message = "Ngày hết hạn (expirationDate) không được để trống")
    @Future(message = "Ngày hết hạn phải ở trong tương lai")
    private LocalDate expirationDate;

    // --- Các trường tùy chọn (Optional) ---

    private String description;

    @Valid // Thêm @Valid để kiểm tra các đối tượng lồng nhau
    private List<String> jobTypes;

    @Valid // Thêm @Valid để kiểm tra đối tượng lồng nhau
    private JobSalaryDto salary;

    @Valid // Thêm @Valid để kiểm tra đối tượng lồng nhau
    private JobWorkTimeDto workTime;

    private List<String> skills;

    private Boolean status; // Mặc định là true
}