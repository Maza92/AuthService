package com.auth.authservice.security.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth.authservice.exception.ApiExceptionFactory;
import com.auth.authservice.security.object.JwtAuthenticationToken;
import com.auth.authservice.security.provider.JwtAuthenticationProvider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtParser jwtParser;
	private final ApiExceptionFactory apiExceptionFactory;
	private final JwtAuthenticationProvider authenticationProvider;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			String token = authHeader.substring(7);
			Claims claims = extractClaims(token);

			JwtAuthenticationToken authentication = new JwtAuthenticationToken(token, claims);

			Authentication authenticated = authenticationProvider.authenticate(authentication);
			SecurityContextHolder.getContext().setAuthentication(authenticated);

		} catch (Exception e) {
			SecurityContextHolder.clearContext();
		}

		filterChain.doFilter(request, response);
	}

	private Claims extractClaims(String token) {
		try {
			return jwtParser.parseSignedClaims(token).getPayload();
		} catch (Exception e) {
			throw apiExceptionFactory.authException("auth.token.invalid");
		}
	}
}
