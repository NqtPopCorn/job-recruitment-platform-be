package com.popcorn.jrp.domain.response.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkTimeResponse {
    private String from;
    private String to;
}
