package com.popcorn.jrp.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidate_subscriptions")
@Getter
@Setter
public class CandidateSubscriptionEntity extends BaseEntity {

    @Column(name = "candidate_id", nullable = false)
    private Long candidateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private CandidateServicePackageEntity servicePackage;

    @Column(name = "subscription_code", length = 50)
    private String subscriptionCode;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(nullable = false)
    private Boolean isLifetime = false;

    @Column(name = "used_apply_count", nullable = false)
    private Integer usedApplyCount = 0;

    @Column(name = "used_highlight_days", nullable = false)
    private Integer usedHighlightDays = 0;

    @Column(name = "total_apply_limit", nullable = false)
    private Integer totalApplyLimit;

    @Column(name = "total_highlight_days", nullable = false)
    private Integer totalHighlightDays;

    @Column(name = "renew_count", nullable = false)
    private Integer renewCount = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum SubscriptionStatus {
        ACTIVE, EXPIRED, SUSPENDED, QUEUED
    }

    @Override
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}