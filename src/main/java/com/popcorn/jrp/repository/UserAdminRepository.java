package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserAdminRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    long countByStatus(boolean status);
}
