package com.auth.authservice.service.token;

import com.auth.authservice.entity.UserEntity;

import io.jsonwebtoken.Claims;

public interface IJwtTokenService {
	String generateToken(UserEntity user);

	boolean validateToken(String token);

	Claims getClaims(String token);
}
