package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.ResumeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ResumeRepository extends JpaRepository<ResumeEntity, Long>, JpaSpecificationExecutor<ResumeEntity> {
    List<ResumeEntity> findByCandidateId(Long candidateId);

    long countByCandidateId(Long candidateId);
}
