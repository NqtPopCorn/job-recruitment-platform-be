package com.popcorn.jrp.repository;

import com.popcorn.jrp.domain.entity.UserEntity;
import com.popcorn.jrp.domain.enums.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    // Lấy tất cả user có role là CANDIDATE
    List<UserEntity> findByRole(Role role);
}