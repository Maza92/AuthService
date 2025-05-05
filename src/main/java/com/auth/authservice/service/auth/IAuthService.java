package com.auth.authservice.service.auth;

import com.auth.authservice.dto.LoginRequestDto;
import com.auth.authservice.dto.LoginResponseDto;
import com.auth.authservice.dto.RefreshRequestDto;
import com.auth.authservice.dto.RefreshResponseDto;
import com.auth.authservice.dto.RegisterRequestDto;

import jakarta.servlet.http.HttpServletRequest;

public interface IAuthService {
	LoginResponseDto login(LoginRequestDto request);

	void register(RegisterRequestDto request);

	void logout(HttpServletRequest request);

	RefreshResponseDto refreshToken(RefreshRequestDto refreshToken);
}
