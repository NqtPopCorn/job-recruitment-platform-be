package com.popcorn.jrp.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscription_upgrade_history")
@Getter
@Setter
public class SubscriptionUpgradeHistoryEntity extends BaseEntity {

    @Column(name = "subscription_id", nullable = false)
    private Long subscriptionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_type", nullable = false)
    private SubscriptionType subscriptionType;

    @Column(name = "old_package_id", nullable = false)
    private Long oldPackageId;

    @Column(name = "new_package_id", nullable = false)
    private Long newPackageId;

    @Column(name = "refund_percent", precision = 5, scale = 2)
    private BigDecimal refundPercent;

    @Column(name = "refund_value", precision = 10, scale = 2)
    private BigDecimal refundValue;

    @Column(name = "final_price", precision = 10, scale = 2)
    private BigDecimal finalPrice;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public enum SubscriptionType {
        EMPLOYER, CANDIDATE
    }

    @Override
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}