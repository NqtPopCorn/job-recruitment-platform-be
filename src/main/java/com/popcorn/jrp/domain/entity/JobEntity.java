package com.popcorn.jrp.domain.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "jobs")
@Getter
@Setter
public class JobEntity extends BaseEntity{
    @ManyToOne
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

    @Column(columnDefinition = "JSON")
    private String workTime; // JSON

    private String salary;
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

