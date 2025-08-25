package com.example.api.dto.order;

public record PaymentInitResponse(
	String orderId,
	String orderName,
	int amount,
	String customerKey,   // 게스트면 백엔드에서 생성하여 내려도 됨
	String successUrl,
	String failUrl
) {

}