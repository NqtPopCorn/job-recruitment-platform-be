package com.popcorn.jrp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.popcorn.jrp.domain.entity.ApplicationEntity;
import com.popcorn.jrp.domain.entity.JobEntity;

@Repository
public interface ApplicationRepository
        extends JpaRepository<ApplicationEntity, Long>, JpaSpecificationExecutor<ApplicationEntity> {
    boolean existsByCandidateIdAndJobId(Long candidateId, Long jobId);

    Optional<ApplicationEntity> findByCandidateIdAndJobId(Long candidateId, Long jobId);

    @Query("SELECT a FROM ApplicationEntity a JOIN FETCH a.candidate WHERE a.job = :job")
    List<ApplicationEntity> findAllByJob(@Param("job") JobEntity job);
}
