package com.example.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
	NO_RESOURCE(HttpStatus.NOT_FOUND, "존재하지 않는 데이터입니다."),
	INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 파라미터 요청입니다."),
	EXTERNAL_API_ERROR(HttpStatus.BAD_GATEWAY, "외부 API 호출 에러입니다."),
	NOT_LOGGED_IN(HttpStatus.UNAUTHORIZED, "로그인이 필요한 사용자입니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 에러입니다.");

	private final HttpStatus status;
	private final String desc;
}