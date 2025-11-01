package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.MessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    // sắp xếp theo thời gian tạo giảm dần
    Page<MessageEntity> findByConversationIdOrderByCreatedAtDesc(Long conversationId, Pageable pageable);

    /**
     * Get latest message
     */
    Optional<MessageEntity> findFirstByConversationIdOrderByCreatedAtDesc(Long conversationId);

    /**
     * Đếm số lượng tin nhắn chưa đọc (được tạo SAU thời điểm lastSeenAt).
     */
    long countByConversationIdAndCreatedAtAfter(Long conversationId, LocalDateTime lastSeenAt);
}
