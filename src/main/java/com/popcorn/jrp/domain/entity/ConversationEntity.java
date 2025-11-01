package com.popcorn.jrp.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "conversations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationEntity extends BaseEntity{

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Dự phòng cho future scale
    // @Column(length = 20)
    // private String type; // "private", "group"
    //
    // private String name;
    // private String imageUrl;

    // Relations
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConversationMemberEntity> members = new ArrayList<>();

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageEntity> messages = new ArrayList<>();
}

