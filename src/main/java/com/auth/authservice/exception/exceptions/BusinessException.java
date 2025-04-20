package com.auth.authservice.exception.exceptions;

import java.util.Locale;

import com.auth.authservice.exception.base.BaseException;

public class BusinessException extends BaseException {
	public BusinessException(String messageKey, Locale locale, Object... args) {
		super(messageKey, locale, args);
	}
}
