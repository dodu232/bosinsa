package com.example.common.response;

import java.util.HashMap;
import java.util.Map;

public class FieldErrorResponse {

	private String code;
	private Map<String, String> fieldErrors = new HashMap<>();

	public static FieldErrorResponse of(String code, Map<String, String> fieldErrors) {
		return new FieldErrorResponse(code, fieldErrors);
	}

	private FieldErrorResponse(String code, Map<String, String> fieldErrors) {
		this.code = code;
		this.fieldErrors = fieldErrors;
	}
}
