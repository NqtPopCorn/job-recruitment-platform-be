package com.popcorn.jrp.domain.entity;
import com.popcorn.jrp.helper.JsonListStringConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "candidates")
@Getter
@Setter
public class CandidateEntity extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
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

    private Integer experience;
    private String currentSalary;
    private String expectedSalary;
    private String gender;
    private String email;
    private String phone;
    private Double hourlyRate;
    private String city;
    private String country;

    @Convert(converter = JsonListStringConverter.class)
    @Column(columnDefinition = "JSON")
    private List<String> languages; // JSON
    @Convert(converter = JsonListStringConverter.class)
    @Column(columnDefinition = "JSON")
    private List<String> skills; // JSON

    private String educationLevel;
    // For soft delete
    private boolean status;

    @OneToMany(mappedBy = "candidate")
    private List<CandidateImageEntity> images;

    @OneToMany(mappedBy = "candidate")
    private List<ResumeEntity> resumes;

    private LocalDateTime createdAt;
    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
    }

}

