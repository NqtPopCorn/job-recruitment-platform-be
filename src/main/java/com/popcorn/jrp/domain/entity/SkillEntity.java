package com.popcorn.jrp.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "skills")
public class SkillEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;

    // Quan hệ ngược (không bắt buộc, có thể bỏ nếu không cần)
    // @ManyToMany(mappedBy = "skills")
    // private List<JobEntity> jobs;
    @Override
    public String toString() {
        return name;
    }
}
