package com.popcorn.jrp.domain.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "resumes")
@Getter
@Setter
public class ResumeEntity extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateEntity candidate;

    @Column(name = "file_name")
    private String fileName;
    private Boolean status;

}

