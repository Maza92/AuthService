package com.auth.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
	@NotBlank(message = "{validation.email.required}")
	@Email(message = "{validation.email.format}")
	private String email;

	@NotBlank(message = "{validation.password.required}")
	private String password;
}
