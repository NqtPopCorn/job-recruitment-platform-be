package com.popcorn.jrp.domain.response.employer;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobInfo {
    Long jobId;
    String jobTitle;
    String location;
}
