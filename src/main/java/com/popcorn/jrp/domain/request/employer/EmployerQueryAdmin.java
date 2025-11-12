package com.popcorn.jrp.domain.request.employer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployerQueryAdmin {
    private String search;
    private String location;
    private String primaryIndustry;
    private int foundationDateMin = 1900;
    private int foundationDateMax = 2025;
    private Boolean status;
}
