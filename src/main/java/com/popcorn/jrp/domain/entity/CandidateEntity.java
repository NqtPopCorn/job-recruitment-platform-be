package com.popcorn.jrp.domain.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "candidates")
@Data
public class CandidateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;

    private String name;
    private LocalDate birthday;
    private String avatar;
    private String industry;
    private String designation;
    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer experience;
    private String currentSalary;
    private String expectedSalary;
    private String gender; // male | female
    private String languages;
    private String skills;
    private String educationLevel;
    private Boolean status;
}

