package com.example.api.usecase.order;

import com.example.api.dto.order.OrderRequest;
import com.example.api.dto.order.OrderResponse;
import org.springframework.stereotype.Service;

@Service
public interface CreateOrderUseCase {

	OrderResponse.Create createOrder(Long userId, OrderRequest.Create dto);

}
