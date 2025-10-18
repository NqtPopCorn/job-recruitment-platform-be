package com.popcorn.jrp.domain.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "employer_images")
@Getter
@Setter
public class EmployerImage extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    private EmployerEntity employer;

    private String filename;
}

