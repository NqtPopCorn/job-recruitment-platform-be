package com.popcorn.jrp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.popcorn.jrp.domain.entity.ApplicationEntity;
import com.popcorn.jrp.domain.entity.JobEntity;

@Repository
public interface ApplicationRepository
        extends JpaRepository<ApplicationEntity, Long>, JpaSpecificationExecutor<ApplicationEntity> {
    boolean existsByCandidateIdAndJobId(Long candidateId, Long jobId);

    Optional<ApplicationEntity> findByCandidateIdAndJobId(Long candidateId, Long jobId);

    @Query("SELECT a FROM ApplicationEntity a JOIN FETCH a.candidate WHERE a.job = :job")
    List<ApplicationEntity> findAllByJob(@Param("job") JobEntity job);

    // Lấy các công việc đã apply gần đây (4 jobs mới nhất)
    @Query("SELECT a FROM ApplicationEntity a " +
            "JOIN FETCH a.job j " +
            "JOIN FETCH j.employer e " +
            "WHERE a.candidate.id = :candidateId " +
            "AND a.isDeleted = false " +
            "ORDER BY a.appliedAt DESC")
    List<ApplicationEntity> findRecentApplicationsByCandidateId(
            @Param("candidateId") Long candidateId,
            Pageable pageable);

    // Lấy tất cả applications với phân trang
    @Query("SELECT a FROM ApplicationEntity a " +
            "JOIN FETCH a.job j " +
            "JOIN FETCH j.employer e " +
            "WHERE a.candidate.id = :candidateId " +
            "AND a.isDeleted = false " +
            "ORDER BY a.appliedAt DESC")
    Page<ApplicationEntity> findAllByCandidateIdWithDetails(
            @Param("candidateId") Long candidateId,
            Pageable pageable);

    // Kiểm tra candidate đã apply job này chưa
    boolean existsByCandidateIdAndJobIdAndIsDeletedFalse(Long candidateId, Long jobId);

    @Query("""
    SELECT a FROM ApplicationEntity a 
    JOIN FETCH a.candidate c 
    JOIN FETCH a.job j 
    WHERE j.employer.id = :employerId 
    AND a.isDeleted = false 
    ORDER BY a.appliedAt DESC
    """)
    Page<ApplicationEntity> findAllApplicationsByEmployerId(
            @Param("employerId") Long employerId,
            Pageable pageable
    );

}
