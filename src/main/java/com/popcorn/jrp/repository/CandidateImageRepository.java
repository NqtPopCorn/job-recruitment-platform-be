package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.CandidateImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateImageRepository extends JpaRepository<CandidateImageEntity, Long> {
}
