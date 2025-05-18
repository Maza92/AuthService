package com.auth.authservice.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.auth.authservice.entity.ResetCodeEntity;

@Repository
public interface ResetCodeRepository extends JpaRepository<ResetCodeEntity, Integer> {

    @Query("UPDATE ResetCodeEntity r SET r.used = true WHERE r.user.id = :userId AND r.used = false")
    @Modifying
    void invalidateAllUserCodes(@Param("userId") Integer userId);

    @Query("SELECT r FROM ResetCodeEntity r WHERE r.code = :code AND r.user.id = :userId AND r.used = false")
    Optional<ResetCodeEntity> findCodeByUserAndCode(
            @Param("code") String code,
            @Param("userId") Integer userId);
}