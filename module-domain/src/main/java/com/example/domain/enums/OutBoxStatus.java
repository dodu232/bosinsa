package com.example.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OutBoxStatus {
	PENDING("발행 대기"),
	PROCESSED("발행 완료"),
	FAILED("발행 실패");

	private final String desc;
}
