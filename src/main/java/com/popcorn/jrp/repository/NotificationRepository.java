package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    // Lấy 6 notifications mới nhất của candidate
    @Query("SELECT n FROM NotificationEntity n " +
            "WHERE n.user.id = :userId " +
            "AND n.isDeleted = false " +
            "ORDER BY n.createdAt DESC")
    List<NotificationEntity> findTop6ByUserIdOrderByCreatedAtDesc(
            @Param("userId") Long userId,
            Pageable pageable);

    // Lấy tất cả notifications với phân trang
    Page<NotificationEntity> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(
            Long userId,
            Pageable pageable);

    // Đếm số notifications chưa đọc
    @Query("SELECT COUNT(n) FROM NotificationEntity n " +
            "WHERE n.user.id = :userId " +
            "AND n.isRead = false " +
            "AND n.isDeleted = false")
    Long countUnreadByUserId(@Param("userId") Long userId);

    // Đánh dấu tất cả là đã đọc
    @Query("UPDATE NotificationEntity n " +
            "SET n.isRead = true " +
            "WHERE n.user.id = :userId " +
            "AND n.isRead = false " +
            "AND n.isDeleted = false")
    void markAllAsReadByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT n FROM NotificationEntity n 
        WHERE n.user.id = :userId 
        AND n.isRead = false 
        AND n.isDeleted = false 
        ORDER BY n.createdAt DESC
        """)
    List<NotificationEntity> findAllUnreadByUserId(@Param("userId") Long userId);
}

