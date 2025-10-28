package com.popcorn.jrp.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "employer_service_packages")
@Getter
@Setter
public class EmployerServicePackageEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "duration_day")
    private Integer durationDay; // NULL nếu là gói vĩnh viễn

    @Column(nullable = false)
    private Boolean isLifetime = false;

    @Column(nullable = false)
    private Integer jobPostLimit;

    @Column(nullable = false)
    private Integer highlightJobLimit;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isLifetime) {
            durationDay = null;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (isLifetime) {
            durationDay = null;
        }
    }
}
