package com.popcorn.jrp.repository.spec;

import com.popcorn.jrp.domain.entity.EmployerEntity;
import com.popcorn.jrp.domain.request.employer.EmployerQueryAdmin;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class EmployerAdminSpecification {
    public Specification<EmployerEntity> getAdminSpecification(EmployerQueryAdmin params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.getSearch() != null && !params.getSearch().isEmpty()) {
                String searchPattern = "%" + params.getSearch().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), searchPattern),
                        cb.like(cb.lower(root.get("email")), searchPattern)));
            }

            if (params.getLocation() != null && !params.getLocation().isEmpty()) {
                String locPattern = "%" + params.getLocation().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("address")), locPattern),
                        cb.like(cb.lower(root.get("city")), locPattern)));
            }

            if (params.getPrimaryIndustry() != null && !params.getPrimaryIndustry().isEmpty()) {
                predicates.add(cb.equal(root.get("primaryIndustry"), params.getPrimaryIndustry()));
            }

            predicates.add(cb.between(root.get("foundedIn"), params.getFoundationDateMin(), params.getFoundationDateMax()));

            if (params.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), params.getStatus()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
