package com.popcorn.jrp.service.impl;

import com.popcorn.jrp.domain.entity.*;
import com.popcorn.jrp.domain.entity.EmployerSubscriptionEntity.SubscriptionStatus;
import com.popcorn.jrp.domain.entity.EmployerSubscriptionEntity;
import com.popcorn.jrp.domain.request.service.*;
import com.popcorn.jrp.domain.response.service.*;
import com.popcorn.jrp.domain.response.service.SubscriptionUsageResponse;
import com.popcorn.jrp.exception.CustomException;
import com.popcorn.jrp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployerSubscriptionService implements com.popcorn.jrp.service.EmployerSubscriptionService {

    private final EmployerServicePackageRepository packageRepository;
    private final EmployerSubscriptionRepository subscriptionRepository;
    private final EmployerAddOnPackageRepository addOnPackageRepository;
    private final EmployerAddOnUsageRepository addOnUsageRepository;
    private final SubscriptionUpgradeHistoryRepository upgradeHistoryRepository;

    // ========================================
    // PACKAGE MANAGEMENT
    // ========================================

    @Transactional
    public EmployerPackageResponse createPackage(CreateEmployerPackageRequest request) {
        EmployerServicePackageEntity packageEntity = new EmployerServicePackageEntity();
        packageEntity.setName(request.getName());
        packageEntity.setDescription(request.getDescription());
        packageEntity.setPrice(request.getPrice());
        packageEntity.setDurationDay(request.getDurationDay());
        packageEntity.setIsLifetime(request.getIsLifetime());
        packageEntity.setJobPostLimit(request.getJobPostLimit());
        packageEntity.setHighlightJobLimit(request.getHighlightJobLimit());

        packageEntity = packageRepository.save(packageEntity);
        return mapToPackageResponse(packageEntity);
    }

    public List<EmployerPackageResponse> getAllPackages() {
        return packageRepository.findAllByOrderByPriceAsc().stream()
                .map(this::mapToPackageResponse)
                .collect(Collectors.toList());
    }

    public EmployerPackageResponse getPackageById(Long packageId) {
        EmployerServicePackageEntity packageEntity = packageRepository.findById(packageId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Package not found"));
        return mapToPackageResponse(packageEntity);
    }

    // ========================================
    // SUBSCRIPTION PURCHASE
    // ========================================

    @Transactional
    public EmployerSubscriptionResponse purchasePackage(Long employerId, PurchasePackageRequest request) {
        // Kiểm tra gói có tồn tại
        EmployerServicePackageEntity packageEntity = packageRepository.findById(request.getPackageId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Package not found"));

        // Kiểm tra subscription hiện tại
        subscriptionRepository.findActiveSubscription(employerId).ifPresent(sub -> {
            throw new CustomException(HttpStatus.BAD_REQUEST,
                    "You already have an active subscription. Please upgrade or wait for it to expire.");
        });

        // Tạo subscription mới
        EmployerSubscriptionEntity subscription = new EmployerSubscriptionEntity();
        subscription.setEmployerId(employerId);
        subscription.setServicePackage(packageEntity);
        subscription.setStartDate(LocalDate.now());
        subscription.setIsLifetime(packageEntity.getIsLifetime());

        if (packageEntity.getIsLifetime()) {
            subscription.setEndDate(null);
        } else {
            subscription.setEndDate(LocalDate.now().plusDays(packageEntity.getDurationDay()));
        }

        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setTotalJobCount(packageEntity.getJobPostLimit());
        subscription.setTotalHighlightCount(packageEntity.getHighlightJobLimit());
        subscription.setUsedJobCount(0);
        subscription.setUsedHighlightCount(0);
        subscription.setRenewCount(0);

        subscription = subscriptionRepository.save(subscription);
        return mapToSubscriptionResponse(subscription);
    }

    // ========================================
    // UPGRADE PACKAGE
    // ========================================

    @Transactional
    public UpgradeCalculationResponse calculateUpgrade(Long employerId, Long newPackageId) {
        // Lấy subscription hiện tại
        EmployerSubscriptionEntity currentSub = subscriptionRepository.findActiveSubscription(employerId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,
                        "No active subscription found"));

        // Lấy gói mới
        EmployerServicePackageEntity newPackage = packageRepository.findById(newPackageId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "New package not found"));

        EmployerServicePackageEntity oldPackage = currentSub.getServicePackage();

        // Kiểm tra gói lifetime
        if (currentSub.getIsLifetime()) {
            throw new CustomException(HttpStatus.BAD_REQUEST,
                    "Cannot upgrade from lifetime package");
        }

        // Tính toán refund
        BigDecimal refundPercent = calculateRefundPercent(currentSub);
        BigDecimal refundValue = oldPackage.getPrice().multiply(refundPercent);
        BigDecimal finalPrice = newPackage.getPrice().subtract(refundValue);

        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
            finalPrice = BigDecimal.ZERO;
        }

        return UpgradeCalculationResponse.builder()
                .oldPackageName(oldPackage.getName())
                .newPackageName(newPackage.getName())
                .oldPackagePrice(oldPackage.getPrice())
                .newPackagePrice(newPackage.getPrice())
                .refundPercent(refundPercent.multiply(new BigDecimal("100")))
                .refundValue(refundValue)
                .finalPrice(finalPrice)
                .message("You will be refunded " + refundPercent.multiply(new BigDecimal("100")) +
                        "% of your current package")
                .build();
    }

    @Transactional
    public EmployerSubscriptionResponse upgradePackage(Long employerId, UpgradePackageRequest request) {
        // Tính toán chi phí
        UpgradeCalculationResponse calculation = calculateUpgrade(employerId, request.getNewPackageId());

        // Lấy subscription hiện tại
        EmployerSubscriptionEntity currentSub = subscriptionRepository.findActiveSubscription(employerId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,
                        "No active subscription found"));

        // Lấy gói mới
        EmployerServicePackageEntity newPackage = packageRepository.findById(request.getNewPackageId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "New package not found"));

        // Lưu lịch sử upgrade
        SubscriptionUpgradeHistoryEntity history = new SubscriptionUpgradeHistoryEntity();
        history.setSubscriptionId(currentSub.getId());
        history.setSubscriptionType(SubscriptionUpgradeHistoryEntity.SubscriptionType.EMPLOYER);
        history.setOldPackageId(currentSub.getServicePackage().getId());
        history.setNewPackageId(newPackage.getId());
        history.setRefundPercent(calculation.getRefundPercent());
        history.setRefundValue(calculation.getRefundValue());
        history.setFinalPrice(calculation.getFinalPrice());
        upgradeHistoryRepository.save(history);

        // Cập nhật subscription
        currentSub.setServicePackage(newPackage);
        currentSub.setStartDate(LocalDate.now());
        currentSub.setIsLifetime(newPackage.getIsLifetime());

        if (newPackage.getIsLifetime()) {
            currentSub.setEndDate(null);
        } else {
            currentSub.setEndDate(LocalDate.now().plusDays(newPackage.getDurationDay()));
        }

        currentSub.setTotalJobCount(newPackage.getJobPostLimit());
        currentSub.setTotalHighlightCount(newPackage.getHighlightJobLimit());
        currentSub.setUsedJobCount(0);
        currentSub.setUsedHighlightCount(0);

        currentSub = subscriptionRepository.save(currentSub);
        return mapToSubscriptionResponse(currentSub);
    }

    private BigDecimal calculateRefundPercent(EmployerSubscriptionEntity subscription) {
        EmployerServicePackageEntity pkg = subscription.getServicePackage();

        // Tính % job còn lại
        BigDecimal remainingJobPercent = BigDecimal.ONE.subtract(
                new BigDecimal(subscription.getUsedJobCount())
                        .divide(new BigDecimal(pkg.getJobPostLimit()), 4, RoundingMode.HALF_UP)
        );

        // Tính % highlight còn lại
        BigDecimal remainingHighlightPercent = BigDecimal.ONE.subtract(
                new BigDecimal(subscription.getUsedHighlightCount())
                        .divide(new BigDecimal(pkg.getHighlightJobLimit()), 4, RoundingMode.HALF_UP)
        );

        // Tính % thời gian còn lại
        long totalDays = ChronoUnit.DAYS.between(subscription.getStartDate(), subscription.getEndDate());
        long remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), subscription.getEndDate());

        if (remainingDays < 0) remainingDays = 0;

        BigDecimal remainingTimePercent = new BigDecimal(remainingDays)
                .divide(new BigDecimal(totalDays), 4, RoundingMode.HALF_UP);

        // Trung bình 3 tỷ lệ
        return remainingJobPercent.add(remainingHighlightPercent).add(remainingTimePercent)
                .divide(new BigDecimal("3"), 4, RoundingMode.HALF_UP);
    }

    // ========================================
    // USAGE TRACKING
    // ========================================

    @Transactional
    public void useJobPost(Long employerId) {
        EmployerSubscriptionEntity subscription = subscriptionRepository.findActiveSubscription(employerId)
                .orElseThrow(() -> new CustomException(HttpStatus.FORBIDDEN,
                        "No active subscription found"));

        // Kiểm tra hết hạn
        if (!subscription.getIsLifetime() && subscription.getEndDate().isBefore(LocalDate.now())) {
            subscription.setStatus(SubscriptionStatus.EXPIRED);
            subscriptionRepository.save(subscription);
            throw new CustomException(HttpStatus.FORBIDDEN, "Your subscription has expired");
        }

        // Kiểm tra quota
        if (subscription.getUsedJobCount() >= subscription.getTotalJobCount()) {
            throw new CustomException(HttpStatus.FORBIDDEN,
                    "You have reached your job post limit. Please upgrade your package.");
        }

        subscription.setUsedJobCount(subscription.getUsedJobCount() + 1);
        subscriptionRepository.save(subscription);
    }

    @Transactional
    public void useHighlight(Long employerId) {
        EmployerSubscriptionEntity subscription = subscriptionRepository.findActiveSubscription(employerId)
                .orElseThrow(() -> new CustomException(HttpStatus.FORBIDDEN,
                        "No active subscription found"));

        // Kiểm tra hết hạn
        if (!subscription.getIsLifetime() && subscription.getEndDate().isBefore(LocalDate.now())) {
            subscription.setStatus(SubscriptionStatus.EXPIRED);
            subscriptionRepository.save(subscription);
            throw new CustomException(HttpStatus.FORBIDDEN, "Your subscription has expired");
        }

        // Kiểm tra quota
        if (subscription.getUsedHighlightCount() >= subscription.getTotalHighlightCount()) {
            throw new CustomException(HttpStatus.FORBIDDEN,
                    "You have reached your highlight limit. Please upgrade or buy add-on.");
        }

        subscription.setUsedHighlightCount(subscription.getUsedHighlightCount() + 1);
        subscriptionRepository.save(subscription);
    }

    public SubscriptionUsageResponse checkUsage(Long employerId) {
        EmployerSubscriptionEntity subscription = subscriptionRepository.findActiveSubscription(employerId)
                .orElse(null);

        if (subscription == null) {
            return SubscriptionUsageResponse.builder()
                    .canPostJob(false)
                    .canHighlightJob(false)
                    .remainingJobPosts(0)
                    .remainingHighlights(0)
                    .message("No active subscription")
                    .build();
        }

        boolean isValid = subscription.getIsLifetime() ||
                subscription.getEndDate().isAfter(LocalDate.now());

        int remainingJobs = subscription.getTotalJobCount() - subscription.getUsedJobCount();
        int remainingHighlights = subscription.getTotalHighlightCount() - subscription.getUsedHighlightCount();

        return SubscriptionUsageResponse.builder()
                .canPostJob(isValid && remainingJobs > 0)
                .canHighlightJob(isValid && remainingHighlights > 0)
                .remainingJobPosts(Math.max(0, remainingJobs))
                .remainingHighlights(Math.max(0, remainingHighlights))
                .message("Subscription active")
                .build();
    }

    // ========================================
    // ADD-ON MANAGEMENT
    // ========================================

    @Transactional
    public AddOnResponse createAddOn(CreateAddOnRequest request) {
        EmployerAddOnPackageEntity addOn = new EmployerAddOnPackageEntity();
        addOn.setName(request.getName());
        addOn.setDescription(request.getDescription());
        addOn.setPrice(request.getPrice());
        addOn.setType(EmployerAddOnPackageEntity.AddOnType.valueOf(request.getType()));
        addOn.setQuantity(request.getQuantity());
        addOn.setDurationDay(request.getDurationDay());
        addOn.setIsLifetime(request.getIsLifetime());

        addOn = addOnPackageRepository.save(addOn);
        return mapToAddOnResponse(addOn);
    }

    @Transactional
    public void purchaseAddOn(Long employerId, Long addOnId) {
        EmployerAddOnPackageEntity addOn = addOnPackageRepository.findById(addOnId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Add-on not found"));

        EmployerAddOnUsageEntity usage = new EmployerAddOnUsageEntity();
        usage.setEmployerId(employerId);
        usage.setAddOnPackage(addOn);
        usage.setUsedCount(0);
        usage.setStartDate(LocalDate.now());
        usage.setStatus(EmployerAddOnUsageEntity.Status.ACTIVE);

        if (addOn.getIsLifetime()) {
            usage.setEndDate(null);
        } else {
            usage.setEndDate(LocalDate.now().plusDays(addOn.getDurationDay()));
        }

        addOnUsageRepository.save(usage);

        // Cộng quota vào subscription chính
        EmployerSubscriptionEntity subscription = subscriptionRepository.findActiveSubscription(employerId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,
                        "No active subscription found"));

        switch (addOn.getType()) {
            case JOB_POST:
                subscription.setTotalJobCount(subscription.getTotalJobCount() + addOn.getQuantity());
                break;
            case HIGHLIGHT:
                subscription.setTotalHighlightCount(subscription.getTotalHighlightCount() + addOn.getQuantity());
                break;
        }

        subscriptionRepository.save(subscription);
    }

    // ========================================
    // GET SUBSCRIPTION INFO
    // ========================================

    public EmployerSubscriptionResponse getActiveSubscription(Long employerId) {
        EmployerSubscriptionEntity subscription = subscriptionRepository.findActiveSubscription(employerId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,
                        "No active subscription found"));
        return mapToSubscriptionResponse(subscription);
    }

    public List<EmployerSubscriptionResponse> getSubscriptionHistory(Long employerId) {
        return subscriptionRepository.findByEmployerId(employerId).stream()
                .map(this::mapToSubscriptionResponse)
                .collect(Collectors.toList());
    }

    // ========================================
    // MAPPERS
    // ========================================

    private EmployerPackageResponse mapToPackageResponse(EmployerServicePackageEntity entity) {
        EmployerPackageResponse response = new EmployerPackageResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setPrice(entity.getPrice());
        response.setDurationDay(entity.getDurationDay());
        response.setIsLifetime(entity.getIsLifetime());
        response.setJobPostLimit(entity.getJobPostLimit());
        response.setHighlightJobLimit(entity.getHighlightJobLimit());
        return response;
    }

    private EmployerSubscriptionResponse mapToSubscriptionResponse(EmployerSubscriptionEntity entity) {
        EmployerSubscriptionResponse response = new EmployerSubscriptionResponse();
        response.setId(entity.getId());
        response.setEmployerId(entity.getEmployerId());
        response.setPackageInfo(mapToPackageResponse(entity.getServicePackage()));
        response.setStartDate(entity.getStartDate());
        response.setEndDate(entity.getEndDate());
        response.setStatus(entity.getStatus().name());
        response.setIsLifetime(entity.getIsLifetime());
        response.setUsedJobCount(entity.getUsedJobCount());
        response.setUsedHighlightCount(entity.getUsedHighlightCount());
        response.setTotalJobCount(entity.getTotalJobCount());
        response.setTotalHighlightCount(entity.getTotalHighlightCount());
        response.setRemainingJobCount(entity.getTotalJobCount() - entity.getUsedJobCount());
        response.setRemainingHighlightCount(entity.getTotalHighlightCount() - entity.getUsedHighlightCount());
        response.setRenewCount(entity.getRenewCount());
        return response;
    }

    private AddOnResponse mapToAddOnResponse(EmployerAddOnPackageEntity entity) {
        AddOnResponse response = new AddOnResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setPrice(entity.getPrice());
        response.setType(entity.getType().name());
        response.setQuantity(entity.getQuantity());
        response.setDurationDay(entity.getDurationDay());
        response.setIsLifetime(entity.getIsLifetime());
        return response;
    }
}