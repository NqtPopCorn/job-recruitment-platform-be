package com.popcorn.jrp.domain.response.job;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryDto {
    @Min(value = 0, message = "Lương tối thiểu không được âm")
    private BigDecimal min;

    @Min(value = 0, message = "Lương tối đa không được âm")
    private BigDecimal max;

    private String currency;

    private boolean negotiable;
}