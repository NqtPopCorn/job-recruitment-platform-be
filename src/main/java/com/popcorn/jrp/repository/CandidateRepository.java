package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository
        extends JpaRepository<CandidateEntity, Long>, JpaSpecificationExecutor<CandidateEntity> {
    Optional<CandidateEntity> getCandidateByUserId(Long userId);

}
