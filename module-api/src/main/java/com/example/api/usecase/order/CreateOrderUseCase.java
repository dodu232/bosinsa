package com.example.api.usecase.order;

import com.example.api.dto.order.OrderRequest;
import com.example.api.dto.order.OrderResponse;

public interface CreateOrderUseCase {

	OrderResponse.Create createOrder(Long userId, OrderRequest.Create dto);

}
