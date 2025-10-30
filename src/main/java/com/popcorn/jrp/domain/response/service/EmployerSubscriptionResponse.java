package com.popcorn.jrp.domain.response.service;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EmployerSubscriptionResponse {
    private Long id;
    private Long employerId;
    private EmployerPackageResponse packageInfo;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Boolean isLifetime;
    private Integer usedJobCount;
    private Integer usedHighlightCount;
    private Integer totalJobCount;
    private Integer totalHighlightCount;
    private Integer remainingJobCount;
    private Integer remainingHighlightCount;
    private Integer renewCount;
}