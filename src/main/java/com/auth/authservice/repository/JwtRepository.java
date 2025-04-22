package com.auth.authservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth.authservice.entity.JwtTokenEntity;

@Repository
public interface JwtRepository extends JpaRepository<JwtTokenEntity, Integer> {
	long countByUserEntity_IdAndIsRevokedFalseAndIsValidTrue(Long userId);

	List<JwtTokenEntity> findByUserEntity_IdAndIsRevokedFalseAndIsValidTrue(Long userId);
}
