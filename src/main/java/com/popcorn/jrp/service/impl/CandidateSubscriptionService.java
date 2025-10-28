//package com.popcorn.jrp.service.impl;
//
//import com.popcorn.jrp.domain.entity.*;
//import com.popcorn.jrp.domain.entity.CandidateSubscriptionEntity.SubscriptionStatus;
//import com.popcorn.jrp.domain.request.service.*;
//import com.popcorn.jrp.domain.response.service.*;
//import com.popcorn.jrp.exception.CustomException;
//import com.popcorn.jrp.repository.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.time.LocalDate;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class CandidateSubscriptionService {
//
//    private final CandidateServicePackageRepository packageRepository;
//    private final CandidateSubscriptionRepository subscriptionRepository;
//    private final SubscriptionUpgradeHistoryRepository upgradeHistoryRepository;
//
//    // ========================================
//    // PACKAGE MANAGEMENT
//    // ========================================
//
//    @Transactional
//    public CandidatePackageResponse createPackage(CreateCandidatePackageRequest request) {
//        CandidateServicePackageEntity packageEntity = new CandidateServicePackageEntity();
//        packageEntity.setName(request.getName());
//        packageEntity.setDescription(request.getDescription());
//        packageEntity.setPrice(request.getPrice());
//        packageEntity.setDurationDay(request.getDurationDay());
//        packageEntity.setIsLifetime(request.getIsLifetime());
//        packageEntity.setHighlightProfileDays(request.getHighlightProfileDays());
//        packageEntity.setJobApplyLimit(request.getJobApplyLimit());
//        packageEntity.setCanViewOtherCandidates(request.getCanViewOtherCandidates());
//
//        packageEntity = packageRepository.save(packageEntity);
//        return mapToPackageResponse(packageEntity);
//    }
//
//    public List<CandidatePackageResponse> getAllPackages() {
//        return packageRepository.findAllByOrderByPriceAsc().stream()
//                .map(this::mapToPackageResponse)
//                .collect(Collectors.toList());
//    }
//
//    public CandidatePackageResponse getPackageById(Long packageId) {
//        CandidateServicePackageEntity packageEntity = packageRepository.findById(packageId)
//                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Package not found"));
//        return mapToPackageResponse(packageEntity);
//    }
//
//    // ========================================
//    // SUBSCRIPTION PURCHASE
//    // ========================================
//
//    @Transactional
//    public CandidateSubscriptionResponse purchasePackage(Long candidateId, PurchasePackageRequest request) {
//        // Kiểm tra gói có tồn tại
//        CandidateServicePackageEntity packageEntity = packageRepository.findById(request.getPackageId())
//                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Package not found"));
//
//        // Kiểm tra subscription hiện tại
//        subscriptionRepository.findActiveSubscription(candidateId).ifPresent(sub -> {
//            throw new CustomException(HttpStatus.BAD_REQUEST,
//                    "You already have an active subscription. Please upgrade or wait for it to expire.");
//        });
//
//        // Tạo subscription code
//        String subscriptionCode = "SUB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
//
//        // Tạo subscription mới
//        CandidateSubscriptionEntity subscription = new CandidateSubscriptionEntity();
//        subscription.setCandidateId(candidateId);
//        subscription.setServicePackage(packageEntity);
//        subscription.setSubscriptionCode(subscriptionCode);
//        subscription.setStartDate(LocalDate.now());
//        subscription.setIsLifetime(packageEntity.getIsLifetime());
//
//        if (packageEntity.getIsLifetime()) {
//            subscription.setEndDate(null);
//        } else {
//            subscription.setEndDate(LocalDate.now().plusDays(packageEntity.getDurationDay()));
//        }
//
//        subscription.setStatus(SubscriptionStatus.ACTIVE);
//        subscription.setTotalApplyLimit(packageEntity.getJobApplyLimit());
//        subscription.setTotalHighlightDays(packageEntity.getHighlightProfileDays());
//        subscription.setUsedApplyCount(0);
//        subscription.setUsedHighlightDays(0);
//        subscription.setRenewCount(0);
//
//        subscription = subscriptionRepository.save(subscription);
//        return mapToSubscriptionResponse(subscription);
//    }
//
//    // ========================================
//    // UPGRADE PACKAGE
//    // ========================================
//
//    @Transactional
//    public UpgradeCalculationResponse calculateUpgrade(Long candidateId, Long newPackageId) {
//        // Lấy subscription hiện tại
//        CandidateSubscriptionEntity currentSub = subscriptionRepository.findActiveSubscription(candidateId)
//                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,
//                        "No active subscription found"));
//
//        // Lấy gói mới
//        CandidateServicePackageEntity newPackage = packageRepository.findById(newPackageId)
//                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "New package not found"));
//
//        CandidateServicePackageEntity oldPackage = currentSub.getServicePackage();
//
//        // Kiểm tra gói lifetime
//        if (currentSub.getIsLifetime()) {
//            throw new CustomException(HttpStatus.BAD_REQUEST,
//                    "Cannot upgrade from lifetime package");
//        }
//
//        // Tính toán refund
//        BigDecimal refundPercent = calculateRefundPercent(currentSub);
//        BigDecimal refundValue = oldPackage.getPrice().multiply(refundPercent);
//        BigDecimal finalPrice = newPackage.getPrice().subtract(refundValue);
//
//        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
//            finalPrice = BigDecimal.ZERO;
//        }
//
//        return UpgradeCalculationResponse.builder()
//                .oldPackageName(oldPackage.getName())
//                .newPackageName(newPackage.getName())
//                .oldPackagePrice(oldPackage.getPrice())
//                .newPackagePrice(newPackage.getPrice())
//                .refundPercent(refundPercent.multiply(new BigDecimal("100")))
//                .refundValue(refundValue)
//                .finalPrice(finalPrice)
//                .message("You will be refunded " + refundPercent.multiply(new BigDecimal("100")) +
//                        "% of your current package")
//                .build();
//    }
//
//    @Transactional
//    public CandidateSubscriptionResponse upgradePackage(Long candidateId, UpgradePackageRequest request) {
//        // Tính toán chi phí
//        UpgradeCalculationResponse calculation = calculateUpgrade(candidateId, request.getNewPackageId());
//
//        // Lấy subscription hiện tại
//        CandidateSubscriptionEntity currentSub = subscriptionRepository.findActiveSubscription(candidateId)
//                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,
//                        "No active subscription found"));
//
//        // Lấy gói mới
//        CandidateServicePackageEntity newPackage = packageRepository.findById(request.getNewPackageId())
//                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "New package not found"));
//
//        // Lưu lịch sử upgrade
//        SubscriptionUpgradeHistoryEntity history = new SubscriptionUpgradeHistoryEntity();
//        history.setSubscriptionId(currentSub.getId());
//        history.setSubscriptionType(SubscriptionUpgradeHistoryEntity.SubscriptionType.CANDIDATE);
//        history.setOldPackageId(currentSub.getServicePackage().getId());
//        history.setNewPackageId(newPackage.getId());
//        history.setRefundPercent(calculation.getRefundPercent());
//        history.setRefundValue(calculation.getRefundValue());
//        history.setFinalPrice(calculation.getFinalPrice());
//        upgradeHistoryRepository.save(history);
//
//        // Cập nhật subscription
//        currentSub.setServicePackage(newPackage);
//        currentSub.setStartDate(LocalDate.now());
//        currentSub.setIsLifetime(newPackage.getIsLifetime());
//
//        if (newPackage.getIsLifetime()) {
//            currentSub.setEndDate(null);
//        } else {
//            currentSub.setEndDate(LocalDate.now().plusDays(newPackage.getDurationDay()));
//        }
//
//        currentSub.setTotalApplyLimit(newPackage.getJobApplyLimit());
//        currentSub.setTotalHighlightDays(newPackage.getHighlightProfileDays());
//        currentSub.setUsedApplyCount(0);
//        currentSub.setUsedHighlightDays(0);
//
//        currentSub = subscriptionRepository.save(currentSub);
//        return mapToSubscriptionResponse(currentSub);
//    }
//
//    private BigDecimal calculateRefundPercent(CandidateSubscriptionEntity subscription) {
//        CandidateServicePackageEntity pkg = subscription.getServicePackage();
//
//        // Tính % ứng tuyển còn lại
//        BigDecimal remainingApplyPercent = BigDecimal.ONE.subtract(
//                new BigDecimal(subscription.getUsedApplyCount())
//                        .divide(new BigDecimal(pkg.getJobApplyLimit()), 4, RoundingMode.HALF_UP)
//        );
//
//        // Tính % highlight còn lại
//        BigDecimal remainingHighlightPercent = BigDecimal.ONE.subtract(
//                new BigDecimal(subscription.getUsedHighlightDays())
//                        .divide(new BigDecimal(pkg.getHighlightProfileDays()), 4, RoundingMode.HALF_UP)
//        );
//
//        // Tính % thời gian còn lại
//        long totalDays = ChronoUnit.DAYS.between(subscription.getStartDate(), subscription.getEndDate());
//        long remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), subscription.getEndDate());
//
//        if (remainingDays < 0) remainingDays = 0;
//
////        BigDecimal remainingTimePercent = new BigDecimal(remainingDays)
////                .divide(new BigDecimal(totalDays), 4, RoundingMode.HALF_UP);
////
////        // Trung bình 3 tỷ lệ
//    }
//}