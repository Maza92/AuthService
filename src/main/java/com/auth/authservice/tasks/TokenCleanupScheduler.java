package com.auth.authservice.tasks;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.auth.authservice.service.token.cleanup.ITokenCleanupService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {
	private final ITokenCleanupService tokenCleanupService;

	@Scheduled(cron = "${security.token.cleanup.expired.cron}")
	public void scheduleExpiredTokenCleanup() {
		tokenCleanupService.deleteExpiredTokens();
	}

	@Scheduled(cron = "${security.token.cleanup.revoked.cron}")
	public void scheduleRevokedTokenCleanup() {
		tokenCleanupService.deleteRevokedTokens();
	}

	@Scheduled(cron = "${security.token.cleanup.inactive.cron}")
	public void scheduleInactiveTokenCleanup() {
		tokenCleanupService.deleteInactiveTokens();
	}

}
