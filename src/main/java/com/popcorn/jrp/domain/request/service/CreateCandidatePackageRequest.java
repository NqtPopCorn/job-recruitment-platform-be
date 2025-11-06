package com.popcorn.jrp.domain.request.service;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateCandidatePackageRequest {
    @NotBlank(message = "Package name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0")
    private BigDecimal price;

    @Min(value = 1, message = "Duration must be at least 1 day")
    private Integer durationDay;

    @NotNull(message = "isLifetime is required")
    private Boolean isLifetime;

    @NotNull(message = "Highlight profile days is required")
    @Min(value = 0, message = "Highlight profile days must be at least 0")
    private Integer highlightProfileDays;

    @NotNull(message = "Job apply limit is required")
    @Min(value = 0, message = "Job apply limit must be at least 0")
    private Integer jobApplyLimit;

    @NotNull(message = "Can view other candidates is required")
    private Boolean canViewOtherCandidates;
}