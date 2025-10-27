package com.popcorn.jrp.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "candidate_sections")
@Getter
@Setter
public class CandidateSectionEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateEntity candidate;

    private String category;

    private String title;
    private String organization;
    private LocalDate startTime;
    private LocalDate endTime;

    @Column(columnDefinition = "TEXT")
    private String text;

}
