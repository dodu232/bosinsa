package com.example.api.dto.order;

import lombok.Builder;

@Builder
public record PaymentInitResponse(
	String orderId,
	String orderName,
	int amount,
	String customerEmail
) {

}