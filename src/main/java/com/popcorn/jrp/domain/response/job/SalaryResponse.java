package com.popcorn.jrp.domain.response.job;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryResponse {
    private BigDecimal min;
    private BigDecimal max;
    private String currency;
    private String unit;
    private boolean negotiable;
}
