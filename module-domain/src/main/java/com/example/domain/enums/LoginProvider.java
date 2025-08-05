package com.example.domain.enums;

import com.example.common.exception.ApiException;
import com.example.common.exception.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginProvider {
	LOCAL("local"),
	KAKAO("kakao");

	private final String provider;

	public static LoginProvider from(String provider) {
		for (LoginProvider lp : values()) {
			if (lp.provider.equalsIgnoreCase(provider)) {
				return lp;
			}
		}
		throw new ApiException("지원하지 않는 로그인 제공자입니다: " + provider, ErrorType.INVALID_PARAMETER);
	}
}
