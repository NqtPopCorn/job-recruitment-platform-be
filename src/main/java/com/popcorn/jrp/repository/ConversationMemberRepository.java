// File: src/main/java/com/popcorn/jrp/repository/ConversationMemberRepository.java
package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.ConversationMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ConversationMemberRepository extends JpaRepository<ConversationMemberEntity, Long> {
    // Check thanh vien
    boolean existsByConversationIdAndUserId(Long conversationId, Long userId);

    Optional<ConversationMemberEntity> findByConversationIdAndUserId(Long conversationId, Long userId);

    long countByConversationId(Long conversationId);
}