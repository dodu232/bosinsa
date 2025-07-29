package com.example.common.exception;


import com.example.common.response.ApiResponse;
import com.example.common.response.ErrorResponse;
import com.example.common.response.FieldErrorResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<FieldErrorResponse>> handleValidationExceptions(
		MethodArgumentNotValidException e) {
		Map<String, String> errors = new HashMap<>();

		for (FieldError error : e.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}

		FieldErrorResponse fieldErrorResponse = FieldErrorResponse.of(
			ErrorType.INVALID_PARAMETER.toString(),
			errors
		);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(ApiResponse.fail(fieldErrorResponse));
	}

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleApiException(ApiException e) {
		log.error("ApiException occurred. type={} message={} className={}", e.getErrorType(),
			e.getMessage(), e.getClass().getName());
		return ResponseEntity.status(e.getHttpStatus())
			.body(ApiResponse.fail(ErrorResponse.of(e.getErrorType().toString(), e.getMessage())));
	}
}
