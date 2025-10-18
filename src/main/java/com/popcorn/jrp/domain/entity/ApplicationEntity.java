package com.popcorn.jrp.domain.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Data
public class ApplicationEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateEntity candidate;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private JobEntity job;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private ResumeEntity resume;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;

    public enum Status {
        PENDING, REVIEWED, ACCEPTED, REJECTED
    }
}

