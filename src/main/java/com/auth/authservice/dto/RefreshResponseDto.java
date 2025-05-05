package com.auth.authservice.dto;

import java.time.Instant;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class RefreshResponseDto {
	private String accessToken;
	private String refreshToken;
	private Instant expiration;
}
