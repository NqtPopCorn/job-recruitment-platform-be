package com.popcorn.jrp.repository.spec;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.popcorn.jrp.domain.entity.ApplicationEntity;
import com.popcorn.jrp.domain.query.ApplicationQueryDto;

@Component
public class ApplicationSpecification {
    public Specification<ApplicationEntity> filterApplications(ApplicationQueryDto queryDto) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            // Lọc theo candidateId
            if (queryDto.getCandidateId() != null) {
                predicates.getExpressions().add(
                        cb.equal(root.get("candidate").get("id"), queryDto.getCandidateId()));
            }

            // Lọc theo trạng thái
            if (queryDto.getStatus() != null && !queryDto.getStatus().isBlank()) {
                predicates.getExpressions().add(
                        cb.equal(root.get("status"), queryDto.getStatus()));
            }

            // Lọc theo datePosted
            if (queryDto.getDatePosted() != null && queryDto.getDatePosted() > 0) {
                LocalDateTime fromDate = LocalDateTime.now().minusDays(queryDto.getDatePosted());
                predicates.getExpressions().add(
                        cb.greaterThanOrEqualTo(root.get("createdAt").as(LocalDateTime.class), fromDate));
            }

            return predicates;
        };
    }
}
