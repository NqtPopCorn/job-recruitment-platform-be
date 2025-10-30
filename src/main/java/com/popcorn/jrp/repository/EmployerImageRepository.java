package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.EmployerImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployerImageRepository extends JpaRepository<EmployerImageEntity, Long> {
    Optional<EmployerImageEntity> findByFilename(String name);
}
