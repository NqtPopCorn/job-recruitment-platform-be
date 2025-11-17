package com.popcorn.jrp.domain.response.candidate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateStatisticsResponse {

    Long appliedJobsCount;

    Long jobAlertsCount;

    Long messagesCount;

    Long shortlistCount;
}
