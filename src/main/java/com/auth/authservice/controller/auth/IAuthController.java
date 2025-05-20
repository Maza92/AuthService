package com.auth.authservice.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.auth.authservice.dto.ApiErrorDto;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.auth.authservice.controller.baseDoc.AcceptLanguageHeader;

@Tag(name = "Auth", description = "Controller for authentication")
public interface IAuthController {

	@PostMapping("/login")
	@Operation(summary = "Login", description = "Authenticate user")
	@ApiResponse(responseCode = "200", description = "User authenticated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDto.class)))
	@ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
	@ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
	@AcceptLanguageHeader
	ResponseEntity<LoginResponseDto> login(LoginRequestDto request);

	@PostMapping("/register")
	@Operation(summary = "Register", description = "Register new user")
	@ApiResponse(responseCode = "201", description = "User created successfully")
	@ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
	@ApiResponse(responseCode = "409", description = "Email already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
	@ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
	@AcceptLanguageHeader
	ResponseEntity<Void> register(
			@Parameter(description = "User registration request") @Valid @RequestBody RegisterRequestDto request);

	@PostMapping("/logout")
	@Operation(summary = "Logout", description = "Logout user")
	@ApiResponse(responseCode = "200", description = "User logged out successfully")
	@ApiResponse(responseCode = "400", description = "Invalid token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
	@AcceptLanguageHeader
	@SecurityRequirement(name = "Auth")
	ResponseEntity<Void> logout(HttpServletRequest request);

	@PostMapping("/refresh-token")
	@AcceptLanguageHeader
	ResponseEntity<RefreshResponseDto> refreshToken(
			@Parameter(description = "Refresh token request") @Valid @RequestBody RefreshRequestDto refreshRequest);

	@PostMapping("/forgot-password")
	@Operation(summary = "Forgot Password", description = "Send password reset code to user's email")
	@ApiResponse(responseCode = "200", description = "Reset code sent successfully")
	@ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
	@ApiResponse(responseCode = "500", description = "Error sending email", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
	@AcceptLanguageHeader
	ResponseEntity<Void> forgotPassword(
			@Parameter(description = "Forgot password request") @Valid @RequestBody ForgotPasswordDto request);

	@PostMapping("/verify-reset-code")
	@Operation(summary = "Verify Reset Code", description = "Verify the reset code sent to user's email")
	@ApiResponse(responseCode = "200", description = "Reset code verified successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResetTokenDto.class)))
	@ApiResponse(responseCode = "404", description = "Invalid code or user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
	@ApiResponse(responseCode = "401", description = "Too many attempts", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
	@AcceptLanguageHeader
	ResponseEntity<ResetTokenDto> verifyResetCode(
			@Parameter(description = "Reset code verification request") @Valid @RequestBody ResetCodeDto request);

	@PostMapping("/recover-password")
	@Operation(summary = "Recover Password", description = "Set new password using reset token")
	@ApiResponse(responseCode = "200", description = "Password recovered successfully")
	@ApiResponse(responseCode = "401", description = "Invalid reset token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
	@ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
	@AcceptLanguageHeader
	ResponseEntity<Void> recoverPassword(
			@Parameter(description = "Recover password request") @Valid @RequestBody RecoverPasswordDto request);

	@PostMapping("/reset-password")
	@Operation(summary = "Reset Password", description = "Change password when user is logged in")
	@ApiResponse(responseCode = "200", description = "Password reset successfully")
	@ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
	@ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
	@AcceptLanguageHeader
	@SecurityRequirement(name = "Auth")
	ResponseEntity<Void> resetPassword(
			@Parameter(description = "Reset password request") @Valid @RequestBody ResetPasswordDto request,
			HttpServletRequest httpRequest);

	@PostMapping("/reset-password-by-admin/{userId}")
	@Operation(summary = "Reset Password By Admin", description = "Change password for admin")
	@ApiResponse(responseCode = "200", description = "Password reset successfully")
	@ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
	@ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
	@AcceptLanguageHeader
	@SecurityRequirement(name = "Auth")
	ResponseEntity<Void> resetPasswordByAdmin(
			@Parameter(description = "Reset password request") @Valid @RequestBody ResetPasswordByAdminDto request,
			@Parameter(description = "User id") @PathVariable Integer userId);
}
