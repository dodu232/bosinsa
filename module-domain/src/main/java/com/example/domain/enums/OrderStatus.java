package com.example.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
	CREATED("주문 생성"),
	PAID("결제 완료"),
	CANCELLED("주문 취소"),
	FULFILLING("주문 준비중"),
	SHIPPED("배송 중"),
	DELIVERED("배송 완료");

	private final String status;
}
