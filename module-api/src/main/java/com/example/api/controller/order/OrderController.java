package com.example.api.controller.order;

import com.example.api.dto.order.OrderRequest;
import com.example.api.dto.order.OrderResponse;
import com.example.api.dto.order.PaymentComplete;
import com.example.api.dto.order.PaymentInitResponse;
import com.example.api.infra.auth.CustomUserDetails;
import com.example.api.usecase.order.CompletePaymentUseCase;
import com.example.api.usecase.order.CreateOrderUseCase;
import com.example.api.usecase.order.GetOrderUseCase;
import com.example.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

	private final CreateOrderUseCase createOrderUseCase;
	private final GetOrderUseCase getOrderUseCase;
	private final CompletePaymentUseCase completePaymentUseCase;


	@PostMapping
	public ResponseEntity<ApiResponse<OrderResponse.Create>> createOrder(
		@AuthenticationPrincipal CustomUserDetails user,
		@Valid @RequestBody OrderRequest.Create request) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.success(createOrderUseCase.createOrder(user.getId(), request)));
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<ApiResponse<PaymentInitResponse>> getOrder(
		@PathVariable Long orderId
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.success(getOrderUseCase.getOrder(orderId)));
	}

	@PostMapping("/payment/complete")
	public ResponseEntity<ApiResponse<String>> completePayment(
		@RequestBody PaymentComplete request
	) {
		completePaymentUseCase.completePayment(request);
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.success("결제 완료"));
	}

}
