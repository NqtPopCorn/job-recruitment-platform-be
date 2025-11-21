package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.EmployerSubscriptionEntity;
import com.popcorn.jrp.domain.entity.EmployerSubscriptionEntity.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployerSubscriptionRepository extends JpaRepository<EmployerSubscriptionEntity, Long> {

    Optional<EmployerSubscriptionEntity> findByEmployerIdAndStatus(Long employerId, SubscriptionStatus status);

    List<EmployerSubscriptionEntity> findByEmployerId(Long employerId);

    @Query("SELECT e FROM EmployerSubscriptionEntity e WHERE e.status = 'ACTIVE' AND e.endDate < :currentDate AND e.isLifetime = false")
    List<EmployerSubscriptionEntity> findExpiredSubscriptions(LocalDate currentDate);

    @Query("SELECT e FROM EmployerSubscriptionEntity e WHERE e.employerId = :employerId AND e.status = 'ACTIVE'")
    Optional<EmployerSubscriptionEntity> findActiveSubscription(Long employerId);

    boolean existsByServicePackageId(Long packageId);

    boolean existsByServicePackageIdAndStatus(Long packageId, SubscriptionStatus status);

}