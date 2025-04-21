package com.auth.authservice.security.provider;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth.authservice.entity.UserEntity;
import com.auth.authservice.exception.ApiExceptionFactory;
import com.auth.authservice.repository.UserRepository;
import com.auth.authservice.security.object.JwtAuthenticationToken;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final UserRepository userRepository;
	private final ApiExceptionFactory apiExceptionFactory;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		JwtAuthenticationToken authToken = (JwtAuthenticationToken) authentication;
		Claims claims = authToken.getClaims();

		String userId = claims.getSubject();
		if (userId == null) {
			throw apiExceptionFactory.authException("auth.token.missing.subject");
		}

		UserEntity user = userRepository.findById(Long.valueOf(userId))
				.orElseThrow(() -> apiExceptionFactory.entityNotFound("user", userId));

		if (!user.isActive()) {
			throw apiExceptionFactory.authException("auth.user.inactive");
		}

		List<GrantedAuthority> authorities = Collections
				.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));

		return new JwtAuthenticationToken(
				(String) authToken.getCredentials(),
				claims,
				user,
				authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthenticationToken.class.isAssignableFrom(authentication);
	}
}