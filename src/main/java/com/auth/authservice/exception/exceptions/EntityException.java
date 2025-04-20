package com.auth.authservice.exception.exceptions;

import java.util.Locale;

import com.auth.authservice.exception.base.BaseException;

public class EntityException extends BaseException {
	public EntityException(String messageKey, Locale locale, Object... args) {
		super(messageKey, locale, args);
	}
}
