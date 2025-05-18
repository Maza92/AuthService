package com.auth.authservice.service.auth.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.aspectj.weaver.patterns.IToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth.authservice.dto.ForgotPasswordDto;
import com.auth.authservice.dto.LoginRequestDto;
import com.auth.authservice.dto.LoginResponseDto;
import com.auth.authservice.dto.RecoverPasswordDto;
import com.auth.authservice.dto.RefreshRequestDto;
import com.auth.authservice.dto.RefreshResponseDto;
import com.auth.authservice.dto.RegisterRequestDto;
import com.auth.authservice.dto.ResetCodeDto;
import com.auth.authservice.dto.ResetPasswordDto;
import com.auth.authservice.dto.ResetTokenDto;
import com.auth.authservice.entity.ResetCodeEntity;
import com.auth.authservice.entity.RoleEntity;
import com.auth.authservice.entity.UserEntity;
import com.auth.authservice.enums.RoleEnum;
import com.auth.authservice.exception.ApiExceptionFactory;
import com.auth.authservice.repository.JwtRepository;
import com.auth.authservice.repository.ResetCodeRepository;
import com.auth.authservice.repository.RoleRepository;
import com.auth.authservice.repository.UserRepository;
import com.auth.authservice.service.auth.IAuthService;
import com.auth.authservice.service.email.EmailService;
import com.auth.authservice.service.email.EmailTemplateService;
import com.auth.authservice.service.token.IJwtTokenService;
import com.auth.authservice.service.token.validator.ITokenValidationService;
import com.auth.authservice.utils.MessageUtils;

import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

	private final UserRepository userRepository;
	private final JwtRepository jwtRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final ApiExceptionFactory apiExceptionFactory;
	private final ResetCodeRepository resetCodeRepository;

	private final IJwtTokenService jwtTokenService;
	private final ITokenValidationService tokenValidationService;
	private final EmailTemplateService emailTemplateService;
	private final EmailService emailService;
	private final MessageUtils messageUtils;

	@Value("${security.jwt.max-jwt-count}")
	int MAX_JWT_COUNT;

	@Value("${security.jwt.max-reset-code-attempts}")
	int MAX_RESET_CODE_ATTEMPTS;

	@Override
	public LoginResponseDto login(LoginRequestDto request) {

		UserEntity user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> apiExceptionFactory.authException("auth.invalid.credentials"));

		if (!user.isActive()) {
			throw apiExceptionFactory.authException("auth.user.inactive");
		}

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw apiExceptionFactory.authException("auth.invalid.credentials");
		}

		if (jwtRepository
				.countActiveTokensByUser(user.getId().longValue()) > MAX_JWT_COUNT) {
			throw apiExceptionFactory.authException("auth.max.jwt.count");
		}

		return jwtTokenService.generateLoginUserTokens(user);
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

	@Transactional
	@Override
	public void logout(HttpServletRequest request) {
		String token = jwtTokenService.extractTokenHeader(request);

		try {
			jwtTokenService.revokeToken(token);
		} catch (Exception e) {
			throw apiExceptionFactory.businessException("exception.unexpected", e);
		}
	}

	@Override
	public RefreshResponseDto refreshToken(RefreshRequestDto refreshRequest) {
		Claims claims = tokenValidationService.validateRefreshToken(refreshRequest.getRefreshToken());

		String userId = claims.getSubject();
		UserEntity user = userRepository.findById(Long.valueOf(userId))
				.orElseThrow(() -> apiExceptionFactory.entityNotFoundGeneric("user", userId));

		return jwtTokenService.generateRefreshUserTokens(user, refreshRequest.getRefreshToken());
	}

	@Override
	@Transactional
	public void forgotPassword(ForgotPasswordDto request) {
		UserEntity user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> apiExceptionFactory.entityNotFoundGeneric("user", request.getEmail()));

		resetCodeRepository.invalidateAllUserCodes(user.getId());

		String verificationCode = generateVerificationCode();

		Map<String, Object> templateVariables = new HashMap<>();
		templateVariables.put("userName", user.getFirstName());
		templateVariables.put("verificationCode", verificationCode);

		String htmlContent = emailTemplateService.generateEmailContent(
				"password-reset",
				templateVariables);

		try {
			emailService.sendHtmlEmail(
					user.getEmail(),
					messageUtils.getMessage("email.password.reset.subject"),
					htmlContent);

			ResetCodeEntity resetCode = new ResetCodeEntity()
					.setExpiresAt(Instant.now().plusSeconds(60 * 60))
					.setCode(verificationCode)
					.setUser(user);

			resetCodeRepository.save(resetCode);
		} catch (MessagingException e) {
			throw apiExceptionFactory.businessException("exception.email.send.error", e);
		}

	}

	@Override
	public ResetTokenDto verifyResetCode(ResetCodeDto request) {
		UserEntity user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> apiExceptionFactory.entityNotFoundGeneric("user", request.getEmail()));

		ResetCodeEntity code;
		try {
			code = resetCodeRepository
					.findCodeByUserAndCode(request.getCode(), user.getId())
					.orElseThrow(() -> apiExceptionFactory.businessException("auth.reset.code.invalid"));
		} catch (Exception e) {
			throw apiExceptionFactory.businessException("auth.reset.code.invalid");
		}

		if (code.getExpiresAt().isBefore(Instant.now()))
			throw apiExceptionFactory.businessException("auth.reset.code.expired");

		if (code.getAttempts() >= MAX_RESET_CODE_ATTEMPTS)
			throw apiExceptionFactory.authException("resetCode.too.many.attempts");

		code.setAttempts(code.getAttempts() + 1);
		code.setUsed(true);
		resetCodeRepository.save(code);
		return jwtTokenService.generatePasswordResetToken(user);
	}

	@Override
	@Transactional
	public void recoverPassword(RecoverPasswordDto request) {
		Claims claims = tokenValidationService.validateResetToken(request.getResetToken());

		String userId = claims.getSubject();
		UserEntity user = userRepository.findById(Long.valueOf(userId))
				.orElseThrow(() -> apiExceptionFactory.entityNotFoundGeneric("user", userId));
		jwtRepository.revokeToken(request.getResetToken());
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);
	}

	@Override
	public void resetPassword(ResetPasswordDto request, HttpServletRequest httpRequest) {
		String token = jwtTokenService.extractTokenHeader(httpRequest);
		Claims claims = tokenValidationService.validateToken(token);

		String userId = claims.getSubject();
		UserEntity user = userRepository.findById(Long.valueOf(userId))
				.orElseThrow(() -> apiExceptionFactory.entityNotFoundGeneric("user", userId));

		if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
			throw apiExceptionFactory.authException("auth.invalid.old.password");
		}

		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);
	}

	private String generateVerificationCode() {
		return String.format("%06d", new Random().nextInt(1_000_000));
	}
}
