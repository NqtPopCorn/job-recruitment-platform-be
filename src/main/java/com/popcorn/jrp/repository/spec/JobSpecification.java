package com.popcorn.jrp.repository.spec;

import com.popcorn.jrp.domain.entity.JobEntity;
import com.popcorn.jrp.domain.request.job.JobQueryParameters;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JobSpecification {

    public Specification<JobEntity> filterBy(JobQueryParameters params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Luôn lọc các job chưa bị xóa mềm
            predicates.add(cb.equal(root.get("isDeleted"), false));

            if (params == null) {
                return cb.and(predicates.toArray(new Predicate[0]));
            }

            // 1. Lọc theo 'search' (jobTitle hoặc company name)
            if (params.getSearch() != null && !params.getSearch().isEmpty()) {
                String searchPattern = "%" + params.getSearch().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("jobTitle")), searchPattern),
                        cb.like(cb.lower(root.get("employer").get("name")), searchPattern)
                ));
            }

            // 2. Lọc theo 'location' (location hoặc city)
            if (params.getLocation() != null && !params.getLocation().isEmpty()) {
                String locationPattern = "%" + params.getLocation().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("location")), locationPattern),
                        cb.like(cb.lower(root.get("city")), locationPattern)
                ));
            }

            // 3. Lọc theo 'category' (industry)
            if (params.getCategory() != null && !params.getCategory().isEmpty()) {
                predicates.add(cb.equal(root.get("industry"), params.getCategory()));
            }

            // 4. Lọc theo 'type' (JSON search, ví dụ: "Full Time")
            if (params.getType() != null && !params.getType().isEmpty()) {
                predicates.add(cb.like(root.get("jobType"), "%" + params.getType() + "%"));
            }

            // 5. Lọc theo 'experience' (integer)
            if (params.getExperience() != null && !params.getExperience().isEmpty()) {
                try {
                    Integer exp = Integer.parseInt(params.getExperience());
                    predicates.add(cb.equal(root.get("experience"), exp));
                } catch (NumberFormatException e) {
                    // Bỏ qua nếu không phải là số
                }
            }

            // 6. Lọc lương (Yêu cầu xử lý JSON trong DB, phức tạp)
            // Tạm thời bỏ qua lọc min/max salary vì nó nằm trong JSON.

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}