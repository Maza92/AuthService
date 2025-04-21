package com.auth.authservice.service.auth.impl;

import java.util.Collections;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.authservice.dto.LoginRequest;
import com.auth.authservice.dto.LoginResponse;
import com.auth.authservice.dto.RegisterRequest;
import com.auth.authservice.entity.RoleEntity;
import com.auth.authservice.entity.UserEntity;
import com.auth.authservice.enums.RoleEnum;
import com.auth.authservice.exception.ApiExceptionFactory;
import com.auth.authservice.repository.RoleRepository;
import com.auth.authservice.repository.UserRepository;
import com.auth.authservice.service.token.IJwtTokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final IJwtTokenService jwtTokenService;
	private final ApiExceptionFactory apiExceptionFactory;

	public LoginResponse login(LoginRequest request) {
		UserEntity user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> apiExceptionFactory.authException("auth.invalid.credentials"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw apiExceptionFactory.authException("auth.invalid.credentials");
		}

		String token = jwtTokenService.generateToken(user);

		return new LoginResponse(token);
	}

	public void register(RegisterRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw apiExceptionFactory.businessException("auth.email.exists");
		}

		UserEntity user = new UserEntity();
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setActive(true);

		RoleEntity userRole = roleRepository.findByName(RoleEnum.USER.name())
				.orElseThrow(() -> apiExceptionFactory.entityNotFound("role", RoleEnum.USER.name()));
		user.setRoles(Collections.singleton(userRole));

		userRepository.save(user);
	}
}
