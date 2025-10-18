package com.popcorn.jrp.domain.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "candidates")
@Getter
@Setter
public class CandidateEntity extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private String name;
    private LocalDate birthday;
    private String avatar;
    private String industry;
    private String designation;
    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "DECIMAL(4,2)")
    private Double experience;
    private String currentSalary;
    private String expectedSalary;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public enum Gender {
        male,
        female,
        other
    }

    private String languages;
    private String skills;
    private String educationLevel;
    private Boolean status;
}

