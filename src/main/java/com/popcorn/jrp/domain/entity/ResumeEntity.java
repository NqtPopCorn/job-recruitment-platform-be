package com.popcorn.jrp.domain.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "resumes")
@Data
public class ResumeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidateId", nullable = false)
    private CandidateEntity candidate;

    private String filename;
    private Boolean status;
}

