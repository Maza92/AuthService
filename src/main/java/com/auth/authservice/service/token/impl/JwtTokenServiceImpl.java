package com.auth.authservice.service.token.impl;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth.authservice.entity.RoleEntity;
import com.auth.authservice.entity.UserEntity;
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

	@Value("${security.jwt.expiration-time}")
	private long expirationTime;

	@Override
	public String generateToken(UserEntity user) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + expirationTime);

		return Jwts.builder()
				.subject(user.getId().toString())
				.claim("email", user.getEmail())
				.claim("roles", user.getRoles().stream()
						.map(RoleEntity::getName)
						.collect(Collectors.toList()))
				.issuedAt(now)
				.expiration(expiration)
				.signWith(jwtKeyService.generateKey())
				.compact();
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