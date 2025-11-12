package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.EmployerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmployerAdminRepository extends JpaRepository<EmployerEntity, Long>, JpaSpecificationExecutor<EmployerEntity> {
}
