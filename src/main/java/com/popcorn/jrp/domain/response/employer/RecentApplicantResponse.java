package com.popcorn.jrp.domain.response.employer;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentApplicantResponse {
    Long applicationId;
    Long candidateId;
    String candidateName;
    String candidateAvatar;
    String designation; // Job title/position
    CandidateInfo candidateInfo;
    JobInfo jobInfo;
    LocalDateTime appliedAt;
    String appliedTimeAgo;
    String applicationStatus; // PENDING, REVIEWED, ACCEPTED, REJECTED
    String coverLetter;
    String resumeFilename;
}