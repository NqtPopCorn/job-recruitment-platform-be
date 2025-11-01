package com.popcorn.jrp.domain.response.employer;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho thông tin lương của công việc.
 */
@Data
@NoArgsConstructor
public class JobSalaryDto {

    @NotNull(message = "Lương tối thiểu (min) không được để trống")
    @Min(value = 0, message = "Lương tối thiểu phải lớn hơn hoặc bằng 0")
    private Double min;

    @NotNull(message = "Lương tối đa (max) không được để trống")
    @Min(value = 0, message = "Lương tối đa phải lớn hơn hoặc bằng 0")
    private Double max;

    @NotBlank(message = "Đơn vị tiền tệ (currency) không được để trống")
    private String currency; // "VND", "USD"

    @NotBlank(message = "Đơn vị tính lương (unit) không được để trống")
    private String unit; // "month", "hour"

    @NotNull(message = "Trường thỏa thuận (negotiable) không được để trống")
    private Boolean negotiable;
}
