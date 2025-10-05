package com.popcorn.jrp.domain.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "candidate_sections")
@Data
public class CandidateSectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidateId", nullable = false)
    private CandidateEntity candidate;

    private String title;
    private String industry;
    private LocalDate startTime;
    private LocalDate endTime;

    @Column(columnDefinition = "TEXT")
    private String text;
}

