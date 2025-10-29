package com.popcorn.jrp.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jobs")
@Getter
@Setter
public class JobEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    private EmployerEntity employer;

    private String jobTitle;

    @Column(columnDefinition = "JSON")
    private String skills; // JSON

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "JSON")
    private String jobType; // JSON

    private String level;

    @Column(columnDefinition = "JSON")
    private String responsibilities; // JSON

    @Column(columnDefinition = "JSON")
    private String skillAndExperiences; // JSON

    private String salary;
    private Integer experience;

    @Column(precision = 12, scale = 2)
    private BigDecimal minSalary;

    @Column(precision = 12, scale = 2)
    private BigDecimal maxSalary;

    private String currency;

    private Boolean negotiable;

    private LocalDate workTimeFrom;
    private LocalDate workTimeTo;

    private String industry;

    private Integer quantity;

    private String country;
    private String city;
    private String location;
    private LocalDate expirationDate;

    private Boolean status;
    private Boolean isDeleted;

    @ManyToMany
    @JoinTable(name = "job_skills", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<SkillEntity> skills;

    @ManyToMany
    @JoinTable(name = "job_job_type", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "job_type_id"))
    private List<JobTypeEntity> jobTypes;

    @Column(columnDefinition = "TEXT")
    private String createdBy; // JSON {userId, email}

    @Column(columnDefinition = "TEXT")
    private String updatedBy;

    @Column(columnDefinition = "TEXT")
    private String deletedBy;
}
