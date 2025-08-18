package com.example.api.usecase.cart;

import com.example.api.dto.cart.CartRequest;
import com.example.api.dto.cart.CartView;
import com.example.api.facade.cart.CartFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveFromCartUseCase {

	private final CartFacade cartFacade;

	public CartView remove(String cartId, CartRequest.DeleteItems dto) {

		return cartFacade.removeItem(cartId, dto.getProductIds());
	}
}
