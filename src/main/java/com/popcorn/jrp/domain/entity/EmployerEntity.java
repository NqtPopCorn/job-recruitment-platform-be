package com.popcorn.jrp.domain.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "employers")
@Getter
@Setter
public class EmployerEntity extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

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
    private String socialMedias;

    @OneToMany(mappedBy = "employer")
    private List<EmployerImageEntity> images;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

