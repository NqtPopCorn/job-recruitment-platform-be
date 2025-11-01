package com.popcorn.jrp.repository.spec;

import com.popcorn.jrp.domain.entity.JobEntity;
import com.popcorn.jrp.domain.entity.JobTypeEntity;
import com.popcorn.jrp.domain.request.job.EmployerJobQueryDto;
import com.popcorn.jrp.domain.request.job.JobQueryParameters;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class JobSpecification {

    /**
     * Spec for public data
     */
    public Specification<JobEntity> publicFilter(JobQueryParameters params) {

        // Bắt đầu với các điều kiện cơ sở: CHƯA BỊ XÓA và ĐANG HOẠT ĐỘNG
        Specification<JobEntity> spec = isNotDeleted().and(isActive());

        // Nếu không có tham số lọc, chỉ trả về điều kiện cơ sở
        if (params == null) {
            return spec;
        }

        return spec
                .and(hasSearchTerm(params.getSearch()))
                .and(hasLocation(params.getLocation()))
                .and(hasCategory(params.getCategory()))
                .and(hasType(params.getType()))
                .and(hasExperience(params.getExperience()))
                .and(withDatePosted(params.getDatePosted()))
                .and(withSalaryRange(params.getMin(), params.getMax()));
    }

    /**
     * Spec for permitted data
     */
    public Specification<JobEntity> dashboardFilter(EmployerJobQueryDto params) {
        Specification<JobEntity> spec = isNotDeleted().and(isActive());
        if (params == null) {
            return spec;
        }
        return spec
                .and(hasCategory(params.getCategory()))
                .and(withDatePosted(params.getDatePosted()));
    }

    private Specification<JobEntity> isNotDeleted() {
        return (root, query, cb) -> cb.equal(root.get("isDeleted"), false);
    }

    private Specification<JobEntity> isActive() {
        return (root, query, cb) -> cb.equal(root.get("status"), true);
    }

    /**
     * 1. Lọc theo 'search' (jobTitle HOẶC employer.name)
     * (Tự động JOIN với bảng EmployerEntity)
     */
    private Specification<JobEntity> hasSearchTerm(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isEmpty()) {
                return cb.conjunction(); // true
            }
            String searchPattern = "%" + search.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("jobTitle")), searchPattern),
                    cb.like(cb.lower(root.get("employer").get("name")), searchPattern));
        };
    }

    /**
     * 2. Lọc theo 'location' (location HOẶC city)
     */
    private Specification<JobEntity> hasLocation(String location) {
        return (root, query, cb) -> {
            if (location == null || location.isEmpty()) {
                return cb.conjunction();
            }
            String locationPattern = "%" + location.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("location")), locationPattern),
                    cb.like(cb.lower(root.get("city")), locationPattern));
        };
    }

    /**
     * 3. Lọc theo 'category' (chính là trường 'industry')
     */
    private Specification<JobEntity> hasCategory(String category) {
        return (root, query, cb) -> {
            if (category == null || category.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("industry"), category);
        };
    }

    /**
     * 4. Lọc theo 'type' (Full-time, Part-time, v.v.)
     * Đây là quan hệ ManyToMany, cần JOIN.
     */
    private Specification<JobEntity> hasType(String type) {
        return (root, query, cb) -> {
            if (type == null || type.isEmpty()) {
                return cb.conjunction();
            }

            Join<JobEntity, JobTypeEntity> jobTypeJoin = root.join("jobTypes");
            return cb.equal(jobTypeJoin.get("name"), type);
        };
    }

    /**
     * 5. Lọc theo 'experience' (năm kinh nghiệm)
     */
    private Specification<JobEntity> hasExperience(Integer experience) {
        return (root, query, cb) -> {
            if (experience == null) {
                return cb.conjunction();
            }

            return cb.lessThanOrEqualTo(root.get("experience"), experience);
        };
    }

    /**
     * 6. Lọc theo 'datePosted' (số ngày tính từ hôm nay)
     * [BỔ SUNG]
     */
    private Specification<JobEntity> withDatePosted(int datePosted) {
        return (root, query, cb) -> {
            if (datePosted <= 0) {
                return cb.conjunction(); // 0 hoặc số âm nghĩa là không lọc
            }

            LocalDateTime pastDateTime = LocalDateTime.now().minusDays(datePosted);
            return cb.greaterThanOrEqualTo(root.get("createdAt"), pastDateTime);
        };
    }

    /**
     * 7. Lọc theo khoảng lương (min/max)
     * [BỔ SUNG]
     * Logic: Tìm các job có khoảng lương (minSalary, maxSalary) GIAO NHAU
     * với khoảng lương người dùng tìm (min, max).
     * Bao gồm cả các job "Thỏa thuận" (negotiable = true).
     */
    private Specification<JobEntity> withSalaryRange(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {
            // Nếu không filter lương, bỏ qua
            if (min == null && max == null) {
                return cb.conjunction();
            }

            // Điều kiện 1: Job này là "Lương thỏa thuận"
            Predicate isNegotiable = cb.isTrue(root.get("negotiable"));

            // Điều kiện 2: Khoảng lương của job giao với khoảng lương tìm kiếm
            Predicate salaryMatches;

            if (min != null && max != null) {
                // Logic overlap: (Job.minSalary <= User.max) AND (Job.maxSalary >= User.min)
                salaryMatches = cb.and(
                        cb.lessThanOrEqualTo(root.get("minSalary"), max),
                        cb.greaterThanOrEqualTo(root.get("maxSalary"), min));
            } else if (min != null) {
                // Chỉ có min: Job.maxSalary >= User.min
                salaryMatches = cb.greaterThanOrEqualTo(root.get("maxSalary"), min);
            } else {
                // Chỉ có max: Job.minSalary <= User.max
                salaryMatches = cb.lessThanOrEqualTo(root.get("minSalary"), max);
            }

            // Kết hợp: (Lương khớp) HOẶC (Thỏa thuận)
            return cb.or(salaryMatches, isNegotiable);
        };
    }
}