package com.auth.authservice.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.auth.authservice.dto.LoginRequestDto;
import com.auth.authservice.dto.LoginResponseDto;
import com.auth.authservice.dto.RegisterRequestDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Auth", description = "Controller for authentication")
public interface IAuthController {

	@Operation(summary = "Login", description = "Authenticate user")
	@PostMapping("/login")
	ResponseEntity<LoginResponseDto> login(LoginRequestDto request);

	@Operation(summary = "Register", description = "Register new user")
	@PostMapping("/register")
	@ApiResponse(responseCode = "201", description = "User created successfully")
	@ApiResponse(responseCode = "400", description = "Invalid request")
	@ApiResponse(responseCode = "409", description = "Email already exists")
	@ApiResponse(responseCode = "500", description = "Unexpected error")
	@Parameter(name = "Accept-Language", in = ParameterIn.HEADER, required = true, description = "Language code (e.g., 'en' for English, 'es' for Spanish)", example = "en", schema = @Schema(type = "string", defaultValue = "en"))
	ResponseEntity<Void> register(
			@Parameter(description = "User registration request") @Valid @RequestBody RegisterRequestDto request);
}
