package com.popcorn.jrp.domain.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @Column(nullable = false)
    private String role; // admin | candidate | employer

    // Quan hệ 1-1 hoặc 1-n với Candidate, Employer, SocialMedia, etc.
}

