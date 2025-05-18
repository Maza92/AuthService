package com.auth.authservice.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.auth.authservice.entity.ResetCodeEntity;
import com.auth.authservice.entity.UserEntity;

@Repository
public interface ResetCodeRepository extends JpaRepository<ResetCodeEntity, Integer> {
    
    @Query("SELECT r FROM ResetCodeEntity r WHERE r.code = :code AND r.user = :user AND r.used = false AND r.expiresAt > :now")
    Optional<ResetCodeEntity> findValidCodeByUserAndCode(
        @Param("code") String code, 
        @Param("user") UserEntity user,
        @Param("now") Instant now
    );

    @Query("SELECT r FROM ResetCodeEntity r WHERE r.user = :user AND r.used = false AND r.expiresAt > :now")
    Optional<ResetCodeEntity> findValidCodeByUser(
        @Param("user") UserEntity user,
        @Param("now") Instant now
    );

    boolean existsByUserAndUsedFalseAndExpiresAtGreaterThan(UserEntity user, Instant now);
}