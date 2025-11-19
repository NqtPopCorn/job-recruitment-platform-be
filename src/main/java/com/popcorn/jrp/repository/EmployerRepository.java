package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.entity.EmployerEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployerRepository extends JpaRepository<EmployerEntity, Long>,
        JpaSpecificationExecutor<EmployerEntity> {

    // Dùng cho: 4. GET DETAIL COMPANY BY USER ID
    Optional<EmployerEntity> findByUserId(Long userId);
    Optional<EmployerEntity> findByUserIdAndIsDeletedFalse(Long userId);

    // Dùng cho: 6. GET INDUSTRY LIST
    @Query("SELECT DISTINCT e.primaryIndustry FROM EmployerEntity e WHERE e.primaryIndustry IS NOT NULL AND e.primaryIndustry != ''")
    List<String> findDistinctIndustries();

    // Dùng cho: 7. POST NEW COMPANY (Kiểm tra email)
    boolean existsByEmail(String email);

    // Dùng cho: 9. SOFT DELETE (Để lấy cả entity đã bị xóa)
    @Query("SELECT e FROM EmployerEntity e WHERE e.id = :id AND e.status = false")
    Optional<EmployerEntity> findSoftDeletedById(Long id);

    @Query("SELECT c FROM EmployerEntity e JOIN e.potentialCandidates c "
            + "WHERE e.id = :employerId "
            + "AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<CandidateEntity> findPotentialCandidatesByEmployerId(
            @Param("employerId") Long employerId,
            @Param("search") String search,
            Pageable pageable);

    @Query("SELECT COUNT(j) FROM JobEntity j " +
            "WHERE j.employer.id = :employerId " +
            "AND j.isDeleted = false")
    Long countPostedJobsByEmployerId(@Param("employerId") Long employerId);

    @Query("SELECT COUNT(a) FROM ApplicationEntity a " +
            "JOIN a.job j " +
            "WHERE j.employer.id = :employerId " +
            "AND a.isDeleted = false")
    Long countApplicationsByEmployerId(@Param("employerId") Long employerId);


    @Query("SELECT COUNT(DISTINCT m) FROM MessageEntity m " +
            "JOIN ConversationMemberEntity cm ON cm.conversation.id = m.conversation.id " +
            "WHERE cm.user.id = (SELECT e.user.id FROM EmployerEntity e WHERE e.id = :employerId) " +
            "AND m.isDeleted = false " +
            "AND m.senderUser.id != (SELECT e.user.id FROM EmployerEntity e WHERE e.id = :employerId)")
    Long countMessagesByEmployerId(@Param("employerId") Long employerId);


    @Query("SELECT COUNT(pc) FROM EmployerEntity e " +
            "JOIN e.potentialCandidates pc " +
            "WHERE e.id = :employerId " +
            "AND pc.isDeleted = false")
    Long countShortlistedCandidatesByEmployerId(@Param("employerId") Long employerId);
}
