package com.popcorn.jrp.domain.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employer_images")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployerImageEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    private EmployerEntity employer;

    private String filename;
}

