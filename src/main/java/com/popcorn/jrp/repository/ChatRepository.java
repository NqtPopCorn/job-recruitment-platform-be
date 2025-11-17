package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

    @Query("SELECT COUNT(c) FROM ChatEntity c " +
            "WHERE (c.sender.id = :userId OR c.receiver.id = :userId) " +
            "AND c.isDeleted = false")
    Long countByUserId(@Param("userId") Long userId);
}