package com.popcorn.jrp.domain.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "blogs")
@Getter @Setter
public class BlogEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

