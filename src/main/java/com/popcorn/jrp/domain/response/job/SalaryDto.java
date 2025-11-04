package com.popcorn.jrp.domain.response.job;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SalaryDto {
    @Min(value = 0, message = "Lương tối thiểu không được âm")
    private Double min;

    @Min(value = 0, message = "Lương tối đa không được âm")
    private Double max;

    private String currency;
    private Boolean negotiable;
}