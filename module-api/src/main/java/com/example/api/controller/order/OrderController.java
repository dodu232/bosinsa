package com.example.api.controller.order;

import com.example.api.usecase.order.CreateOrderUseCase;
import com.example.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

	private final CreateOrderUseCase createOrderUseCase;

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> createOrder(
		@AuthenticationPrincipal UserDetails userDetails
	) {
		System.out.println(userDetails);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>());
	}

}
