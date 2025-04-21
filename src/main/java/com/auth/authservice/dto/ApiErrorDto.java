package com.auth.authservice.dto;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class ApiErrorDto {
	private int status;
	private String message;
	private Instant timestamp;
	private String path;
	private String method;
	private List<String> errors;

	public ApiErrorDto(int status, String message, Instant timestamp) {
		this.status = status;
		this.message = message;
		this.timestamp = timestamp;
		this.errors = List.of();
	}

	public ApiErrorDto(int status, String message, Instant timestamp, String path, String method) {
		this(status, message, timestamp);
		this.path = path;
		this.method = method;
	}

	public ApiErrorDto(int status, String message, Instant timestamp, List<String> errors) {
		this(status, message, timestamp);
		this.errors = errors;
	}
}
