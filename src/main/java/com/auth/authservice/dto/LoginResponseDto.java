package com.auth.authservice.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
	private String userId;
	private String username;
	private String token;
	private Instant expiration;
	private String refreshToken;
}
