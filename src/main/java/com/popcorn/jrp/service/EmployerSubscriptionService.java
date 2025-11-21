package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.request.service.*;
import com.popcorn.jrp.domain.response.service.*;

import java.util.List;

public interface EmployerSubscriptionService {
    EmployerPackageResponse createPackage(CreateEmployerPackageRequest request);
    public EmployerPackageResponse updatePackage(Long packageId, UpdateEmployerPackageRequest request);
    public void deletePackage(Long packageId);
    public AddOnResponse updateAddOn(Long addOnId, UpdateAddOnRequest request);
    public void deleteAddOn(Long addOnId);
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
