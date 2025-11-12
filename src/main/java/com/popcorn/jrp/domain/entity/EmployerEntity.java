package com.popcorn.jrp.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployerEntity extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // Danh sách ứng viên tiềm năng
    @ManyToMany
    @JoinTable(name = "potential_candidates", // tên bảng trung gian
            joinColumns = @JoinColumn(name = "employer_id"), // khóa ngoại trỏ tới Employer
            inverseJoinColumns = @JoinColumn(name = "candidate_id") // khóa ngoại trỏ tới Candidate
    )
    private List<CandidateEntity> potentialCandidates = new ArrayList<>();

    private String email;
    private String name;
    private String primaryIndustry;
    private String size;
    private Integer foundedIn;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String phone;
    private String address;
    private String logo;
    private String website;
    private String country;
    private String city;
    private Boolean status;

    @Column(columnDefinition = "JSON")
    private String socialMedias = "[]";

    @OneToMany(mappedBy = "employer")
    private List<EmployerImageEntity> images;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        status = true;
    }
}
