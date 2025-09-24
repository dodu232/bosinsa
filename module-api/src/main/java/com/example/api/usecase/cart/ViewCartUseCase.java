package com.example.api.usecase.cart;

import com.example.api.dto.cart.CartView;

public interface ViewCartUseCase {

	CartView getCart(String cartId);
}
