package com.auth.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {
	@Email
	@NotBlank
	private String email;

	@NotBlank
	@Size(min = 8, message = "Password must be at least 8 characters long")
	private String password;

	@NotBlank
	private String userName;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;
}