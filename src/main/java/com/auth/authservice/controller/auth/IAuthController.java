package com.auth.authservice.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.auth.authservice.dto.ApiErrorDto;
import com.auth.authservice.dto.LoginRequestDto;
import com.auth.authservice.dto.LoginResponseDto;
import com.auth.authservice.dto.RegisterRequestDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Auth", description = "Controller for authentication")
public interface IAuthController {

	@PostMapping("/login")
	@Operation(summary = "Login", description = "Authenticate user")
	@ApiResponse(responseCode = "200", description = "User authenticated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDto.class)))
	@ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class), examples = @ExampleObject(name = "ValidationErrorExample", value = """
			{
			    "status": 400,
				"message": "Validation error",
				"timestamp": "2025-04-22T00:44:52.345768198Z",
				"errors": [
					"email: must not be blank",
					"password: must not be blank"
				]
			}
			""")))
	@ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class), examples = {
			@ExampleObject(name = "InvalidCredentialsExample", value = """
					{
						"status": 401,
						"message": "Invalid credentials",
						"timestamp": "2025-04-22T00:44:52.345768198Z",
						"path": "/api/v1/api/auth/login",
						"method": "POST",
						"errors": []
					}
					"""), @ExampleObject(name = "UserInactiveExample", value = """
					{
						"status": 401,
						"message": "User is inactive",
						"timestamp": "2025-04-22T00:44:52.345768198Z",
						"path": "/api/v1/api/auth/login",
						"method": "POST",
						"errors": []
					}
					"""), @ExampleObject(name = "MaxJwtCountExample", value = """
					{
						"status": 401,
						"message": "The maximum number of active sessions has been reached",
						"timestamp": "2025-04-22T00:44:52.345768198Z",
						"path": "/api/v1/api/auth/login",
						"method": "POST",
						"errors": []
					}
					""") }))
	ResponseEntity<LoginResponseDto> login(LoginRequestDto request);

	@PostMapping("/register")
	@Operation(summary = "Register", description = "Register new user")
	@ApiResponse(responseCode = "201", description = "User created successfully")
	@ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class), examples = {
			@ExampleObject(name = "ValidationErrorExample", value = """
					{
					    "status": 400,
						"message": "Validation error",
						"timestamp": "2025-04-22T00:44:52.345768198Z",
						"errors": [
							"userName: must not be blank",
							"password: must not be blank",
							"lastName: must not be blank",
							"firstName: must not be blank",
							"email: must not be blank"
						]
					}
					"""), @ExampleObject(name = "OperationErrorExample", value = """
					{
						"status": 400,
						"message": "Operation error",
						"timestamp": "2025-04-22T00:44:52.345768198Z",
						"errors": []
					}
					""") }))
	@ApiResponse(responseCode = "409", description = "Email already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class), examples = @ExampleObject(name = "EmailExistsExample", value = """
			{
				"status": 409,
				"message": "The email already exists",
				"timestamp": "2025-04-22T00:55:19.644611203Z",
				"path": "/api/v1/api/auth/register",
				"method": "POST",
				"errors": []
			}
			""")))
	@ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class), examples = @ExampleObject(name = "InternalServerErrorExample", value = """
			{
				"status": 500,
				"message": "Internal Server Error",
				"timestamp": "2025-04-22T00:44:52.345768198Z",
				"path": "/api/v1/api/auth/register",
				"method": "POST",
				"errors": []
			}
			""")))
	@Parameter(name = "Accept-Language", in = ParameterIn.HEADER, required = true, description = "Language code (e.g., 'en' for English, 'es' for Spanish)", example = "en", schema = @Schema(type = "string", defaultValue = "en"))
	ResponseEntity<Void> register(
			@Parameter(description = "User registration request") @Valid @RequestBody RegisterRequestDto request);
}
