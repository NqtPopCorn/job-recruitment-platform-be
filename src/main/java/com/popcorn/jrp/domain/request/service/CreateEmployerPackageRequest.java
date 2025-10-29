package com.popcorn.jrp.domain.request.service;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateEmployerPackageRequest {
    @NotBlank(message = "Package name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0")
    private BigDecimal price;

    @Min(value = 1, message = "Duration must be at least 1 day")
    private Integer durationDay; // Null nếu lifetime

    @NotNull(message = "isLifetime is required")
    private Boolean isLifetime;

    @NotNull(message = "Job post limit is required")
    @Min(value = 0, message = "Job post limit must be at least 0")
    private Integer jobPostLimit;

    @NotNull(message = "Highlight job limit is required")
    @Min(value = 0, message = "Highlight job limit must be at least 0")
    private Integer highlightJobLimit;
}