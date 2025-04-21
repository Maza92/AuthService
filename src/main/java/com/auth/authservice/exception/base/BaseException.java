package com.auth.authservice.exception.base;

import java.util.Locale;

import org.springframework.context.MessageSource;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
	private final String messageKey;
	private final Locale locale;
	private final Object[] args;

	protected BaseException(String messageKey, Locale locale, Object... args) {
		this.messageKey = messageKey;
		this.locale = locale;
		this.args = args;
	}

	public String getResolvedMessage(MessageSource messageSource) {
		return messageSource.getMessage(messageKey, args, locale);
	}
}
