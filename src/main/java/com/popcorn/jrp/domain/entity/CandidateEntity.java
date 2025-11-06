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
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "candidates")
@Getter
@Setter
public class CandidateEntity extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
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
    private BigDecimal currentSalary;
    private BigDecimal expectedSalary;
    private String currency;
    private String gender;
    private String email;
    private String phone;
    private Double hourlyRate;
    private String city;
    private String country;

    @Convert(converter = JsonListStringConverter.class)
    @Column(columnDefinition = "JSON")
    private List<String> languages = new ArrayList<>(); // JSON

    @Convert(converter = JsonListStringConverter.class)
    @Column(columnDefinition = "JSON")
    private List<String> skills = new ArrayList<>(); // JSON

    private String educationLevel;

    private boolean status;

    @Column(columnDefinition = "JSON")
    private String socialMedias = "[]";

    @OneToMany(mappedBy = "candidate")
    private List<CandidateImageEntity> images;

    @OneToMany(mappedBy = "candidate")
    private List<ResumeEntity> resumes;

    @Override
    public void onCreate() {
        super.onCreate();
        this.status = true;
    }
}
