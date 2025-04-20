package com.auth.authservice.handler;

import java.time.Instant;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth.authservice.dto.ApiError;
import com.auth.authservice.exception.base.BaseException;
import com.auth.authservice.exception.exceptions.AuthException;
import com.auth.authservice.exception.exceptions.BusinessException;
import com.auth.authservice.exception.exceptions.EntityException;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
	private final MessageSource messageSource;

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ApiError> handleBaseException(BaseException ex) {
		String message = messageSource.getMessage(
				ex.getMessageKey(),
				ex.getArgs(),
				ex.getMessageKey(),
				ex.getLocale());

		HttpStatus status = determineHttpStatus(ex);

		return ResponseEntity
				.status(status)
				.body(new ApiError(status.value(), message, Instant.now()));
	}

	private HttpStatus determineHttpStatus(BaseException ex) {
		if (ex instanceof AuthException) {
			return HttpStatus.UNAUTHORIZED;
		} else if (ex instanceof EntityException) {
			return HttpStatus.NOT_FOUND;
		} else if (ex instanceof BusinessException) {
			return HttpStatus.BAD_REQUEST;
		}
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
}
