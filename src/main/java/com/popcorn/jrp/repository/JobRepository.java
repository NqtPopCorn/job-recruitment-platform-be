package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<JobEntity, Long>, JpaSpecificationExecutor<JobEntity> {

    // 4. GET List Category (Industry of Job)
    @Query("SELECT DISTINCT j.industry FROM JobEntity j WHERE j.industry IS NOT NULL AND j.industry != '' AND j.isDeleted = false")
    List<String> findDistinctIndustries();

    // 5. GET List Primary Industry of Company
    @Query("SELECT DISTINCT j.industry FROM JobEntity j WHERE j.employer.id = :companyId AND j.industry IS NOT NULL AND j.industry != '' AND j.isDeleted = false")
    List<String> findDistinctIndustriesByEmployerId(Long companyId);

    // 6. GET List Skills (Lấy JSON để Java xử lý)
    // @Query("SELECT j.skills FROM JobEntity j WHERE j.skills IS NOT NULL AND
    // j.isDeleted = false")
    // List<String> findAllSkillsJson();

    // 7. GET List Cities
    @Query("SELECT DISTINCT j.city FROM JobEntity j WHERE j.city IS NOT NULL AND j.city != '' AND j.isDeleted = false")
    List<String> findDistinctCities();

    // 8. GET Max Salary (Lấy JSON để Java xử lý)
    @Query("SELECT MAX(j.maxSalary) FROM JobEntity j WHERE j.maxSalary IS NOT NULL AND j.isDeleted = false AND j.currency = :currency")
    BigDecimal getMaxSalaryWithCurrency(@Param("currency") String currency);

    // // 10. GET List Job Dashboard
    // List<JobEntity> findByEmployerIdAndIsDeletedFalse(Long companyId, Page);

    // 9. GET Related Jobs (Tìm theo industry, country, city)
    List<JobEntity> findByIdNotAndIndustryAndCountryAndCityAndIsDeletedFalse(
            Long id, String industry, String country, String city);
}