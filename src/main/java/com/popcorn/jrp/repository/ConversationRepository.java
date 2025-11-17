// File: src/main/java/com/popcorn/jrp/repository/ConversationRepository.java
package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.ConversationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {

    /**
     * Find private conversation
     */
    @Query("SELECT c FROM ConversationEntity c " +
            "JOIN c.members m1 " +
            "JOIN c.members m2 " +
            "WHERE m1.user.id = :userId1 AND m2.user.id = :userId2 " +
            "AND (SELECT COUNT(m.id) FROM ConversationMemberEntity m WHERE m.conversation = c) = 2")
    Optional<ConversationEntity> findPrivateConversationByUsers(
            @Param("userId1") Long userId1,
            @Param("userId2") Long userId2
    );

    /**
     * Get paginated conversations
     */
    @Query("SELECT c FROM ConversationEntity c JOIN c.members m WHERE m.user.id = :userId")
    Page<ConversationEntity> findConversationsByUserId(@Param("userId") Long userId, Pageable pageable);
}