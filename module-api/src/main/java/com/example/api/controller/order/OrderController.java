package com.example.api.controller.order;

import com.example.api.dto.order.OrderRequest;
import com.example.api.dto.order.OrderResponse;
import com.example.api.infra.auth.CustomUserDetails;
import com.example.api.usecase.order.CreateOrderUseCase;
import com.example.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

	private final CreateOrderUseCase createOrderUseCase;

	@PostMapping
	public ResponseEntity<ApiResponse<OrderResponse.Create>> createOrder(
		@AuthenticationPrincipal CustomUserDetails user,
		@Valid @RequestBody OrderRequest.Create request) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.success(createOrderUseCase.createOrder(user.getId(), request)));
	}

}
