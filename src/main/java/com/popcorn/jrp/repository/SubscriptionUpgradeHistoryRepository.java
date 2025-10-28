package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.SubscriptionUpgradeHistoryEntity;
import com.popcorn.jrp.domain.entity.SubscriptionUpgradeHistoryEntity.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubscriptionUpgradeHistoryRepository extends JpaRepository<SubscriptionUpgradeHistoryEntity, Long> {
    List<SubscriptionUpgradeHistoryEntity> findBySubscriptionIdAndSubscriptionType(Long subscriptionId, SubscriptionType type);
}