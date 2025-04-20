package com.auth.authservice.constant;

import java.util.List;

public final class LanguageConstants {
	private LanguageConstants() {
		throw new IllegalStateException("Constants class");
	}

	public static final String DEFAULT_LOCALE = "en";
	public static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";

	private static final List<String> MESSAGE_SOURCES = List.of(
			"classpath:locale/message",
			"classpath:validation/validation",
			"classpath:api/api-doc");

	public static List<String> getMessageSources() {
		return MESSAGE_SOURCES;
	}
}
