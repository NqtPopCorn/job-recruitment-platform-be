package com.popcorn.jrp.domain.request.service;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateEmployerPackageRequest {

    @Size(min = 3, max = 100, message = "Package name must be between 3 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Min(value = 0, message = "Price must be non-negative")
    private BigDecimal price;

    @Min(value = 1, message = "Duration must be at least 1 day")
    private Integer durationDay;

    private Boolean isLifetime;

    @Min(value = 0, message = "Job post limit must be non-negative")
    private Integer jobPostLimit;

    @Min(value = 0, message = "Highlight job limit must be non-negative")
    private Integer highlightJobLimit;
}