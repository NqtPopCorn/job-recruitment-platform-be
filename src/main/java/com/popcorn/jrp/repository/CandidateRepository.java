package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository
        extends JpaRepository<CandidateEntity, Long>, JpaSpecificationExecutor<CandidateEntity> {

    Optional<CandidateEntity> findByIdAndStatusTrueAndIsDeletedFalse(Long id);

    Optional<CandidateEntity> findByUserIdAndIsDeletedFalse(Long userId);

    Optional<CandidateEntity> findByUserId(Long userId);

    @Query("SELECT COUNT(a) FROM ApplicationEntity a " +
            "WHERE a.candidate.id = :candidateId AND a.isDeleted = false")
    Long countAppliedJobsByCandidateId(@Param("candidateId") Long candidateId);

    @Query("SELECT COUNT(DISTINCT j) FROM JobEntity j " +
            "WHERE j.isDeleted = false AND j.status = true " +
            "AND j.industry IN (SELECT c.industry FROM CandidateEntity c WHERE c.id = :candidateId) " +
            "AND j.expirationDate > CURRENT_TIMESTAMP")
    Long countJobAlertsByCandidateId(@Param("candidateId") Long candidateId);

    @Query("SELECT COUNT(DISTINCT m) FROM MessageEntity m " +
            "JOIN ConversationMemberEntity cm ON cm.conversation.id = m.conversation.id " +
            "WHERE cm.user.id = (SELECT c.user.id FROM CandidateEntity c WHERE c.id = :candidateId) " +
            "AND m.isDeleted = false " +
            "AND m.senderUser.id != (SELECT c.user.id FROM CandidateEntity c WHERE c.id = :candidateId)")
    Long countUnreadMessagesByCandidateId(@Param("candidateId") Long candidateId);

    @Query("SELECT COUNT(j) FROM JobEntity j " +
            "JOIN j.employer e " +
            "JOIN e.potentialCandidates pc " +
            "WHERE pc.id = :candidateId AND j.isDeleted = false AND j.status = true")
    Long countShortlistedJobsByCandidateId(@Param("candidateId") Long candidateId);
}
