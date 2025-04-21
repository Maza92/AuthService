package com.auth.authservice.service.auth;

import com.auth.authservice.dto.LoginRequestDto;
import com.auth.authservice.dto.LoginResponseDto;
import com.auth.authservice.dto.RegisterRequestDto;

public interface IAuthService {
	LoginResponseDto login(LoginRequestDto request);

	void register(RegisterRequestDto request);
}
