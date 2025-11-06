package com.popcorn.jrp.domain.entity;

import com.popcorn.jrp.helper.JsonListStringConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobEntity extends BaseEntity {

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "employer_id", nullable = true)
        private EmployerEntity employer;

        @Column(columnDefinition = "TEXT")
        private String name;

        @Column(columnDefinition = "TEXT")
        private String description;

        private String level;

        @Column(columnDefinition = "JSON")
        @Convert(converter = JsonListStringConverter.class)
        private List<String> responsibilities; // JSON

        @Column(columnDefinition = "JSON")
        @Convert(converter = JsonListStringConverter.class)
        private List<String> skillAndExperiences; // JSON

        private Integer experience;

        @Column(precision = 12, scale = 2)
        private BigDecimal minSalary;
        @Column(precision = 12, scale = 2)
        private BigDecimal maxSalary;
        private String currency;
        private Boolean negotiable;

        private String workTimeFrom;
        private String workTimeTo;

        private String industry;
        private Integer quantity;
        private String country;
        private String city;
        private String location;
        private LocalDate expirationDate;

        @Column(nullable = false)
        private boolean status;

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
