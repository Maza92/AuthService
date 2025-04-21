package com.auth.authservice.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

@SpringBootTest
public class MessageConfigTest {
	@Autowired
	private MessageSource messageSource;

	@Test
	void testMessageResolution() {
		String message = messageSource.getMessage(
				"exception.entity.not.found",
				new Object[] { "User", 1 },
				Locale.getDefault());
		assertEquals("Could not find User with id 1", message);
	}
}
