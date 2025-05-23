package com.auth.authservice.controller.auth.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.authservice.controller.auth.IAuthController;
import com.auth.authservice.dto.ForgotPasswordDto;
import com.auth.authservice.dto.LoginRequestDto;
import com.auth.authservice.dto.LoginResponseDto;
import com.auth.authservice.dto.RecoverPasswordDto;
import com.auth.authservice.dto.RefreshRequestDto;
import com.auth.authservice.dto.RefreshResponseDto;
import com.auth.authservice.dto.RegisterRequestDto;
import com.auth.authservice.dto.ResetCodeDto;
import com.auth.authservice.dto.ResetPasswordByAdminDto;
import com.auth.authservice.dto.ResetPasswordDto;
import com.auth.authservice.dto.ResetTokenDto;
import com.auth.authservice.service.auth.IAuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController implements IAuthController {

	private final IAuthService authService;

	@Override
	public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@Override
	public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDto request) {
		authService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Override
	public ResponseEntity<Void> logout(HttpServletRequest request) {
		authService.logout(request);
		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<RefreshResponseDto> refreshToken(@Valid @RequestBody RefreshRequestDto refreshToken) {
		return ResponseEntity.ok(authService.refreshToken(refreshToken));
	}

	@Override
	public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordDto request) {
		authService.forgotPassword(request);
		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<ResetTokenDto> verifyResetCode(@Valid @RequestBody ResetCodeDto request) {
		return ResponseEntity.ok(authService.verifyResetCode(request));
	}

	@Override
	public ResponseEntity<Void> recoverPassword(@Valid @RequestBody RecoverPasswordDto request) {
		authService.recoverPassword(request);
		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordDto request,
			HttpServletRequest httpRequest) {
		authService.resetPassword(request, httpRequest);
		return ResponseEntity.ok().build();
	}

	public ResponseEntity<Void> resetPasswordByAdmin(@Valid @RequestBody ResetPasswordByAdminDto request,
			Integer userId) {
		authService.resetPasswordByAdmin(request, userId);
		return ResponseEntity.ok().build();
	}
}
