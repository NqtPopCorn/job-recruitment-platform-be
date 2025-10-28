package com.popcorn.jrp.repository.spec;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.popcorn.jrp.domain.entity.JobEntity;
import com.popcorn.jrp.domain.entity.JobTypeEntity;
import com.popcorn.jrp.domain.query.JobQuery;

import jakarta.persistence.criteria.*;
import java.time.LocalDateTime;

public class JobSpecification {
    public static Specification<JobEntity> filter(JobQuery query) {
        return (Root<JobEntity> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            Predicate predicate = cb.conjunction();

            // 🔍 Search theo name hoặc level
            if (query.getSearch() != null && !query.getSearch().isEmpty()) {
                String pattern = "%" + query.getSearch().toLowerCase() + "%";
                Predicate nameLike = cb.like(cb.lower(root.get("name")), pattern);
                Predicate levelLike = cb.like(cb.lower(root.get("level")), pattern);
                predicate = cb.and(predicate, cb.or(nameLike, levelLike));
            }

            // 🌍 Location (country/city)
            if (query.getLocation() != null && !query.getLocation().isEmpty()) {
                String pattern = "%" + query.getLocation().toLowerCase() + "%";
                Predicate countryLike = cb.like(cb.lower(root.get("country")), pattern);
                Predicate cityLike = cb.like(cb.lower(root.get("city")), pattern);
                predicate = cb.and(predicate, cb.or(countryLike, cityLike));
            }

            // 🏭 Category
            if (query.getCategory() != null && !query.getCategory().isEmpty()) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("industry"), query.getCategory()));
            }

            // 🕒 Job Type (ManyToMany)
            if (query.getType() != null && !query.getType().isEmpty()) {
                String pattern = "%" + query.getType().toLowerCase().replace("-", " ") + "%";
                Join<JobEntity, JobTypeEntity> jobTypeJoin = root.join("jobTypes", JoinType.LEFT);
                predicate = cb.and(predicate,
                        cb.like(cb.lower(jobTypeJoin.get("name")), pattern));
            }

            // 🧠 Experience
            if (query.getExperience() != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("experience"), query.getExperience()));
            }

            // 📅 Date posted (lọc theo số ngày gần đây)
            if (query.getDatePosted() != null && query.getDatePosted() > 0) {
                LocalDateTime fromDate = LocalDateTime.now().minusDays(query.getDatePosted());
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate));
            }

            // 💰 Salary range
            if (query.getMin() != null || query.getMax() != null) {
                if (query.getMin() != null) {
                    predicate = cb.and(predicate,
                            cb.greaterThanOrEqualTo(root.get("minSalary"), query.getMin()));
                }
                if (query.getMax() != null) {
                    predicate = cb.and(predicate,
                            cb.lessThanOrEqualTo(root.get("maxSalary"), query.getMax()));
                }
            }

            // 💱 Currency
            if (query.getCurrency() != null && !query.getCurrency().isEmpty()) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("currency"), query.getCurrency()));
            }

            // ⏰ Job còn hạn + status = true
            predicate = cb.and(predicate,
                    cb.greaterThanOrEqualTo(root.get("expirationDate"), LocalDateTime.now()));
            predicate = cb.and(predicate, cb.isTrue(root.get("status")));

            return predicate;
        };
    }
}
