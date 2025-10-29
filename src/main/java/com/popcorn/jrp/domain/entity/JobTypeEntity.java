package com.popcorn.jrp.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job_type")
public class JobTypeEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;

    // Quan hệ ngược (tùy chọn)
    // @ManyToMany(mappedBy = "jobTypes")
    // private List<JobEntity> jobs;
}
