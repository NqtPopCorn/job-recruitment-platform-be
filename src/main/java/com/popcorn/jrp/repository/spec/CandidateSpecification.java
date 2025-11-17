package com.popcorn.jrp.repository.spec;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.request.candidate.CandidateSearchAdminRequest;
import com.popcorn.jrp.domain.request.candidate.CandidateSearchRequest;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Component;

@Component
public class CandidateSpecification {

    public Specification<CandidateEntity> getPublicSpecification(CandidateSearchRequest request) {
        return hasNameLike(request.getSearch())
                .and(hasLocationLike(request.getLocation()))
                .and(hasIndustryLike(request.getIndustry()))
                .and(hasGender(request.getGender()))
                .and(hasExperienceLessThanOrEqual(request.getExperience()))
                .and(withEducationLevel(request.getEducation()))
                .and(hasStatus(true))
                .and(hasIsDeleted());
    }

    // Filter candidates for chat
    public Specification<CandidateEntity> getForChatSpecification(String search) {
        return hasNameLike(search)
                .and(hasEmailLike(search))
                .and(hasStatus(true))
                .and(hasIsDeleted());
    }

    public Specification<CandidateEntity> hasNameLike(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) {
                return cb.conjunction(); // Trả về điều kiện luôn đúng (true)
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public Specification<CandidateEntity> hasEmailLike(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isEmpty()) {
                return cb.conjunction(); // Trả về điều kiện luôn đúng (true)
            }
            return cb.like(cb.lower(root.get("email")), "%" + search.toLowerCase() + "%");
        };
    }

    public Specification<CandidateEntity> hasLocationLike(String location) {
        return (root, query, cb) -> {
            if (location == null || location.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%");
        };
    }

    public Specification<CandidateEntity> hasIndustryLike(String industry) {
        return (root, query, cb) -> {
            if (industry == null || industry.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("industry")), "%" + industry.toLowerCase() + "%");
        };
    }

    public Specification<CandidateEntity> hasGender(String gender) {
        return (root, query, cb) -> {
            if (gender == null || gender.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("gender"), gender);
        };
    }

    public Specification<CandidateEntity> hasExperienceLessThanOrEqual(Integer experience) {
        return (root, query, cb) -> {
            if (experience == null || experience <= 0) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("experience"), experience);
        };
    }

    public Specification<CandidateEntity> withEducationLevel(String education) {
        return (root, query, cb) -> {
            if (education == null || education.isEmpty()) {
                return cb.conjunction(); // không lọc nếu không có giá trị
            }
            return cb.like(cb.lower(root.get("educationLevel")), "%" + education.toLowerCase() + "%");
        };
    }

    public Specification<CandidateEntity> hasStatus(Boolean status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public Specification<CandidateEntity> hasIsDeleted() {
        return (root, query, cb) -> {
            return cb.isFalse(root.get("isDeleted"));
        };
    }

    public Specification<CandidateEntity> getAdminSpecification(CandidateSearchAdminRequest request) {
        return hasNameLike(request.getSearch())
                .and(hasLocationLike(request.getLocation()))
                .and(hasIndustryLike(request.getIndustry()))
                .and(hasGender(request.getGender()))
                .and(hasStatus(request.getStatus()))
                .and(hasIsDeleted());
    }
}