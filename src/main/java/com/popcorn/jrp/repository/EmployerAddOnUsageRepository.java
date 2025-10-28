package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.EmployerAddOnUsageEntity;
import com.popcorn.jrp.domain.entity.EmployerAddOnUsageEntity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployerAddOnUsageRepository extends JpaRepository<EmployerAddOnUsageEntity, Long> {

    List<EmployerAddOnUsageEntity> findByEmployerIdAndStatus(Long employerId, Status status);

    @Query("SELECT e FROM EmployerAddOnUsageEntity e WHERE e.status = 'ACTIVE' AND e.endDate < :currentDate")
    List<EmployerAddOnUsageEntity> findExpiredAddOns(LocalDate currentDate);
}