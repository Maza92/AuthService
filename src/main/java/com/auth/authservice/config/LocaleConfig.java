package com.auth.authservice.config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import com.auth.authservice.constant.LanguageConstants;

@Configuration
public class LocaleConfig {
	private static final List<Locale> SUPPORTED_LOCALES = Arrays.asList(
			Locale.forLanguageTag(LanguageConstants.DEFAULT_LOCALE),
			Locale.forLanguageTag("es"));

	@Bean
	public LocaleResolver localeResolver() {
		var resolver = new AcceptHeaderLocaleResolver();
		resolver.setSupportedLocales(LocaleConfig.SUPPORTED_LOCALES);
		resolver.setDefaultLocale(Locale.forLanguageTag(LanguageConstants.DEFAULT_LOCALE));
		return resolver;
	}
}
