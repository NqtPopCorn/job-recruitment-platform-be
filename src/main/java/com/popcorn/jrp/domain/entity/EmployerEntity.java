package com.popcorn.jrp.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
}
