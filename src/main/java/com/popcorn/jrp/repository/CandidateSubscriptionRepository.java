package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.CandidateSubscriptionEntity;
import com.popcorn.jrp.domain.entity.CandidateSubscriptionEntity.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateSubscriptionRepository extends JpaRepository<CandidateSubscriptionEntity, Long> {

    Optional<CandidateSubscriptionEntity> findByCandidateIdAndStatus(Long candidateId, SubscriptionStatus status);

    List<CandidateSubscriptionEntity> findByCandidateId(Long candidateId);

    @Query("SELECT c FROM CandidateSubscriptionEntity c WHERE c.status = 'ACTIVE' AND c.endDate < :currentDate AND c.isLifetime = false")
    List<CandidateSubscriptionEntity> findExpiredSubscriptions(LocalDate currentDate);

    @Query("SELECT c FROM CandidateSubscriptionEntity c WHERE c.candidateId = :candidateId AND c.status = 'ACTIVE'")
    Optional<CandidateSubscriptionEntity> findActiveSubscription(Long candidateId);
}