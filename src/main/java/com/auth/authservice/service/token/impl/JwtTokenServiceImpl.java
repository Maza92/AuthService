package com.auth.authservice.service.token.impl;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth.authservice.dto.LoginResponseDto;
import com.auth.authservice.dto.RefreshResponseDto;
import com.auth.authservice.dto.ResetTokenDto;
import com.auth.authservice.entity.JwtTokenEntity;
import com.auth.authservice.entity.UserEntity;
import com.auth.authservice.enums.TokenTypeEnum;
import com.auth.authservice.exception.ApiExceptionFactory;
import com.auth.authservice.repository.JwtRepository;
import com.auth.authservice.service.token.IJwtTokenService;
import com.auth.authservice.service.token.key.IJwtKeyService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements IJwtTokenService {

	private final IJwtKeyService jwtKeyService;
	private final JwtParser jwtParser;
	private final JwtRepository jwtRepository;
	private final ApiExceptionFactory apiExceptionFactory;

	@Value("${security.jwt.expiration-ms-time}")
	private long expirationTime;

	@Value("${security.jwt.expiration-refresh-ms-time}")
	private long refreshExpirationTime;

	@Value("${security.jwt.password-reset-expiration}")
	private Long passwordResetExpiration;

	@Value("${security.jwt.max-refresh-count}")
	private int maxRefreshCount;

	@Override
	public String generateToken(UserEntity user) {
		return generateToken(user, expirationTime, TokenTypeEnum.ACCESS,
				Map.of("email", user.getEmail(),
						"role", user.getRole()));
	}

	@Override
	public String generateRefreshToken(UserEntity user) {
		return generateToken(user, refreshExpirationTime, TokenTypeEnum.REFRESH,
				Map.of("email", user.getEmail(),
						"role", user.getRole()));
	}

	@Override
	public String generateResetToken(UserEntity user) {
		return generateToken(user, passwordResetExpiration, TokenTypeEnum.RESET,
				Map.of("type", TokenTypeEnum.RESET.name()));
	}

	@Override
	public ResetTokenDto generatePasswordResetToken(UserEntity user) {
		String token = generateResetToken(user);
		return new ResetTokenDto()
				.setResetToken(token);
	}

	@Override
	public LoginResponseDto generateLoginUserTokens(UserEntity user) {
		String token = generateToken(user);
		String refreshToken = generateRefreshToken(user);
		return new LoginResponseDto()
				.setToken(token)
				.setRefreshToken(refreshToken)
				.setExpiration(getClaims(token).getExpiration().toInstant())
				.setUsername(user.getUsername());
	}

	@Override
	public RefreshResponseDto generateRefreshUserTokens(UserEntity user, String oldRefreshToken) {

		JwtTokenEntity refreshTokenEntity = jwtRepository
				.findActiveToken(oldRefreshToken)
				.orElseThrow(() -> apiExceptionFactory.businessException("auth.token.not.exists"));

		if (refreshTokenEntity.getRefreshCount() > maxRefreshCount) {
			throw apiExceptionFactory.businessException("auth.token.refresh.count.exceeded");
		}

		refreshTokenEntity.setRefreshCount(refreshTokenEntity.getRefreshCount() + 1);
		refreshTokenEntity.setLastRefreshAt(Instant.now());
		jwtRepository.save(refreshTokenEntity);

		String token = generateToken(user);
		String refreshToken = generateRefreshToken(user);
		return new RefreshResponseDto()
				.setAccessToken(token)
				.setRefreshToken(refreshToken)
				.setExpiration(getClaims(token).getExpiration().toInstant());
	}

	private String generateToken(UserEntity user, Long expirationTime, TokenTypeEnum tokenType,
			Map<String, Object> additionalClaims) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + expirationTime);

		var jwtBuilder = Jwts.builder()
				.subject(user.getId().toString())
				.claim("username", user.getUsername())
				.issuedAt(now)
				.expiration(expiration);

		if (additionalClaims != null && !additionalClaims.isEmpty()) {
			additionalClaims.forEach(jwtBuilder::claim);
		}

		String token = jwtBuilder.signWith(jwtKeyService.generateKey())
				.compact();

		jwtRepository.save(new JwtTokenEntity().setToken(token)
				.setUserEntity(user)
				.setValid(true)
				.setRevoked(false)
				.setTokenType(tokenType)
				.setExpiry(expiration.toInstant())
				.setJti(token));

		return token;
	}

	@Override
	public void revokeToken(String token) {
		JwtTokenEntity jwtToken = jwtRepository
				.findActiveToken(token)
				.orElseThrow(() -> apiExceptionFactory.businessException("auth.token.not.exists"));

		if (jwtToken != null) {
			jwtToken.setRevoked(true);
			jwtToken.setRevokedAt(new Date().toInstant());
			jwtRepository.save(jwtToken);
		}
	}

	@Override
	public boolean validateToken(String token) {
		try {
			jwtParser.parseSignedClaims(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Claims getClaims(String token) {
		return jwtParser.parseSignedClaims(token).getPayload();
	}

	public String extractTokenHeader(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer ")) {
			return header.substring(7);
		}
		return null;
	}

}