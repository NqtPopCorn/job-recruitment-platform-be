package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.EmployerServicePackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployerServicePackageRepository extends JpaRepository<EmployerServicePackageEntity, Long> {
    List<EmployerServicePackageEntity> findAllByOrderByPriceAsc();
}