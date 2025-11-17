package com.popcorn.jrp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.popcorn.jrp.domain.entity.ApplicationEntity;
import com.popcorn.jrp.domain.entity.JobEntity;
import org.springframework.data.domain.Pageable;

@Repository
public interface ApplicationRepository
        extends JpaRepository<ApplicationEntity, Long>, JpaSpecificationExecutor<ApplicationEntity> {
    boolean existsByCandidateIdAndJobId(Long candidateId, Long jobId);

    Optional<ApplicationEntity> findByCandidateIdAndJobId(Long candidateId, Long jobId);

    @Query("SELECT a FROM ApplicationEntity a JOIN FETCH a.candidate WHERE a.job = :job")
    List<ApplicationEntity> findAllByJob(@Param("job") JobEntity job);

    @Query("SELECT COUNT(a) FROM ApplicationEntity a WHERE a.candidate.id = :candidateId AND a.isDeleted = false")
    Long countByCandidateId(@Param("candidateId") Long candidateId);

    @Query("""
        SELECT a FROM ApplicationEntity a
        JOIN FETCH a.job j
        JOIN FETCH j.employer e
        WHERE a.candidate.id = :candidateId
        AND a.isDeleted = false
        AND j.isDeleted = false
        ORDER BY a.appliedAt DESC
    """)
    List<ApplicationEntity> findRecentApplicationsByCandidateId(
            @Param("candidateId") Long candidateId,
            Pageable pageable
    );
}
