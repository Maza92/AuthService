package com.auth.authservice.exception;

import org.springframework.stereotype.Component;

import com.auth.authservice.exception.exceptions.AuthException;
import com.auth.authservice.exception.exceptions.BusinessException;
import com.auth.authservice.exception.exceptions.EntityException;
import com.auth.authservice.utils.LocaleUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ApiExceptionFactory {
	private final LocaleUtils localeUtils;

	public AuthException authException(String messageKey, Object... args) {
		return new AuthException(messageKey, localeUtils.getCurrentLocale(), args);
	}

	public EntityException entityNotFound(String entityName, Object id) {
		return new EntityException(
				"exception.entity.not.found",
				localeUtils.getCurrentLocale(),
				entityName, id);
	}

	public BusinessException businessException(String messageKey, Object... args) {
		return new BusinessException(messageKey, localeUtils.getCurrentLocale(), args);
	}
}