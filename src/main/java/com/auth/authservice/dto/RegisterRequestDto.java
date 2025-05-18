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
	@Email(message = "{validation.email.format}")
	@NotBlank(message = "{validation.email.required}")
	private String email;

	@NotBlank(message = "{validation.password.required}")
	@Size(min = 8, message = "{validation.password.size}")
	private String password;

	@NotBlank(message = "{validation.username.required}")
	private String userName;

	@NotBlank(message = "{validation.firstname.required}")
	private String firstName;

	@NotBlank(message = "{validation.lastname.required}")
	private String lastName;
}