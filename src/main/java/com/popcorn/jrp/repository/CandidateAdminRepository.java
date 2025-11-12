package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CandidateAdminRepository extends JpaRepository<CandidateEntity, Long>, JpaSpecificationExecutor<CandidateEntity> {
    Optional<CandidateEntity> findById(Long id);
}
