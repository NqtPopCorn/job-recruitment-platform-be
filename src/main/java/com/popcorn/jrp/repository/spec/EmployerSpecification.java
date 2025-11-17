package com.popcorn.jrp.repository.spec;

import com.popcorn.jrp.domain.entity.EmployerEntity;
import com.popcorn.jrp.domain.request.employer.EmployerQueryParameters;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class EmployerSpecification {

    public Specification<EmployerEntity> filterBy(EmployerQueryParameters params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Lọc theo 'search' (name hoặc email)
            if (params.getSearch() != null && !params.getSearch().isEmpty()) {
                String searchPattern = "%" + params.getSearch().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), searchPattern),
                        cb.like(cb.lower(root.get("email")), searchPattern)));
            }

            // Lọc theo 'location' (address hoặc city)
            if (params.getLocation() != null && !params.getLocation().isEmpty()) {
                String locationPattern = "%" + params.getLocation().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("address")), locationPattern),
                        cb.like(cb.lower(root.get("city")), locationPattern)));
            }

            // Lọc theo 'primaryIndustry'
            if (params.getPrimaryIndustry() != null && !params.getPrimaryIndustry().isEmpty()) {
                predicates.add(cb.equal(root.get("primaryIndustry"), params.getPrimaryIndustry()));
            }

            // Lọc theo 'foundationDate'
            predicates.add(cb.between(root.get("foundedIn"),
                    params.getFoundationDateMin(),
                    params.getFoundationDateMax()));

            // Mặc định luôn lọc status = true (mặc dù đã có @Where)
            predicates.add(cb.isTrue(root.get("status")));

            // Mặc định luôn lọc isDeleted = false (mặc dù đã có @Where)
            predicates.add(cb.isFalse(root.get("isDeleted")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
