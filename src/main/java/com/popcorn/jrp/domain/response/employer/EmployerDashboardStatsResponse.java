package com.popcorn.jrp.domain.response.employer;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class EmployerDashboardStatsResponse {
    Long postedJobs;
    Long applications;
    Long messages;
    Long shortlist;
}
