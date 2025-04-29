package com.auth.authservice.service.token.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth.authservice.entity.JwtTokenEntity;
import com.auth.authservice.entity.UserEntity;
import com.auth.authservice.repository.JwtRepository;
import com.auth.authservice.service.token.IJwtTokenService;
import com.auth.authservice.service.token.key.IJwtKeyService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements IJwtTokenService {

	private final IJwtKeyService jwtKeyService;
	private final JwtParser jwtParser;
	private final JwtRepository jwtRepository;

	@Value("${security.jwt.expiration-ms-time}")
	private long expirationTime;

	@Value("${security.jwt.expiration-refresh-ms-time}")
	private long refreshExpirationTime;

	@Override
	public String generateToken(UserEntity user) {
		return generateToken(user, expirationTime);
	}

	@Override
	public String generateRefreshToken(UserEntity user) {
		return generateToken(user, refreshExpirationTime);
	}

	private String generateToken(UserEntity user, Long expirationTime) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + expirationTime);

		String token = Jwts.builder()
				.subject(user.getId().toString())
				.claim("email", user.getEmail())
				.claim("role", user.getRole())
				.issuedAt(now)
				.expiration(expiration)
				.signWith(jwtKeyService.generateKey())
				.compact();

		jwtRepository.save(new JwtTokenEntity().setToken(token)
				.setUserEntity(user)
				.setValid(true)
				.setRevoked(false)
				.setExpiry(expiration.toInstant())
				.setJti(token));

		return token;
	}

	@Override
	public void revokeToken(String token) {
		JwtTokenEntity jwtToken = jwtRepository
				.findActiveTokensByUser(Long.parseLong(token))
				.stream()
				.filter(t -> t.getToken().equals(token))
				.findFirst()
				.orElse(null);

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
}