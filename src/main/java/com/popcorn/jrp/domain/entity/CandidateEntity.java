package com.popcorn.jrp.domain.entity;
import com.popcorn.jrp.helper.ListStringConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private Integer experience;
    private String currentSalary;
    private String expectedSalary;
    private String gender;
    private String email;
    private String phone;
    private Double hourlyRate;
    private String city;
    private String country;

    @Convert(converter = ListStringConverter.class)
    private List<String> languages; // "English, Japanese".split
    @Convert(converter = ListStringConverter.class)
    private List<String> skills; // "Java, React, Nodejs".split

    private String educationLevel;
    // For soft delete
    private boolean status;

    private LocalDateTime createdAt;
    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
    }

//    @OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<CandidateSectionEntity> candidateSections;

}

