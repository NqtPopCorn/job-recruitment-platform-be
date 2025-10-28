package com.popcorn.jrp.domain.response.service;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CandidateSubscriptionResponse {
    private Long id;
    private Long candidateId;
    private CandidatePackageResponse packageInfo;
    private String subscriptionCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Boolean isLifetime;
    private Integer usedApplyCount;
    private Integer usedHighlightDays;
    private Integer totalApplyLimit;
    private Integer totalHighlightDays;
    private Integer remainingApplyCount;
    private Integer remainingHighlightDays;
    private Integer renewCount;
}