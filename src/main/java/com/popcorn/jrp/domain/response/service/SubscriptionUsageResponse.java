package com.popcorn.jrp.domain.response.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscriptionUsageResponse {
    private Boolean canPostJob;
    private Boolean canHighlightJob;
    private Boolean canApplyJob;
    private Boolean canHighlightProfile;
    private Integer remainingJobPosts;
    private Integer remainingHighlights;
    private Integer remainingApplies;
    private Integer remainingHighlightDays;
    private String message;
}