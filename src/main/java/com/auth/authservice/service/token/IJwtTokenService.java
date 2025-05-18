package com.auth.authservice.service.token;

import com.auth.authservice.dto.RefreshResponseDto;
import com.auth.authservice.dto.ResetTokenDto;
import com.auth.authservice.dto.LoginResponseDto;
import com.auth.authservice.entity.UserEntity;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

public interface IJwtTokenService {
	String generateToken(UserEntity user);

	String generateRefreshToken(UserEntity user);

	String generateResetToken(UserEntity user);

	LoginResponseDto generateLoginUserTokens(UserEntity user);

	RefreshResponseDto generateRefreshUserTokens(UserEntity user, String refreshToken);

	ResetTokenDto generatePasswordResetToken(UserEntity user);

	void revokeToken(String token);

	boolean validateToken(String token);

	Claims getClaims(String token);

	String extractTokenHeader(HttpServletRequest request);
}
