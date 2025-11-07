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

        @OneToMany(mappedBy = "job")
        private List<ApplicationEntity> applications;

        @Column(columnDefinition = "TEXT")
        private String title;

        @Column(columnDefinition = "TEXT")
        private String description;

        private String level;

        @Column(columnDefinition = "JSON")
        @Convert(converter = JsonListStringConverter.class)
        private List<String> responsibilities; // JSON

        @Column(columnDefinition = "JSON")
        @Convert(converter = JsonListStringConverter.class)
        private List<String> skillAndExperiences; // JSON

        private int experience;

        @Column(precision = 12, scale = 2)
        private BigDecimal minSalary;
        @Column(precision = 12, scale = 2)
        private BigDecimal maxSalary;
        private String currency;
        private Boolean negotiable;

        private String workTimeFrom;
        private String workTimeTo;

        private String industry;
        private int quantity;
        private String country;
        private String city;
        private String location;
        private LocalDateTime expirationDate;

        @Column(nullable = false)
        private Boolean status;

        @Convert(converter = JsonListStringConverter.class)
        @Column(columnDefinition = "JSON")
        private List<String> skills;

        @Convert(converter = JsonListStringConverter.class)
        @Column(columnDefinition = "JSON")
        private List<String> jobTypes;

        @Column(columnDefinition = "JSON")
        private String createdBy; // JSON {userId, email}

        @Column(columnDefinition = "JSON")
        private String updatedBy;

        @Column(columnDefinition = "JSON")
        private String deletedBy;

}
