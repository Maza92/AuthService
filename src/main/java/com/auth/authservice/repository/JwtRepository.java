package com.auth.authservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.auth.authservice.entity.JwtTokenEntity;

@Repository
public interface JwtRepository extends JpaRepository<JwtTokenEntity, Integer> {
	@Query("SELECT COUNT(j) FROM JwtTokenEntity j WHERE j.userEntity.id = :userId AND j.isRevoked = false AND j.isValid = true")
	long countActiveTokensByUser(@Param("userId") Long userId);

	@Query("SELECT j FROM JwtTokenEntity j WHERE j.userEntity.id = :userId AND j.isRevoked = false AND j.isValid = true")
	List<JwtTokenEntity> findActiveTokensByUser(@Param("userId") Long userId);
}
