package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.request.service.CreateAddOnRequest;
import com.popcorn.jrp.domain.request.service.CreateEmployerPackageRequest;
import com.popcorn.jrp.domain.request.service.PurchasePackageRequest;
import com.popcorn.jrp.domain.request.service.UpgradePackageRequest;
import com.popcorn.jrp.domain.response.service.*;

import java.util.List;

public interface EmployerSubscriptionService {
    EmployerPackageResponse createPackage(CreateEmployerPackageRequest request);
    List<EmployerPackageResponse> getAllPackages();
    EmployerPackageResponse getPackageById(Long packageId);
    EmployerSubscriptionResponse purchasePackage(Long employerId, PurchasePackageRequest request);
    UpgradeCalculationResponse calculateUpgrade(Long employerId, Long newPackageId);
    EmployerSubscriptionResponse upgradePackage(Long employerId, UpgradePackageRequest request);
    void useJobPost(Long employerId);
    void useHighlight(Long employerId);
    AddOnResponse createAddOn(CreateAddOnRequest request);
    void purchaseAddOn(Long employerId, Long addOnId);
    EmployerSubscriptionResponse getActiveSubscription(Long employerId);
    List<EmployerSubscriptionResponse> getSubscriptionHistory(Long employerId);
    SubscriptionUsageResponse checkUsage(Long employerId);
}
