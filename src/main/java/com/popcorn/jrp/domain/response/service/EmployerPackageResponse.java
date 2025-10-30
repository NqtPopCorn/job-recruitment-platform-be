package com.popcorn.jrp.domain.response.service;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class EmployerPackageResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationDay;
    private Boolean isLifetime;
    private Integer jobPostLimit;
    private Integer highlightJobLimit;
}