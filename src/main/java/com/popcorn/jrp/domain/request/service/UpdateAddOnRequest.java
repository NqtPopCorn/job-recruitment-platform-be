package com.popcorn.jrp.domain.request.service;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateAddOnRequest {

    @Size(min = 3, max = 100, message = "Add-on name must be between 3 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Min(value = 0, message = "Price must be non-negative")
    private BigDecimal price;

    private String type; // JOB_POST hoặc HIGHLIGHT

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @Min(value = 1, message = "Duration must be at least 1 day")
    private Integer durationDay;

    private Boolean isLifetime;
}