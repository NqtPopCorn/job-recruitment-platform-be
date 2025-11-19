package com.popcorn.jrp.domain.response.candidate;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobAppliedRecentlyResponse {
    Long applicationId;
    Long jobId;
    String jobTitle;
    CompanyInfo company;
    String location;
    SalaryRange salary;
    List<String> jobTypes; // ["Full Time", "Private", "Urgent"]
    LocalDateTime appliedAt;
    String appliedTimeAgo;
    String applicationStatus; // PENDING, REVIEWED, ACCEPTED, REJECTED
    Boolean isSaved;
}
