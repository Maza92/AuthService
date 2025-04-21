package com.auth.authservice.service.token.key.impl;

import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth.authservice.exception.ApiExceptionFactory;
import com.auth.authservice.service.token.key.IJwtKeyService;

import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtKeyServiceImpl implements IJwtKeyService {

	@Value("${security.jwt.secret-key}")
	private String secretKey;

	private final ApiExceptionFactory apiExceptionFactory;

	@Override
	public @NotNull SecretKey generateKey() {
		try {
			byte[] keyBytes = Base64.getDecoder().decode(secretKey);
			return Keys.hmacShaKeyFor(keyBytes);
		} catch (IllegalArgumentException e) {
			throw this.apiExceptionFactory.authException("auth.token.key.error", e.getMessage());
		} catch (Exception e) {
			throw this.apiExceptionFactory.authException("exception.unexpected", e.getMessage());
		}
	}

	@Override
	public boolean isValidKey(byte @NotNull [] key) {
		try {
			var secretKeyToValidate = Keys.hmacShaKeyFor(key);
			return secretKeyToValidate.getEncoded().length >= 32;
		} catch (Exception e) {
			return false;
		}
	}
}
