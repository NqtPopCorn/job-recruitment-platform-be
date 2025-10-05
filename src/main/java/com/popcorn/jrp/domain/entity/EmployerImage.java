package com.popcorn.jrp.domain.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "employer_images")
@Data
public class EmployerImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employerId", nullable = false)
    private EmployerEntity employer;

    private String filename;
}

