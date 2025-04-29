package com.auth.authservice.controller.auth.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.authservice.controller.auth.IAuthController;
import com.auth.authservice.dto.LoginRequestDto;
import com.auth.authservice.dto.LoginResponseDto;
import com.auth.authservice.dto.RegisterRequestDto;
import com.auth.authservice.service.auth.IAuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
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

	public ResponseEntity<Void> logout() {
		return ResponseEntity.ok().build();
	}

	public ResponseEntity<Void> refreshToken() {
		return ResponseEntity.ok().build();
	}

	public ResponseEntity<Void> forgotPassword() {
		return ResponseEntity.ok().build();
	}

	public ResponseEntity<Void> resetPassword() {
		return ResponseEntity.ok().build();
	}
}
