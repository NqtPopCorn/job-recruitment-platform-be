package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.request.service.*;
import com.popcorn.jrp.domain.response.ApiResponse;
import com.popcorn.jrp.domain.response.service.*;
import com.popcorn.jrp.security.RequireRole;
import com.popcorn.jrp.service.CandidateSubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candidate/subscriptions")
@RequiredArgsConstructor
public class CandidateSubscriptionController {

    private final CandidateSubscriptionService subscriptionService;

    // ========================================
    // PACKAGE ENDPOINTS
    // ========================================

    @GetMapping("/packages")
    public ResponseEntity<List<CandidatePackageResponse>> getAllPackages() {
        return ResponseEntity.ok(subscriptionService.getAllPackages());
    }

    @GetMapping("/packages/{packageId}")
    public ResponseEntity<CandidatePackageResponse> getPackageById(@PathVariable Long packageId) {
        return ResponseEntity.ok(subscriptionService.getPackageById(packageId));
    }

    @PostMapping("/packages")
    @RequireRole({"admin"})
    public ResponseEntity<CandidatePackageResponse> createPackage(
            @Valid @RequestBody CreateCandidatePackageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subscriptionService.createPackage(request));
    }

    // ========================================
    // SUBSCRIPTION ENDPOINTS
    // ========================================

    @PostMapping("/purchase")
    @RequireRole({"candidate"})
    public ResponseEntity<CandidateSubscriptionResponse> purchasePackage(
            Authentication authentication,
            @Valid @RequestBody PurchasePackageRequest request) {
        Long candidateId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(subscriptionService.purchasePackage(candidateId, request));
    }

    @GetMapping("/active")
    @RequireRole({"candidate"})
    public ResponseEntity<CandidateSubscriptionResponse> getActiveSubscription(
            Authentication authentication) {
        System.out.println(123);
        Long candidateId = Long.parseLong(authentication.getName());
        System.out.println(candidateId);
        return ResponseEntity.ok(subscriptionService.getActiveSubscription(candidateId));
    }

    @GetMapping("/history")
    @RequireRole({"candidate"})
    public ResponseEntity<List<CandidateSubscriptionResponse>> getSubscriptionHistory(
            Authentication authentication) {
        Long candidateId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(subscriptionService.getSubscriptionHistory(candidateId));
    }

    @GetMapping("/usage")
    @RequireRole({"candidate"})
    public ResponseEntity<SubscriptionUsageResponse> checkUsage(Authentication authentication) {
        Long candidateId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(subscriptionService.checkUsage(candidateId));
    }

    @GetMapping("/can-view-others")
    @RequireRole({"candidate"})
    public ResponseEntity<ApiResponse> canViewOtherCandidates(Authentication authentication) {
        Long candidateId = Long.parseLong(authentication.getName());
        boolean canView = subscriptionService.canViewOtherCandidates(candidateId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(canView)
                        .statusCode(HttpStatus.OK.value())
                        .message(canView ? "You can view other candidates" : "Upgrade to view other candidates")
                        .build()
        );
    }

    // ========================================
    // UPGRADE ENDPOINTS
    // ========================================

    @PostMapping("/calculate-upgrade")
    @RequireRole({"candidate"})
    public ResponseEntity<UpgradeCalculationResponse> calculateUpgrade(
            Authentication authentication,
            @Valid @RequestBody UpgradePackageRequest request) {
        Long candidateId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(subscriptionService.calculateUpgrade(candidateId, request.getNewPackageId()));
    }

    @PostMapping("/upgrade")
    @RequireRole({"candidate"})
    public ResponseEntity<CandidateSubscriptionResponse> upgradePackage(
            Authentication authentication,
            @Valid @RequestBody UpgradePackageRequest request) {
        Long candidateId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(subscriptionService.upgradePackage(candidateId, request));
    }
}