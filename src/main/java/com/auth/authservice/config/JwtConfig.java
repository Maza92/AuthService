package com.auth.authservice.config;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.auth.authservice.exception.ApiExceptionFactory;
import com.auth.authservice.service.token.key.IJwtKeyService;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

	private final IJwtKeyService jwtKeyService;
	private final ApiExceptionFactory apiExceptionFactory;

	@Value("${security.jwt.secret-key}")
	private String secretKey;

	@PostConstruct
	public void validateConfiguration() {
		try {
			byte[] keyBytes = Base64.getDecoder().decode(secretKey);
			if (!this.jwtKeyService.isValidKey(keyBytes)) {
				throw this.apiExceptionFactory.authException("auth.token.key.invalid");
			}
		} catch (IllegalArgumentException e) {
			throw this.apiExceptionFactory.authException("auth.token.key.error", e.getMessage());
		}
	}

	@Bean
	JwtParser jwtParser() {
		return Jwts.parser()
				.verifyWith(this.jwtKeyService.generateKey())
				.build();
	}
}