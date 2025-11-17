package com.popcorn.jrp.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversation_members", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "conversation_id" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private ConversationEntity conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private LocalDateTime lastSeenAt;

    // Future fields
    // private LocalDateTime joinedAt;
    // private String displayName;
    // private String role; // "member", "admin", "super_admin"

    @PrePersist
    public void prePersist() {
        if (lastSeenAt == null) {
            lastSeenAt = LocalDateTime.now();
        }
    }
}
