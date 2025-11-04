package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.EmployerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployerRepository extends JpaRepository<EmployerEntity, Long>,
        JpaSpecificationExecutor<EmployerEntity> {

    // Dùng cho: 4. GET DETAIL COMPANY BY USER ID
    Optional<EmployerEntity> findByUserId(Long userId);

    // Dùng cho: 6. GET INDUSTRY LIST
    @Query("SELECT DISTINCT e.primaryIndustry FROM EmployerEntity e WHERE e.primaryIndustry IS NOT NULL AND e.primaryIndustry != ''")
    List<String> findDistinctIndustries();

    // Dùng cho: 7. POST NEW COMPANY (Kiểm tra email)
    boolean existsByEmail(String email);

    // Dùng cho: 9. SOFT DELETE (Để lấy cả entity đã bị xóa)
    @Query("SELECT e FROM EmployerEntity e WHERE e.id = :id AND e.status = false")
    Optional<EmployerEntity> findSoftDeletedById(Long id);
}
