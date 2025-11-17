package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("SELECT COUNT(n) FROM NotificationEntity n " +
            "WHERE n.user.id = :userId AND n.isDeleted = false")
    Long countByUserId(@Param("userId") Long userId);

    @Query("SELECT n FROM NotificationEntity n " +
            "WHERE n.user.id = :userId AND n.isDeleted = false " +
            "ORDER BY n.createdAt DESC")
    List<NotificationEntity> findLatestByUserId(@Param("userId") Long userId, Pageable pageable);
}
