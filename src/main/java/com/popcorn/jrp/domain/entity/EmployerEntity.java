package com.popcorn.jrp.domain.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "employers")
@Data
public class EmployerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false)
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

