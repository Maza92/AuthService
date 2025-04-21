package com.auth.authservice.exception;

import org.springframework.stereotype.Component;

import com.auth.authservice.exception.exceptions.AlreadyExistsException;
import com.auth.authservice.exception.exceptions.AuthException;
import com.auth.authservice.exception.exceptions.BusinessException;
import com.auth.authservice.exception.exceptions.EntityException;
import com.auth.authservice.exception.exceptions.EntityNotFoundException;
import com.auth.authservice.exception.exceptions.ValidationException;
import com.auth.authservice.utils.LocaleUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ApiExceptionFactory {
	private final LocaleUtils localeUtils;

	public AuthException authException(String messageKey, Object... args) {
		return new AuthException(messageKey, localeUtils.getCurrentLocale(), args);
	}

	public EntityException entityNotFoundGeneric(String entityName, Object criteria) {
		return new EntityException("exception.entity.not.found.generic", localeUtils.getCurrentLocale(), entityName,
				criteria);
	}

	public EntityNotFoundException entityNotFound(String messageKey, Object... args) {
		return new EntityNotFoundException(messageKey, localeUtils.getCurrentLocale(), args);
	}

	public AlreadyExistsException conflictException(String messageKey, Object... args) {
		return new AlreadyExistsException(messageKey, localeUtils.getCurrentLocale(), args);
	}

	public ValidationException validationException(String messageKey, Object... args) {
		return new ValidationException(messageKey, localeUtils.getCurrentLocale(), args);
	}

	public BusinessException businessException(String messageKey, Object... args) {
		return new BusinessException(messageKey, localeUtils.getCurrentLocale(), args);
	}
}