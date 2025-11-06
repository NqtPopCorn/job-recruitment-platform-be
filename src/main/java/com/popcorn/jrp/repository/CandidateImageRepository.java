package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.CandidateImageEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CandidateImageRepository extends JpaRepository<CandidateImageEntity, Long> {
    Optional<CandidateImageEntity> findByCandidateIdAndFilename(Long candidateId, String filename);
}
