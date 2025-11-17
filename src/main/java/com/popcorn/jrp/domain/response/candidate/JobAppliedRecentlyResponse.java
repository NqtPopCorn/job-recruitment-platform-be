package com.popcorn.jrp.domain.response.candidate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class JobAppliedRecentlyResponse {
    private Long jobId;
    private Long applicationId;
    private String jobTitle;
    private String companyName;
    private String companyLogo;
    private String location;
    private String city;
    private String country;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private String currency;
    private LocalDateTime appliedAt;
    private String applicationStatus; // PENDING, REVIEWED, ACCEPTED, REJECTED
    private List<String> jobTypes; // Full Time, Part Time, etc.
    private Boolean isUrgent;
    private String level; //
}
