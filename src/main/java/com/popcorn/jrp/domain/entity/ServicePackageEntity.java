package com.popcorn.jrp.domain.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_packages")
@Data
public class ServicePackageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

