package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.request.service.*;
import com.popcorn.jrp.domain.response.ApiResponse;
import com.popcorn.jrp.domain.response.service.*;
import com.popcorn.jrp.security.RequireRole;
import com.popcorn.jrp.service.EmployerSubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employer/subscriptions")
@RequiredArgsConstructor
public class EmployerSubscriptionController {

    private final EmployerSubscriptionService subscriptionService;

    // ========================================
    // PACKAGE ENDPOINTS
    // ========================================

    @GetMapping("/packages")
    public ResponseEntity<List<EmployerPackageResponse>> getAllPackages() {
        return ResponseEntity.ok(subscriptionService.getAllPackages());
    }

    @GetMapping("/packages/{packageId}")
    public ResponseEntity<EmployerPackageResponse> getPackageById(@PathVariable Long packageId) {
        return ResponseEntity.ok(subscriptionService.getPackageById(packageId));
    }

    @PostMapping("/packages")
    @RequireRole({"admin"})
    public ResponseEntity<EmployerPackageResponse> createPackage(
            @Valid @RequestBody CreateEmployerPackageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subscriptionService.createPackage(request));
    }

    @PutMapping("/packages/{packageId}")
    @RequireRole({"admin"})
    public ResponseEntity<EmployerPackageResponse> updatePackage(
            @PathVariable Long packageId,
            @Valid @RequestBody UpdateEmployerPackageRequest request) {
        return ResponseEntity.ok(subscriptionService.updatePackage(packageId, request));
    }

    @DeleteMapping("/packages/{packageId}")
    @RequireRole({"admin"})
    public ResponseEntity<ApiResponse> deletePackage(@PathVariable Long packageId) {
        subscriptionService.deletePackage(packageId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .statusCode(HttpStatus.OK.value())
                        .message("Package deleted successfully")
                        .build()
        );
    }

    @PutMapping("/addons/{addOnId}")
    @RequireRole({"admin"})
    public ResponseEntity<AddOnResponse> updateAddOn(
            @PathVariable Long addOnId,
            @Valid @RequestBody UpdateAddOnRequest request) {
        return ResponseEntity.ok(subscriptionService.updateAddOn(addOnId, request));
    }

    @DeleteMapping("/addons/{addOnId}")
    @RequireRole({"admin"})
    public ResponseEntity<ApiResponse> deleteAddOn(@PathVariable Long addOnId) {
        subscriptionService.deleteAddOn(addOnId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .statusCode(HttpStatus.OK.value())
                        .message("Add-on deleted successfully")
                        .build()
        );
    }

    // ========================================
    // SUBSCRIPTION ENDPOINTS
    // ========================================

    @PostMapping("/purchase")
    @RequireRole({"employer"})
    public ResponseEntity<EmployerSubscriptionResponse> purchasePackage(
            Authentication authentication,
            @Valid @RequestBody PurchasePackageRequest request) {
        Long employerId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(subscriptionService.purchasePackage(employerId, request));
    }

    @GetMapping("/active")
    @RequireRole({"employer"})
    public ResponseEntity<EmployerSubscriptionResponse> getActiveSubscription(
            Authentication authentication) {
        Long employerId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(subscriptionService.getActiveSubscription(employerId));
    }

    @GetMapping("/history")
    @RequireRole({"employer"})
    public ResponseEntity<List<EmployerSubscriptionResponse>> getSubscriptionHistory(
            Authentication authentication) {
        Long employerId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(subscriptionService.getSubscriptionHistory(employerId));
    }

    @GetMapping("/usage")
    @RequireRole({"employer"})
    public ResponseEntity<SubscriptionUsageResponse> checkUsage(Authentication authentication) {
        Long employerId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(subscriptionService.checkUsage(employerId));
    }

    // ========================================
    // UPGRADE ENDPOINTS
    // ========================================

    @PostMapping("/calculate-upgrade")
    @RequireRole({"employer"})
    public ResponseEntity<UpgradeCalculationResponse> calculateUpgrade(
            Authentication authentication,
            @Valid @RequestBody UpgradePackageRequest request) {
        Long employerId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(subscriptionService.calculateUpgrade(employerId, request.getNewPackageId()));
    }

    @PostMapping("/upgrade")
    @RequireRole({"employer"})
    public ResponseEntity<EmployerSubscriptionResponse> upgradePackage(
            Authentication authentication,
            @Valid @RequestBody UpgradePackageRequest request) {
        Long employerId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(subscriptionService.upgradePackage(employerId, request));
    }

    // ========================================
    // ADD-ON ENDPOINTS
    // ========================================

    @PostMapping("/addons")
    @RequireRole({"admin"})
    public ResponseEntity<AddOnResponse> createAddOn(
            @Valid @RequestBody CreateAddOnRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subscriptionService.createAddOn(request));
    }

    @PostMapping("/addons/{addOnId}/purchase")
    @RequireRole({"employer"})
    public ResponseEntity<ApiResponse> purchaseAddOn(
            Authentication authentication,
            @PathVariable Long addOnId) {
        Long employerId = Long.parseLong(authentication.getName());
        subscriptionService.purchaseAddOn(employerId, addOnId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .statusCode(HttpStatus.OK.value())
                        .message("Add-on purchased successfully")
                        .build()
        );
    }
}