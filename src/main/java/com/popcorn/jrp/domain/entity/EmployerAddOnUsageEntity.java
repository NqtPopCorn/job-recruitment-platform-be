package com.popcorn.jrp.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employer_addon_usage")
@Getter
@Setter
public class EmployerAddOnUsageEntity extends BaseEntity {

    @Column(name = "employer_id", nullable = false)
    private Long employerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addon_package_id", nullable = false)
    private EmployerAddOnPackageEntity addOnPackage;

    @Column(name = "used_count", nullable = false)
    private Integer usedCount = 0;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public enum Status {
        ACTIVE, EXPIRED
    }

    @Override
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}