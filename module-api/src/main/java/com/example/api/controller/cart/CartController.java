package com.example.api.controller.cart;

import com.example.api.dto.cart.CartRequest;
import com.example.api.dto.cart.CartView;
import com.example.api.usecase.cart.AddToCartUseCase;
import com.example.api.usecase.cart.RemoveFromCartUseCase;
import com.example.api.usecase.cart.ViewCartUseCase;
import com.example.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

	private final AddToCartUseCase addToCartUseCase;
	private final ViewCartUseCase viewCartUseCase;
	private final RemoveFromCartUseCase removeFromCartUseCase;

	@GetMapping
	public ResponseEntity<ApiResponse<CartView>> getCarts(
		@CookieValue(value = "cart_id", required = false) String cartId
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.success(viewCartUseCase.getCart(cartId)));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<CartView>> add(
		@CookieValue(value = "cart_id", required = false) String cartId,
		@Valid @RequestBody CartRequest.AddItems request
	) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ApiResponse.success(addToCartUseCase.add(cartId, request)));
	}

	@DeleteMapping
	public ResponseEntity<ApiResponse<CartView>> remove(
		@CookieValue(value = "cart_id", required = false) String cartId,
		@Valid @RequestBody CartRequest.DeleteItems request
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.success(removeFromCartUseCase.remove(cartId, request)));
	}

}
