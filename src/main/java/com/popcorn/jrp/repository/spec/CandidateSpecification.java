package com.popcorn.jrp.repository.spec;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.request.CandidateSearchRequest;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public final class CandidateSpecification {

    public static Specification<CandidateEntity> getSpecification(CandidateSearchRequest request) {
        return CandidateSpecification.hasNameLike(request.getSearch())
                .and(CandidateSpecification.hasLocationLike(request.getLocation()))
                .and(CandidateSpecification.hasIndustryLike(request.getIndustry()))
                .and(CandidateSpecification.hasGender(request.getGender()))
                .and(CandidateSpecification.hasExperienceLessThanOrEqual(request.getExperience()))
                .and(CandidateSpecification.withEducationLevel(request.getEducation()));
    }

    public static Specification<CandidateEntity> hasNameLike(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) {
                return cb.conjunction(); // Trả về điều kiện luôn đúng (true)
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<CandidateEntity> hasLocationLike(String location) {
        return (root, query, cb) -> {
            if (location == null || location.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%");
        };
    }

    public static Specification<CandidateEntity> hasIndustryLike(String industry) {
        return (root, query, cb) -> {
            if (industry == null || industry.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("industry")), "%" + industry.toLowerCase() + "%");
        };
    }

    public static Specification<CandidateEntity> hasGender(CandidateEntity.Gender gender) {
        return (root, query, cb) -> {
            if (gender == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("gender"), gender);
        };
    }

    public static Specification<CandidateEntity> hasExperienceLessThanOrEqual(Double experience) {
        return (root, query, cb) -> {
            if (experience == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("experience"), experience);
        };
    }

    public static Specification<CandidateEntity> withEducationLevel(String education) {
        return (root, query, cb) -> {
            if (education == null || education.isEmpty()) {
                return cb.conjunction();
            }

            String eduLower = education.toLowerCase();
            if ("university".equals(eduLower)) {
                return cb.like(cb.lower(root.get("educationLevel")), "%đại học%");
            } else if ("college".equals(eduLower)) {
                return cb.like(cb.lower(root.get("educationLevel")), "%cao đẳng%");
            } else if ("other".equals(eduLower)) {
                Predicate notUniversity = cb.notLike(cb.lower(root.get("educationLevel")), "%đại học%");
                Predicate notCollege = cb.notLike(cb.lower(root.get("educationLevel")), "%cao đẳng%");
                return cb.and(notUniversity, notCollege);
            }
            return cb.conjunction(); // Nếu không khớp case nào thì không lọc gì
        };
    }
}