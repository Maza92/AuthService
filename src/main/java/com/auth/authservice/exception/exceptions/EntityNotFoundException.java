package com.auth.authservice.exception.exceptions;

import java.util.Locale;

import com.auth.authservice.exception.base.BaseException;

public class EntityNotFoundException extends BaseException {
	public EntityNotFoundException(String messageKey, Locale locale, Object... args) {
		super(messageKey, locale, args);
	}
}
