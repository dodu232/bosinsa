package com.example.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

	private final String message;
	private final ErrorType errorType;
	private final HttpStatus httpStatus;

	public ApiException(String message, ErrorType errorType, HttpStatus httpStatus) {
		this.message = message;
		this.errorType = errorType;
		this.httpStatus = httpStatus;
	}

	public ApiException(String message, ErrorType errorType) {
		this.message = message;
		this.errorType = errorType;
		this.httpStatus = errorType.getStatus();
	}
}