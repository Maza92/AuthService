package com.auth.authservice.service.auth;

import com.auth.authservice.dto.ForgotPasswordDto;
import com.auth.authservice.dto.LoginRequestDto;
import com.auth.authservice.dto.LoginResponseDto;
import com.auth.authservice.dto.RecoverPasswordDto;
import com.auth.authservice.dto.RefreshRequestDto;
import com.auth.authservice.dto.RefreshResponseDto;
import com.auth.authservice.dto.RegisterRequestDto;
import com.auth.authservice.dto.ResetCodeDto;
import com.auth.authservice.dto.ResetPasswordDto;
import com.auth.authservice.dto.ResetTokenDto;

import jakarta.servlet.http.HttpServletRequest;

public interface IAuthService {
	LoginResponseDto login(LoginRequestDto request);

	void register(RegisterRequestDto request);

	void logout(HttpServletRequest request);

	RefreshResponseDto refreshToken(RefreshRequestDto refreshToken);

	void forgotPassword(ForgotPasswordDto request);

	ResetTokenDto verifyResetCode(ResetCodeDto code);

	void recoverPassword(RecoverPasswordDto request);

	void resetPassword(ResetPasswordDto request, HttpServletRequest httpRequest);
}
