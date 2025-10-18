package com.popcorn.jrp.domain.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "resumes")
@Getter
@Setter
public class ResumeEntity extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateEntity candidate;

    private String filename;
    private Boolean status;
}

