package com.example.common.response;

public class ErrorResponse {

	private String code;
	private String message;

	public static ErrorResponse of(String code, String message) {
		ErrorResponse e = new ErrorResponse();
		e.code = code;
		e.message = message;
		return e;
	}
}
