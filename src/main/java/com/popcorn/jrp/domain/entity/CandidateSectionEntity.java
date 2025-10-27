package com.popcorn.jrp.domain.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Candidate Experiences and Achievement
 */
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
    private String organization;
    private LocalDate startTime;
    private LocalDate endTime;

    @Column(columnDefinition = "TEXT")
    private String text;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

