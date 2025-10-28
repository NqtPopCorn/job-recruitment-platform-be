package com.popcorn.jrp.domain.response.employer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JobSalaryDto {
    private double min;
    private double max;
    private String currency;
    private boolean negotiable;
}
