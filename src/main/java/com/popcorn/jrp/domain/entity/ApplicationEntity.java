package com.popcorn.jrp.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateEntity candidate;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private JobEntity job;

    private String filename;

    private String coverLetter;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private LocalDateTime appliedAt;

    public enum Status {
        PENDING, REVIEWED, ACCEPTED, REJECTED
    }
}
