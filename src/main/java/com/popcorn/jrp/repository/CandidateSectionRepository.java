package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.CandidateSectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CandidateSectionRepository extends JpaRepository<CandidateSectionEntity, Long> {
    // Tìm tất cả các section thuộc về một ứng viên
    List<CandidateSectionEntity> findByCandidateId(Long candidateId);
}
