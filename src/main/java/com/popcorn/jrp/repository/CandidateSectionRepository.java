package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.CandidateSectionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CandidateSectionRepository extends JpaRepository<CandidateSectionEntity, Long> {
    // Tìm tất cả các section thuộc về một ứng viên
    List<CandidateSectionEntity> findByCandidateId(Long candidateId);
    @Modifying
    @Transactional
    @Query("DELETE FROM CandidateSectionEntity c WHERE c.candidate.id = :candidateId")
    void deleteAllByCandidateId(Long candidateId);
}
