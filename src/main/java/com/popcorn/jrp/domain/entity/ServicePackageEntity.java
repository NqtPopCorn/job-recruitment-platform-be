package com.popcorn.jrp.domain.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_packages")
@Getter
@Setter
public class ServicePackageEntity extends BaseEntity {

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal price;
    private Integer durationDays;

    @Enumerated(EnumType.STRING)
    private Target target;

    private LocalDateTime createdAt;

    public enum Target {
        CANDIDATE, EMPLOYER
    }
}

