package com.popcorn.jrp.repository.spec;

import com.popcorn.jrp.domain.entity.UserEntity;
import com.popcorn.jrp.domain.request.user.UserQueryAdmin;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserAdminSpecification {
    public Specification<UserEntity> getAdminSpecification(UserQueryAdmin params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.getSearch() != null && !params.getSearch().isEmpty()) {
                String searchPattern = "%" + params.getSearch().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("email")), searchPattern));
            }

            if (params.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), params.getStatus()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
