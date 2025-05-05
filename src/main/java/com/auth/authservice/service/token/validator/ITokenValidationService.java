package com.auth.authservice.service.token.validator;

import io.jsonwebtoken.Claims;

public interface ITokenValidationService {
	Claims validateToken(String token);

	Claims validateRefreshToken(String token);

	Claims extractClaims(String token);
}
