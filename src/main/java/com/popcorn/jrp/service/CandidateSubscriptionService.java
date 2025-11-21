package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.request.service.*;
import com.popcorn.jrp.domain.response.service.*;

import java.util.List;

public interface CandidateSubscriptionService {
    CandidatePackageResponse createPackage(CreateCandidatePackageRequest request);
    public CandidatePackageResponse updatePackage(Long packageId, UpdateCandidatePackageRequest request);
    public void deletePackage(Long packageId);
    List<CandidatePackageResponse> getAllPackages();
    CandidatePackageResponse getPackageById(Long packageId);
    CandidateSubscriptionResponse purchasePackage(Long candidateId, PurchasePackageRequest request);
    UpgradeCalculationResponse calculateUpgrade(Long candidateId, Long newPackageId);
    CandidateSubscriptionResponse upgradePackage(Long candidateId, UpgradePackageRequest request);
    void useJobApply(Long candidateId);
    void useHighlightDay(Long candidateId);
    boolean canViewOtherCandidates(Long candidateId);
    SubscriptionUsageResponse checkUsage(Long candidateId);
    CandidateSubscriptionResponse getActiveSubscription(Long candidateId);
    List<CandidateSubscriptionResponse> getSubscriptionHistory(Long candidateId);
}
