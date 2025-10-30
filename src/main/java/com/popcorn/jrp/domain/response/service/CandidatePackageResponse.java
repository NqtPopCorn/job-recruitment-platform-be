package com.popcorn.jrp.domain.response.service;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CandidatePackageResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationDay;
    private Boolean isLifetime;
    private Integer highlightProfileDays;
    private Integer jobApplyLimit;
    private Boolean canViewOtherCandidates;
}