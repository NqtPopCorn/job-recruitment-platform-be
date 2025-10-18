package com.popcorn.jrp.domain.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "candidate_sections")
@Getter
@Setter
public class CandidateSectionEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateEntity candidate;

    private String title;
    private String industry;
    private LocalDate startTime;
    private LocalDate endTime;

    @Column(columnDefinition = "TEXT")
    private String text;
}

