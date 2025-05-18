package com.auth.authservice.constant;

import java.util.List;

public class SecurityConstants {
	private SecurityConstants() {
		throw new IllegalStateException("Constants class");
	}

	private static final List<String> PUBLIC_ENDPOINTS = List.of(
			"/auth/login",
			"/auth/register",
			"/swagger-ui/**",
			"/v3/api-docs/**",
			"/auth/refresh-token",
			"/auth/forgot-password",
			"/auth/verify-reset-code",
			"/auth/recover-password");

	public static String[] getPublicEndpoints() {
		return PUBLIC_ENDPOINTS.toArray(new String[0]);
	}

}
