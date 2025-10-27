package com.popcorn.jrp.domain.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "jobs")
@Getter
@Setter
public class    JobEntity extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    private EmployerEntity employer;

    @Column(columnDefinition = "TEXT")
    private String skills; // JSON lưu dưới dạng String

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String jobType; // JSON

    private String level;

    @Column(columnDefinition = "TEXT")
    private String responsibilities; // JSON

    @Column(columnDefinition = "TEXT")
    private String skillAndExperiences; // JSON

    private Integer experience;
    private String industry;
    private Integer quantity;
    private String country;
    private String city;
    private String location;
    private LocalDate expirationDate;
    private Boolean status;
    private Boolean isDeleted;
}

