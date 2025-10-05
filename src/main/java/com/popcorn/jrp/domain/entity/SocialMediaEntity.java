package com.popcorn.jrp.domain.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "social_media")
@Data
public class SocialMediaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;

    private String platform;
    private String url;
}

