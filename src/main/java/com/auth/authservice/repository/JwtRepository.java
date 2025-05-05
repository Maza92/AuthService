package com.auth.authservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.auth.authservice.entity.JwtTokenEntity;
import com.auth.authservice.enums.TokenTypeEnum;

@Repository
public interface JwtRepository extends JpaRepository<JwtTokenEntity, Integer> {
	@Query("SELECT COUNT(j) > 0 FROM JwtTokenEntity j WHERE j.token = :token")
	boolean existsByToken(@Param("token") String token);

	@Query("SELECT COUNT(j) FROM JwtTokenEntity j WHERE j.userEntity.id = :userId AND j.isRevoked = false AND j.isValid = true AND j.tokenType = 'ACCESS'")
	long countActiveTokensByUser(@Param("userId") Long userId);

	@Query("SELECT j FROM JwtTokenEntity j WHERE j.userEntity.id = :userId AND j.isRevoked = false AND j.isValid = true")
	List<JwtTokenEntity> findActiveTokensByUser(@Param("userId") Long userId);

	@Query("SELECT j FROM JwtTokenEntity j WHERE j.token = :token AND j.isValid = true AND j.isRevoked = false")
	Optional<JwtTokenEntity> findActiveToken(@Param("token") String token);

	@Query("SELECT COUNT(j) > 0 FROM JwtTokenEntity j WHERE j.token = :token AND j.isRevoked = true")
	boolean isRevoked(@Param("token") String token);

	@Query("SELECT COUNT(j) > 0 FROM JwtTokenEntity j WHERE j.token = :token AND j.isValid = true")
	boolean isValid(@Param("token") String token);

	@Query("UPDATE JwtTokenEntity j SET j.isRevoked = true WHERE j.token = :token")
	void revokeToken(@Param("token") String token);

	@Query("UPDATE JwtTokenEntity j SET j.isValid = false WHERE j.token = :token")
	void invalidateToken(@Param("token") String token);

	@Query("SELECT COUNT(j) > 0 FROM JwtTokenEntity j WHERE j.token = :token AND j.tokenType = :tokenType")
	boolean isTokenType(@Param("token") String token, @Param("tokenType") TokenTypeEnum tokenType);
}
