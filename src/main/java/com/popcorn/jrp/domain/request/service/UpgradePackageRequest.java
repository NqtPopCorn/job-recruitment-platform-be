package com.popcorn.jrp.domain.request.service;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpgradePackageRequest {
    @NotNull(message = "New package ID is required")
    private Long newPackageId;
}