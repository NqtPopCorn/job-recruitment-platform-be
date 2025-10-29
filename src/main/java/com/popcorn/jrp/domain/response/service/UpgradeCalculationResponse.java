package com.popcorn.jrp.domain.response.service;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class UpgradeCalculationResponse {
    private String oldPackageName;
    private String newPackageName;
    private BigDecimal oldPackagePrice;
    private BigDecimal newPackagePrice;
    private BigDecimal refundPercent;
    private BigDecimal refundValue;
    private BigDecimal finalPrice;
    private String message;
}