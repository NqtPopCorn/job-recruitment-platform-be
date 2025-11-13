package com.popcorn.jrp.domain.response.candidate;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateStatusStatisticResponse {
    private long total;
    private long activeCount;
    private long lockedCount;
}