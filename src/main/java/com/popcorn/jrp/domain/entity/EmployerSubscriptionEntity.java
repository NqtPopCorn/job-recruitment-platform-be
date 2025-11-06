package com.popcorn.jrp.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employer_subscriptions")
@Getter
@Setter
public class EmployerSubscriptionEntity extends BaseEntity {

    @Column(name = "employer_id", nullable = false)
    private Long employerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private EmployerServicePackageEntity servicePackage;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(nullable = false)
    private Boolean isLifetime = false;

    @Column(name = "used_job_count", nullable = false)
    private Integer usedJobCount = 0;

    @Column(name = "used_highlight_count", nullable = false)
    private Integer usedHighlightCount = 0;

    @Column(name = "total_job_count", nullable = false)
    private Integer totalJobCount;

    @Column(name = "total_highlight_count", nullable = false)
    private Integer totalHighlightCount;

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
        super.onCreate();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}