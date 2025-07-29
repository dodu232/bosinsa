package com.example.common.response;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class FieldErrorResponse {

	private String code;
	private Map<String, String> fieldErrors = new HashMap<>();
}
