package com.popcorn.jrp.domain.request.service;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PurchasePackageRequest {
    @NotNull(message = "Package ID is required")
    private Long packageId;
}