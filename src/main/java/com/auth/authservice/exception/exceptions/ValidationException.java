package com.auth.authservice.exception.exceptions;

import java.util.Locale;

import com.auth.authservice.exception.base.BaseException;

public class ValidationException extends BaseException {
	public ValidationException(String messagekey, Locale locale, Object... args) {
		super(messagekey, locale, args);
	}
}
