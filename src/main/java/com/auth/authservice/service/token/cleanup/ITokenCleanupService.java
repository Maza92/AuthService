package com.auth.authservice.service.token.cleanup;

public interface ITokenCleanupService {
	void deleteExpiredTokens();

	void deleteRevokedTokens();

	void deleteInactiveTokens();
}
