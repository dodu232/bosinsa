package com.example.api.controller.cart;

import com.example.api.dto.cart.CartRequest;
import com.example.api.usecase.cart.AddToCartUseCase;
import com.example.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

	private final AddToCartUseCase addToCartUseCase;

	@PostMapping
	public ResponseEntity<ApiResponse<?>> AddToCart(
		@CookieValue(value = "cart_id", required = false) String cartId,
		@Valid @RequestBody CartRequest.AddItems request
	) {
		addToCartUseCase.addToCart(cartId, request);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ApiResponse.success(null));
	}

}
