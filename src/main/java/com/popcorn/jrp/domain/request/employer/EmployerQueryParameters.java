package com.popcorn.jrp.domain.request.employer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmployerQueryParameters {

    private int page = 1;
    private int size = 10;
    private String sort;
    private String search;
    private String location;
    private String primaryIndustry;
    private int foundationDateMin = 1900;
    private int foundationDateMax = 2025;
}
