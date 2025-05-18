package com.auth.authservice.service.token.cleanup.impl;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth.authservice.repository.JwtRepository;
import com.auth.authservice.service.token.cleanup.ITokenCleanupService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenCleanupServiceImpl implements ITokenCleanupService {

	private final JwtRepository tokenRepository;

	@Override
	@Transactional
	public void deleteExpiredTokens() {
		int deletedCount = tokenRepository.deleteExpiredTokens(Instant.now());
		System.out.println("Deleted expired tokens: " + deletedCount);
	}

	@Override
	@Transactional
	public void deleteRevokedTokens() {
		int deletedCount = tokenRepository.deleteRevokedTokens();
		System.out.println("Deleted revoked tokens: " + deletedCount);
	}

	@Override
	@Transactional
	public void deleteInactiveTokens() {
		int deletedCount = tokenRepository.deleteInactiveTokens(Instant.now());
		System.out.println("Deleted inactive tokens: " + deletedCount);
	}
}
