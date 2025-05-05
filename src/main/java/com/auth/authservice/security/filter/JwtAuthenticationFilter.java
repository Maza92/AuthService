package com.auth.authservice.security.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth.authservice.exception.ApiExceptionFactory;
import com.auth.authservice.repository.JwtRepository;
import com.auth.authservice.security.object.JwtAuthenticationToken;
import com.auth.authservice.security.provider.JwtAuthenticationProvider;
import com.auth.authservice.service.token.IJwtTokenService;
import com.auth.authservice.service.token.validator.ITokenValidationService;

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

	private final JwtAuthenticationProvider authenticationProvider;
	private final IJwtTokenService jwtTokenService;
	private final ITokenValidationService tokenValidationService;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		String token = this.jwtTokenService.extractTokenHeader(request);

		if (token == null) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			Claims claims = tokenValidationService.validateToken(token);

			JwtAuthenticationToken authentication = new JwtAuthenticationToken(token, claims);

			Authentication authenticated = authenticationProvider.authenticate(authentication);
			SecurityContextHolder.getContext().setAuthentication(authenticated);

		} catch (Exception e) {
			SecurityContextHolder.clearContext();
		}

		filterChain.doFilter(request, response);
	}
}
