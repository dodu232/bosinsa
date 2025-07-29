package com.example.common.response;

public class ApiResponse<T> {

	private boolean success;
	private T data;
	private T error;

	public static <T> ApiResponse<T> success(T data) {
		ApiResponse<T> response = new ApiResponse<>();
		response.success = true;
		response.data = data;
		return response;
	}

	public static <T> ApiResponse<T> fail(T error) {
		ApiResponse<T> response = new ApiResponse<>();
		response.success = false;
		response.error = error;
		return response;
	}
}
