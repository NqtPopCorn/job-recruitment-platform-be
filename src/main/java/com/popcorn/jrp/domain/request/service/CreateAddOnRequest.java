package com.popcorn.jrp.domain.request.service;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateAddOnRequest {
    @NotBlank(message = "Add-on name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0")
    private BigDecimal price;

    @NotBlank(message = "Type is required")
    @Pattern(regexp = "^(JOB_POST|HIGHLIGHT|CV_VIEW)$",
            message = "Type must be JOB_POST, HIGHLIGHT, or CV_VIEW")
    private String type;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @Min(value = 1, message = "Duration must be at least 1 day")
    private Integer durationDay;

    @NotNull(message = "isLifetime is required")
    private Boolean isLifetime;
}