package com.auth.authservice.service.token.validator.impl;

import org.springframework.stereotype.Service;

import com.auth.authservice.enums.TokenTypeEnum;
import com.auth.authservice.exception.ApiExceptionFactory;
import com.auth.authservice.repository.JwtRepository;
import com.auth.authservice.service.token.impl.JwtTokenServiceImpl;
import com.auth.authservice.service.token.validator.ITokenValidationService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenValidationService implements ITokenValidationService {
	private final JwtRepository jwtRepository;
	private final JwtTokenServiceImpl jwtTokenService;
	private final ApiExceptionFactory apiExceptionFactory;
	private final JwtParser jwtParser;

	public Claims validateToken(String token) {
		Claims claims = extractClaims(token);

		if (!jwtRepository.existsByToken(token)) {
			throw apiExceptionFactory.authException("auth.token.not.exists");
		}

		if (jwtRepository.isRevoked(token)) {
			throw apiExceptionFactory.authException("auth.token.revoked");
		}

		if (!jwtRepository.isValid(token)) {
			throw apiExceptionFactory.authException("auth.token.invalid");
		}

		if (claims.getExpiration().getTime() < System.currentTimeMillis()) {
			jwtTokenService.revokeToken(token);
			throw apiExceptionFactory.authException("auth.token.expired");
		}

		return claims;
	}

	public Claims validateResetToken(String token) {
		return validateToken(token);
	}

	public Claims validateRefreshToken(String token) {
		Claims claims = validateToken(token);

		if (!jwtRepository.isTokenType(token, TokenTypeEnum.REFRESH)) {
			throw apiExceptionFactory.authException("auth.token.not.refresh");
		}

		return claims;
	}

	public Claims extractClaims(String token) {
		try {
			return jwtParser.parseSignedClaims(token).getPayload();
		} catch (ExpiredJwtException e) {
			throw apiExceptionFactory.authException("auth.token.expired");
		} catch (Exception e) {
			throw apiExceptionFactory.authException("auth.token.invalid");
		}
	}
}
