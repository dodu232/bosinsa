package com.example.api.usecase.order;

import com.example.api.dto.order.PaymentInitResponse;

public interface GetOrderUseCase {

	PaymentInitResponse getOrder(Long orderId);
}
