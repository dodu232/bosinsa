package com.example.api.usecase.cart;

import com.example.api.dto.cart.CartView;
import com.example.api.facade.cart.CartFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViewCartUseCase {

	private final CartFacade cartFacade;

	public CartView getCart(String cartId) {
		
		return cartFacade.getCart(cartId);

	}

}
