package com.auth.authservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordDto {

	@NotBlank(message = "{validation.email.required}")
	@Email(message = "{validation.email.format}")
	private String email;
}
