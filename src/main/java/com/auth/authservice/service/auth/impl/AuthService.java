package com.auth.authservice.service.auth.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth.authservice.dto.LoginRequestDto;
import com.auth.authservice.dto.LoginResponseDto;
import com.auth.authservice.dto.RegisterRequestDto;
import com.auth.authservice.entity.RoleEntity;
import com.auth.authservice.entity.UserEntity;
import com.auth.authservice.enums.RoleEnum;
import com.auth.authservice.exception.ApiExceptionFactory;
import com.auth.authservice.repository.RoleRepository;
import com.auth.authservice.repository.UserRepository;
import com.auth.authservice.service.auth.IAuthService;
import com.auth.authservice.service.token.IJwtTokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final IJwtTokenService jwtTokenService;
	private final ApiExceptionFactory apiExceptionFactory;

	@Override
	public LoginResponseDto login(LoginRequestDto request) {
		UserEntity user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> apiExceptionFactory.authException("auth.invalid.credentials"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw apiExceptionFactory.authException("auth.invalid.credentials");
		}

		String token = jwtTokenService.generateToken(user);

		return new LoginResponseDto(token);
	}

	@Transactional
	@Override
	public void register(RegisterRequestDto request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw apiExceptionFactory.conflictException("auth.email.exists");
		}

		UserEntity user = new UserEntity()
				.setEmail(request.getEmail())
				.setPassword(passwordEncoder.encode(request.getPassword()))
				.setUsername(request.getUserName())
				.setFirstName(request.getFirstName())
				.setLastName(request.getLastName())
				.setActive(true);

		RoleEntity userRole = roleRepository.findByName(RoleEnum.USER)
				.orElseThrow(() -> apiExceptionFactory.entityNotFoundGeneric("role", RoleEnum.USER));

		user.setRole(userRole);

		userRepository.save(user);
	}
}
