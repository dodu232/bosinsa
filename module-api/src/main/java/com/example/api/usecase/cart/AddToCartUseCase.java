package com.example.api.usecase.cart;

import com.example.api.dto.cart.CartRequest;
import com.example.api.dto.cart.CartView;
import com.example.api.facade.cart.CartFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddToCartUseCase {

	private final CartFacade cartFacade;

	public CartView add(String cartId, CartRequest.AddItems dto) {

		return cartFacade.addItem(cartId, dto.getItems());
	}
}
