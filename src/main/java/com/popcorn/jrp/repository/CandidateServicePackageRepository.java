package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.CandidateServicePackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CandidateServicePackageRepository extends JpaRepository<CandidateServicePackageEntity, Long> {
    List<CandidateServicePackageEntity> findAllByOrderByPriceAsc();
}