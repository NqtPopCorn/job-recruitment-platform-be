package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.EmployerAddOnPackageEntity;
import com.popcorn.jrp.domain.entity.EmployerAddOnPackageEntity.AddOnType;
import com.popcorn.jrp.domain.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployerAddOnPackageRepository extends JpaRepository<EmployerAddOnPackageEntity, Long> {
    List<EmployerAddOnPackageEntity> findByType(AddOnType type);
}
