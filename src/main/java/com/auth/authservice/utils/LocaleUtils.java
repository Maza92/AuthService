package com.auth.authservice.utils;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LocaleUtils {

	public Locale getCurrentLocale() {
		return LocaleContextHolder.getLocale();
	}
}
